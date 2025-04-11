package com.apps.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import okhttp3.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 讯飞星火大模型工具类
 * 用于调用讯飞星火大模型API，接收用户和AI的对话内容，返回AI的回答内容
 * 
 * 文档更新说明：
 * 每次更新代码后，请同步更新 SparkModelUtil.md 文档，确保文档与代码保持一致。
 * 文档更新内容包括：参数说明、方法说明、使用示例和注意事项。
 */
public class SparkModelUtil {
    // 各版本的hostUrl及其对应的domain参数，具体可以参考接口文档 https://www.xfyun.cn/doc/spark/Web.html
    // Spark Lite      https://spark-api.xf-yun.com/v1.1/chat      domain参数为lite
    // Spark Pro       https://spark-api.xf-yun.com/v3.1/chat      domain参数为generalv3
    // Spark Pro-128K  https://spark-api.xf-yun.com/chat/pro-128k  domain参数为pro-128k
    // Spark Max       https://spark-api.xf-yun.com/v3.5/chat      domain参数为generalv3.5
    // Spark Max-32K   https://spark-api.xf-yun.com/chat/max-32k   domain参数为max-32k
    // Spark4.0 Ultra  https://spark-api.xf-yun.com/v4.0/chat      domain参数为4.0Ultra

    // 默认配置参数
    /**
     * 默认的WebSocket连接地址
     * 对应Spark4.0 Ultra版本
     */
    private static final String DEFAULT_HOST_URL = "wss://spark-api.xf-yun.com/v4.0/chat";
    
    /**
     * 默认的模型域名
     * 对应Spark4.0 Ultra版本
     */
    private static final String DEFAULT_DOMAIN = "4.0Ultra";
    
    /**
     * 默认的最大token数
     * 根据接口文档，Pro、Max、Max-32K、4.0 Ultra 取值为[1,8192]，默认为4096
     * Lite、Pro-128K 取值为[1,4096]，默认为4096
     */
    private static final int DEFAULT_MAX_TOKENS = 4096;
    
    /**
     * 默认的温度参数
     * 根据接口文档，取值范围为(0，1]，默认值0.5
     * 核采样阈值，取值越高随机性越强，即相同的问题得到的不同答案的可能性越大
     */
    private static final float DEFAULT_TEMPERATURE = 0.5f;
    
    /**
     * 默认的top_k参数
     * 根据接口文档，取值为[1，6]，默认为4
     * 从k个候选中随机选择一个（非等概率）
     */
    private static final int DEFAULT_TOP_K = 4;
    
    /**
     * 默认的超时时间（秒）
     * 用于控制WebSocket连接的最大等待时间
     */
    private static final int DEFAULT_TIMEOUT_SECONDS = 60;

    // 配置参数
    /**
     * WebSocket连接地址
     * 不同版本的模型对应不同的连接地址
     */
    private final String hostUrl;
    
    /**
     * 模型域名
     * 不同版本的模型对应不同的域名参数
     */
    private final String domain;
    
    /**
     * 应用ID
     * 从开放平台控制台创建的应用中获取
     */
    private final String appId;
    
    /**
     * API密钥对应的密钥
     * 从开放平台控制台创建的应用中获取
     */
    private final String apiSecret;
    
    /**
     * API密钥
     * 从开放平台控制台创建的应用中获取
     */
    private final String apiKey;
    
    /**
     * 最大token数
     * 控制模型回答的最大长度
     */
    private final int maxTokens;
    
    /**
     * 温度参数
     * 控制模型回答的随机性
     */
    private final float temperature;
    
    /**
     * top_k参数
     * 控制模型从候选中选择的策略
     */
    private final int topK;
    
    /**
     * 超时时间（秒）
     * 控制WebSocket连接的最大等待时间
     */
    private final int timeoutSeconds;

    // 用于解析JSON的Gson对象
    private static final Gson gson = new Gson();

    /**
     * 私有构造函数，使用Builder模式创建实例
     */
    private SparkModelUtil(Builder builder) {
        this.hostUrl = builder.hostUrl;
        this.domain = builder.domain;
        this.appId = builder.appId;
        this.apiSecret = builder.apiSecret;
        this.apiKey = builder.apiKey;
        this.maxTokens = builder.maxTokens;
        this.temperature = builder.temperature;
        this.topK = builder.topK;
        this.timeoutSeconds = builder.timeoutSeconds;
    }

    /**
     * 获取Builder实例
     * @return Builder实例
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 调用星火大模型API，获取AI回答
     * @param systemContent 系统角色设定内容，可以为null
     *                  根据接口文档，system用于设置对话背景（仅Max、Ultra版本支持）
     *                  如果传入system参数，需要保证第一条是system
     * @param userContent 用户问题内容
     *                  根据接口文档，所有content的累计tokens长度，不同版本限制不同：
     *                  Lite、Pro、Max、4.0 Ultra版本: 不超过8192;
     *                  Max-32K版本: 不超过32* 1024;
     *                  Pro-128K版本:不超过 128*1024;
     * @return AI的回答内容
     * @throws Exception 调用过程中的异常
     */
    public String chat(String systemContent, String userContent) throws Exception {
        if (userContent == null || userContent.trim().isEmpty()) {
            throw new IllegalArgumentException("用户问题内容不能为空");
        }

        // 创建用于等待WebSocket响应完成的CountDownLatch
        CountDownLatch latch = new CountDownLatch(1);
        // 用于存储AI的回答
        final StringBuilder answerBuilder = new StringBuilder();
        // 用于标记WebSocket是否已关闭
        final boolean[] wsClosed = {false};

        // 构建鉴权url
        String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();

        // 创建WebSocket监听器
        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                try {
                    // 构建请求参数
                    JSONObject requestJson = buildRequestJson(systemContent, userContent);
                    // 发送请求
                    webSocket.send(requestJson.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    wsClosed[0] = true;
                    latch.countDown();
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    // 解析响应
                    JsonParse jsonParse = gson.fromJson(text, JsonParse.class);
                    
                    // 检查错误码
                    if (jsonParse.header.code != 0) {
                        System.err.println("发生错误，错误码为：" + jsonParse.header.code);
                        System.err.println("本次请求的sid为：" + jsonParse.header.sid);
                        wsClosed[0] = true;
                        latch.countDown();
                        return;
                    }
                    
                    // 获取回答内容
                    List<Text> textList = jsonParse.payload.choices.text;
                    for (Text temp : textList) {
                        answerBuilder.append(temp.content);
                    }
                    
                    // 如果是最后一个消息，关闭WebSocket
                    // 根据接口文档，status=2表示最后一个结果
                    if (jsonParse.header.status == 2) {
                        wsClosed[0] = true;
                        latch.countDown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    wsClosed[0] = true;
                    latch.countDown();
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                try {
                    if (response != null) {
                        System.err.println("连接失败，错误码：" + response.code());
                        System.err.println("错误信息：" + response.body().string());
                    } else {
                        System.err.println("连接失败：" + t.getMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    wsClosed[0] = true;
                    latch.countDown();
                }
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                wsClosed[0] = true;
                latch.countDown();
            }
        };

        // 创建WebSocket连接
        WebSocket webSocket = client.newWebSocket(request, listener);

        // 等待响应完成或超时
        boolean completed = latch.await(timeoutSeconds, TimeUnit.SECONDS);
        
        // 如果超时，关闭WebSocket
        if (!completed) {
            webSocket.close(1000, "请求超时");
        }

        // 如果WebSocket未关闭，强制关闭
        if (!wsClosed[0]) {
            webSocket.close(1000, "强制关闭");
        }

        // 返回AI的回答
        return answerBuilder.toString();
    }

    /**
     * 调用星火大模型API，获取AI回答（支持完整对话历史）
     * @param messages 完整的对话历史记录，包括system、user和assistant角色的消息
     * @return AI的回答内容
     * @throws Exception 调用过程中的异常
     */
    public String chatWithHistory(List<Message> messages) throws Exception {
        if (messages == null || messages.isEmpty()) {
            throw new IllegalArgumentException("对话历史记录不能为空");
        }

        // 创建用于等待WebSocket响应完成的CountDownLatch
        CountDownLatch latch = new CountDownLatch(1);
        // 用于存储AI的回答
        final StringBuilder answerBuilder = new StringBuilder();
        // 用于标记WebSocket是否已关闭
        final boolean[] wsClosed = {false};

        // 构建鉴权url
        String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();

        // 创建WebSocket监听器
        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                try {
                    // 构建请求参数
                    JSONObject requestJson = buildRequestJsonWithHistory(messages);
                    // 发送请求
                    webSocket.send(requestJson.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    wsClosed[0] = true;
                    latch.countDown();
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    // 解析响应
                    JsonParse jsonParse = gson.fromJson(text, JsonParse.class);
                    
                    // 检查错误码
                    if (jsonParse.header.code != 0) {
                        System.err.println("发生错误，错误码为：" + jsonParse.header.code);
                        System.err.println("本次请求的sid为：" + jsonParse.header.sid);
                        wsClosed[0] = true;
                        latch.countDown();
                        return;
                    }
                    
                    // 获取回答内容
                    List<Text> textList = jsonParse.payload.choices.text;
                    for (Text temp : textList) {
                        answerBuilder.append(temp.content);
                    }
                    
                    // 如果是最后一个消息，关闭WebSocket
                    // 根据接口文档，status=2表示最后一个结果
                    if (jsonParse.header.status == 2) {
                        wsClosed[0] = true;
                        latch.countDown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    wsClosed[0] = true;
                    latch.countDown();
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                try {
                    if (response != null) {
                        System.err.println("连接失败，错误码：" + response.code());
                        System.err.println("错误信息：" + response.body().string());
                    } else {
                        System.err.println("连接失败：" + t.getMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    wsClosed[0] = true;
                    latch.countDown();
                }
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                wsClosed[0] = true;
                latch.countDown();
            }
        };

        // 创建WebSocket连接
        WebSocket webSocket = client.newWebSocket(request, listener);

        // 等待响应完成或超时
        boolean completed = latch.await(timeoutSeconds, TimeUnit.SECONDS);
        
        // 如果超时，关闭WebSocket
        if (!completed) {
            webSocket.close(1000, "请求超时");
        }

        // 如果WebSocket未关闭，强制关闭
        if (!wsClosed[0]) {
            webSocket.close(1000, "强制关闭");
        }

        // 返回AI的回答
        return answerBuilder.toString();
    }

    /**
     * 构建请求JSON
     * 根据接口文档，请求参数由三个部分组成：header，parameter, payload
     * 
     * @param systemContent 系统角色设定内容
     * @param userContent 用户问题内容
     * @return 请求JSON对象
     */
    private JSONObject buildRequestJson(String systemContent, String userContent) {
        JSONObject requestJson = new JSONObject();

        // 构建header
        // 根据接口文档，header部分包含app_id和uid
        JSONObject header = new JSONObject();
        header.put("app_id", appId);  // 应用appid，从开放平台控制台创建的应用中获取
        header.put("uid", UUID.randomUUID().toString().substring(0, 10));  // 每个用户的id，非必传字段，用于后续扩展

        // 构建parameter
        // 根据接口文档，parameter.chat部分包含domain、temperature、max_tokens、top_k等参数
        JSONObject parameter = new JSONObject();
        JSONObject chat = new JSONObject();
        chat.put("domain", domain);  // 指定访问的模型版本
        chat.put("temperature", temperature);  // 核采样阈值
        chat.put("max_tokens", maxTokens);  // 模型回答的tokens的最大长度
        chat.put("top_k", topK);  // 从k个候选中随机选择一个
        parameter.put("chat", chat);

        // 构建payload
        // 根据接口文档，payload.message.text部分包含对话内容
        JSONObject payload = new JSONObject();
        JSONObject message = new JSONObject();
        JSONArray text = new JSONArray();

        // 如果有system内容，添加为第一条消息
        // 根据接口文档，如果传入system参数，需要保证第一条是system
        if (systemContent != null && !systemContent.trim().isEmpty()) {
            JSONObject systemObj = new JSONObject();
            systemObj.put("role", "system");  // 根据接口文档，role取值为[system,user,assistant]
            systemObj.put("content", systemContent);
            text.add(systemObj);
        }

        // 添加用户问题
        JSONObject userObj = new JSONObject();
        userObj.put("role", "user");  // 根据接口文档，role取值为[system,user,assistant]
        userObj.put("content", userContent);
        text.add(userObj);

        message.put("text", text);
        payload.put("message", message);

        // 组装完整请求
        requestJson.put("header", header);
        requestJson.put("parameter", parameter);
        requestJson.put("payload", payload);

        return requestJson;
    }

    /**
     * 构建请求JSON（支持完整对话历史）
     * 根据接口文档，请求参数由三个部分组成：header，parameter, payload
     * 
     * @param messages 完整的对话历史记录，包括system、user和assistant角色的消息
     * @return 请求JSON对象
     */
    private JSONObject buildRequestJsonWithHistory(List<Message> messages) {
        JSONObject requestJson = new JSONObject();

        // 构建header
        // 根据接口文档，header部分包含app_id和uid
        JSONObject header = new JSONObject();
        header.put("app_id", appId);  // 应用appid，从开放平台控制台创建的应用中获取
        header.put("uid", UUID.randomUUID().toString().substring(0, 10));  // 每个用户的id，非必传字段，用于后续扩展

        // 构建parameter
        // 根据接口文档，parameter.chat部分包含domain、temperature、max_tokens、top_k等参数
        JSONObject parameter = new JSONObject();
        JSONObject chat = new JSONObject();
        chat.put("domain", domain);  // 指定访问的模型版本
        chat.put("temperature", temperature);  // 核采样阈值
        chat.put("max_tokens", maxTokens);  // 模型回答的tokens的最大长度
        chat.put("top_k", topK);  // 从k个候选中随机选择一个
        parameter.put("chat", chat);

        // 构建payload
        // 根据接口文档，payload.message.text部分包含对话内容
        JSONObject payload = new JSONObject();
        JSONObject message = new JSONObject();
        JSONArray text = new JSONArray();

        // 添加所有消息到text数组
        for (Message msg : messages) {
            JSONObject msgObj = new JSONObject();
            msgObj.put("role", msg.getRole());
            msgObj.put("content", msg.getContent());
            text.add(msgObj);
        }

        message.put("text", text);
        payload.put("message", message);

        // 组装完整请求
        requestJson.put("header", header);
        requestJson.put("parameter", parameter);
        requestJson.put("payload", payload);

        return requestJson;
    }

    /**
     * 鉴权方法
     * 根据接口文档，鉴权需要生成RFC1123格式的时间戳，并进行hmac-sha256加密
     * 
     * @param hostUrl 主机URL
     * @param apiKey API密钥
     * @param apiSecret API密钥对应的密钥
     * @return 鉴权后的URL
     * @throws Exception 鉴权过程中的异常
     */
    private String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        // 将wss协议转换为https进行鉴权
        String httpUrl = hostUrl.replace("wss://", "https://");
        URL url = new URL(httpUrl);
        // 生成RFC1123格式的时间戳
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        
        // 拼接签名字符串
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";
        
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        
        // 拼接authorization
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", 
                apiKey, "hmac-sha256", "host date request-line", sha);
        
        // 拼接完整URL
        HttpUrl httpUrlObj = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder()
                .addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8)))
                .addQueryParameter("date", date)
                .addQueryParameter("host", url.getHost())
                .build();

        return httpUrlObj.toString();
    }

    /**
     * 返回的json结果拆解类
     * 根据接口文档，返回结果分为header和payload两部分
     */
    private static class JsonParse {
        Header header;  // 头部信息
        Payload payload;  // 负载信息
    }

    /**
     * Header部分
     * 根据接口文档，header部分包含code、status、sid等字段
     */
    private static class Header {
        int code;  // 错误码，0表示正常，非0表示出错
        int status;  // 会话状态，取值为[0,1,2]；0代表首次结果；1代表中间结果；2代表最后一个结果
        String sid;  // 会话的唯一id，用于讯飞技术人员查询服务端会话日志使用
    }

    /**
     * Payload部分
     * 根据接口文档，payload部分包含choices字段
     */
    private static class Payload {
        Choices choices;  // 选择结果
    }

    /**
     * Choices部分
     * 根据接口文档，choices部分包含text字段
     */
    private static class Choices {
        List<Text> text;  // 文本结果列表
    }

    /**
     * Text部分
     * 根据接口文档，text部分包含role和content字段
     */
    private static class Text {
        String role;  // 角色标识，固定为assistant，标识角色为AI
        String content;  // AI的回答内容
    }

    /**
     * 消息类，用于表示对话中的一条消息
     */
    public static class Message {
        private String role;  // 角色，取值为[system,user,assistant]
        private String content;  // 消息内容

        /**
         * 构造函数
         * @param role 角色，取值为[system,user,assistant]
         * @param content 消息内容
         */
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        /**
         * 获取角色
         * @return 角色
         */
        public String getRole() {
            return role;
        }

        /**
         * 设置角色
         * @param role 角色
         */
        public void setRole(String role) {
            this.role = role;
        }

        /**
         * 获取消息内容
         * @return 消息内容
         */
        public String getContent() {
            return content;
        }

        /**
         * 设置消息内容
         * @param content 消息内容
         */
        public void setContent(String content) {
            this.content = content;
        }
    }

    /**
     * Builder类，用于构建SparkModelUtil实例
     * 使用Builder模式，使参数配置更加灵活和清晰
     */
    public static class Builder {
        /**
         * 主机URL
         * 不同版本的模型对应不同的连接地址
         */
        private String hostUrl = DEFAULT_HOST_URL;
        
        /**
         * 模型域名
         * 不同版本的模型对应不同的域名参数
         */
        private String domain = DEFAULT_DOMAIN;
        
        /**
         * 应用ID
         * 从开放平台控制台创建的应用中获取
         */
        private String appId;
        
        /**
         * API密钥
         * 从开放平台控制台创建的应用中获取
         */
        private String apiKey;
        
        /**
         * API密钥对应的密钥
         * 从开放平台控制台创建的应用中获取
         */
        private String apiSecret;
        
        /**
         * 最大token数
         * 控制模型回答的最大长度
         */
        private int maxTokens = DEFAULT_MAX_TOKENS;
        
        /**
         * 温度参数
         * 控制模型回答的随机性
         */
        private float temperature = DEFAULT_TEMPERATURE;
        
        /**
         * top_k参数
         * 控制模型从候选中选择的策略
         */
        private int topK = DEFAULT_TOP_K;
        
        /**
         * 超时时间（秒）
         * 控制WebSocket连接的最大等待时间
         */
        private int timeoutSeconds = DEFAULT_TIMEOUT_SECONDS;

        /**
         * 设置主机URL
         * @param hostUrl 主机URL
         * @return Builder实例
         */
        public Builder hostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
            return this;
        }

        /**
         * 设置模型域名
         * @param domain 模型域名
         * @return Builder实例
         */
        public Builder domain(String domain) {
            this.domain = domain;
            return this;
        }

        /**
         * 设置应用ID
         * @param appId 应用ID
         * @return Builder实例
         */
        public Builder appId(String appId) {
            this.appId = appId;
            return this;
        }

        /**
         * 设置API密钥
         * @param apiKey API密钥
         * @return Builder实例
         */
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * 设置API密钥对应的密钥
         * @param apiSecret API密钥对应的密钥
         * @return Builder实例
         */
        public Builder apiSecret(String apiSecret) {
            this.apiSecret = apiSecret;
            return this;
        }

        /**
         * 设置最大token数
         * @param maxTokens 最大token数
         * @return Builder实例
         */
        public Builder maxTokens(int maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        /**
         * 设置温度参数
         * @param temperature 温度参数
         * @return Builder实例
         */
        public Builder temperature(float temperature) {
            this.temperature = temperature;
            return this;
        }

        /**
         * 设置top_k参数
         * @param topK top_k参数
         * @return Builder实例
         */
        public Builder topK(int topK) {
            this.topK = topK;
            return this;
        }

        /**
         * 设置超时时间（秒）
         * @param timeoutSeconds 超时时间（秒）
         * @return Builder实例
         */
        public Builder timeoutSeconds(int timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
            return this;
        }

        /**
         * 构建SparkModelUtil实例
         * @return SparkModelUtil实例
         * @throws IllegalArgumentException 如果必要参数未设置
         */
        public SparkModelUtil build() {
            if (appId == null || appId.trim().isEmpty()) {
                throw new IllegalArgumentException("appId不能为空");
            }
            if (apiKey == null || apiKey.trim().isEmpty()) {
                throw new IllegalArgumentException("apiKey不能为空");
            }
            if (apiSecret == null || apiSecret.trim().isEmpty()) {
                throw new IllegalArgumentException("apiSecret不能为空");
            }
            return new SparkModelUtil(this);
        }
    }
} 