package com.apps.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
/*评测示例默认为中文句子题型的示例,
其他试题示例请到Demo中查看试题示例与音频示例并注意修改相关评测参数值,
到平台文档下方进行音频试题示例下载也可以*/

/**
 * 讯飞语音评测工具类
 * 支持并发调用
 */
public class SpeakToAiUtil extends WebSocketListener {
    // 将静态变量改为实例变量
    private String textChoice = "What is your full name?";
    private String textKeywords = "What is your full name?";
    private static final String hostUrl = "https://ise-api.xfyun.cn/v2/open-ise ";//开放评测地址
    private static final String appid = "b2431dd3";//控制台获取
    private static final String apiSecret = "NGM1NDc1YjYzY2E2OTRmMWJhNGRkYjQz";//控制台获取
    private static final String apiKey = "ca4fb6c939220ad1a715e995cef83a05";//控制台获取

    private static final String sub = "ise";//服务类型sub,开放评测值为ise
    private static final String ent = "en_vip";//语言标记参数 ent(cn_vip中文,en_vip英文)

    //题型、文本、音频要请注意做同步变更(如果是英文评测,请注意变更ent参数的值)
    private static final String category = "simple_expression";//题型
    private String text = "";//评测试题,英文试题:[content]\nthere was a gentleman live near my house.
// private static String text="[choice]\n" +
//		 "1. What should I do with the topic?\n" +
//		 "2. How can I deal with the topic?\n" +
//		 "3. What can I do with the topic?\n" +
//		 "4. What should I do with this subject?\n" +
//		 "5. How can I deal with this subject?\n" +
//		 "6. What can I do with this subject?\n" +
//		 "7. What should I do with this title?\n" +
//		 "8. How can I deal with this title?\n" +
//		 "9. What can I do with this title?\n" +
//		 "10. What should I manage this title?\n" +
//		 "11. How can I manage this title?\n" +
//		 "12. What can I manage this title?\n" +
//		 "13. What should I manage this subject?\n" +
//		 "14. How can I manage this subject?\n" +
//		 "15. What should I manage this topic?\n" +
//		 "16. How can I manage this topic?\n" +
//		 "17. What can I manage this topic?\n" +
//		 "18. How should I deal with this topic?\n" +
//		 "19. How should I deal with this title?\n" +
//		 "20. How should I deal with this subject?\n" +
//		 "[keywords]\n" +
//		 "what do topic";

    private String file = "";//评测音频,如传mp3格式请改变参数aue的值为lame，wav格式则为raw

    public static final int StatusFirstFrame = 0;//第一帧
    public static final int StatusContinueFrame = 1;//中间帧
    public static final int StatusLastFrame = 2;//最后一帧

    final Base64.Encoder encoder = Base64.getEncoder();//编码
    final Base64.Decoder decoder = Base64.getDecoder();//解码

    public static final Gson json = new Gson();
    /*private int aus = 1;
    private static Date dateBegin = new Date();// 开始时间
    private static Date dateEnd = new Date();// 结束时间
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");*/
    private long beginTime = 0;
    private long endTime = 0;

    // 用于同步等待评测结果
    private CountDownLatch latch;
    // 存储最终评测结果
    private String finalResult = "";
    
    // 用于同步的锁
    private final ReentrantLock lock = new ReentrantLock();
    
    // 用于标识当前实例是否正在处理评测
    private volatile boolean isProcessing = false;
    private static final Log logger = LogFactory.getLog(SpeakToAiUtil.class);

    /**
     * 评测音频文件与文本的匹配度
     * 
     * @param audioFilePath 音频文件路径
     * @param questionChoice 问题选项
     * @param questionKeywords 问题关键词
     * @return 评测结果分数（double类型）
     * @throws Exception 评测过程中的异常
     */
    public double evaluateAudio(String audioFilePath, String questionChoice, String questionKeywords) throws Exception {
        // 使用锁确保同一时间只有一个线程可以调用此方法
        lock.lock();
        try {
            if (isProcessing) {
                return -1.0; // 返回-1表示评测正在进行中
            }
            
            isProcessing = true;
            
            // 设置评测参数
            this.file = audioFilePath;
            this.textChoice = questionChoice;
            this.textKeywords = questionKeywords;
            this.text = "[choice]\n1. " + questionChoice + "\n[keywords]\n" + questionKeywords;
            
            System.out.println("即将评测文本是：" + this.text);
            System.out.println("评测音频文件是：" + this.file);
            
            // 初始化同步锁
            latch = new CountDownLatch(1);
            finalResult = "";
            
            // 构建鉴权url
            String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
            OkHttpClient client = new OkHttpClient.Builder().build();
            System.out.println("鉴权URL: " + authUrl);
            
            // 将http(s)替换为ws(s)
            String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
            Request request = new Request.Builder().url(url).build();
            System.out.println("WebSocket URL: " + url);
            
            // 创建WebSocket连接
            WebSocket webSocket = client.newWebSocket(request, this);
            
            // 等待评测结果，最多等待120秒
            boolean completed = latch.await(120, TimeUnit.SECONDS);
            if (!completed) {
                logger.warn("评测超时，未收到结果----audioFilePath: " + audioFilePath);
                return 0; // 评测超时
            }
            
            // 关闭WebSocket连接
            webSocket.close(1000, "");
            
            // 将结果转换为double类型
            try {
                return Double.parseDouble(finalResult);
            } catch (NumberFormatException e) {
                logger.info("评测无法将结果转换为数字: " + finalResult);
                return 0; // 返回-3表示结果转换失败
            }
        } finally {
            isProcessing = false;
            lock.unlock();
        }
    }

    //WebSocket握手连接并上传音频数据
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        new Thread(() -> {
            //连接成功，开始发送数据
            int frameSize = 1280;
            int intervel = 40;
            int status = 0;  // 音频的状态
            ssb(webSocket);
            beginTime = (new Date()).getTime();
            try (FileInputStream fs = new FileInputStream(file)) {
                byte[] buffer = new byte[frameSize];
                end:
                while (true) {
                    int len = fs.read(buffer);
                    if (len == -1) {
                        status = StatusLastFrame;  //文件读完，改变status 为 2
                    }

                    switch (status) {
                        case StatusFirstFrame:   // 第一帧音频status = 0
                            send(webSocket, 1, 1, Base64.getEncoder().encodeToString(Arrays.copyOf(buffer, len)));
                            status = StatusContinueFrame;//中间帧数
                            break;

                        case StatusContinueFrame:  //中间帧status = 1
                            send(webSocket, 2, 1, Base64.getEncoder().encodeToString(Arrays.copyOf(buffer, len)));
                            break;

                        case StatusLastFrame:    // 最后一帧音频status = 2 ，标志音频发送结束
                            send(webSocket, 4, 2, "");
                            System.out.println("发送最后一帧");
                            endTime = (new Date()).getTime();
                            System.out.println("总耗时：" + (endTime - beginTime) + "ms");
                            break end;
                    }
                    Thread.sleep(intervel); //模拟音频采样延时
                }
                System.out.println("所有数据已发送");
            } catch (FileNotFoundException e) {
                System.out.println("文件未找到: " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("IO异常: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("发送音频异常: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
    //上传参数添加与发送
    private void ssb(WebSocket webSocket) {
        ParamBuilder p = new ParamBuilder();
        p.add("common", new ParamBuilder()
                        .add("app_id", appid)
                )
                .add("business", new ParamBuilder()
                        .add("category", category)
                        .add("rstcd", "utf8")

                        //群体：adult成人 、youth（中学，效果与设置pupil参数一致）、pupil小学
                        //仅中文字、词、句题型支持
                        //.add("group", "pupil")

                        //打分门限值：hard、common、easy
                        //仅英文引擎支持
                        .add("check_type","easy")

                        //学段：junior(1,2年级) middle(3,4年级) senior(5,6年级)
                        //仅中文题型：中小学的句子、篇章题型支持
                        //.add("grade","junior")

                        //extra_ability生效条件：ise_unite=1,rst=entirety
                        .add("ise_unite","extra_ability")
                         .add("rst","plain")
                        /*1.全维度(准确度分、流畅度分、完整度打分) ,extra_ability值为multi_dimension
                          2.支持因素错误信息显示(声韵、调型是否正确),extra_ability值为syll_phone_err_msg
                          3.单词基频信息显示（基频开始值、结束值）,extra_ability值为pitch ，仅适用于单词和句子题型
                          4.(字词句篇均适用,如选多个能力，用分号;隔开如:syll_phone_err_msg;pitch;multi_dimension)*/
                        .add("extra_ability","multi_dimension")

                        //试卷部分添加拼音,限制条件：添加拼音的汉字个数不超过整个试卷中汉字个数的三分之一。
                        //jin1|tian1|天气怎么样支持

                        //分制转换，rst=entirety是默认值，请根据文档推荐选择使用百分制


                        .add("sub",sub)
                        .add("ent",ent)
                        .add("tte", "utf-8")
                        .add("cmd", "ssb")
                        .add("auf", "audio/L16;rate=16000")
                        .add("aue", "raw")
                        //评测文本(new String(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF })+text)
                        .add("text",'\uFEFF'+text)//Base64.getEncoder().encodeToString(text.getBytes())
                ).add("data", new ParamBuilder()
                        .add("status", 0)
                        .add("data", ""));
        //System.err.println(p.toString());
        webSocket.send(p.toString());
    }
    //客户端给服务端发送数据
    public void send(WebSocket webSocket, int aus,int status, String data) {
        ParamBuilder p = new ParamBuilder();
        p.add("business", new ParamBuilder()
                .add("cmd", "auw")
                .add("aus", aus)
                .add("aue", "raw")
        ).add("data",new ParamBuilder()
                .add("status",status)
                .add("data",data)
                .add("data_type",1)
                .add("encoding","raw")
        );
        //System.out.println("发送的数据"+p.toString());
        webSocket.send(p.toString());
    }
    //客户端接收服务端消息
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        //System.out.println(text);
        IseNewResponseData resp = json.fromJson(text, IseNewResponseData.class);
        if (resp != null) {
            if (resp.getCode() != 0) {
                System.out.println("错误码: " + resp.getCode() + " 错误信息: " + resp.getMessage() + " 会话ID: " + resp.getSid());
                System.out.println("错误码查询链接：https://www.xfyun.cn/document/error-code");
           //     finalResult = "-4.0"; // 返回-4表示评测失败
                finalResult = "0";
                latch.countDown();
                return;
            }
            if (resp.getData() != null) {
                if (resp.getData().getData() != null) {
                    //中间结果处理
                    System.out.println("收到中间结果");
                }
                if (resp.getData().getStatus() == 2) {
                    try {
                        // 获取最终评测结果
                        String result = new String(decoder.decode(resp.getData().getData()), "UTF-8");
                        System.out.println("会话ID: " + resp.getSid() + " 最终识别结果: " + result);
                        
                        // 解析XML结果，提取total_score值
                        if (result.contains("<total_score value=\"")) {
                            int startIndex = result.indexOf("<total_score value=\"") + "<total_score value=\"".length();
                            int endIndex = result.indexOf("\"", startIndex);
                            if (startIndex > 0 && endIndex > startIndex) {
                                String scoreStr = result.substring(startIndex, endIndex);
                                try {
                                    double score = Double.parseDouble(scoreStr);
                                    // 保留两位小数
                                    finalResult = String.format("%.2f", score);
                                } catch (NumberFormatException e) {
                                    finalResult = "-5.0"; // 返回-5表示解析分数失败
                                }
                            } else {
                                finalResult = "-6.0"; // 返回-6表示未找到分数
                            }
                        } else {
                            finalResult = "-7.0"; // 返回-7表示结果格式不正确
                        }
                    } catch (Exception e) {
                        System.out.println("解析结果异常: " + e.getMessage());
                        e.printStackTrace();
                        finalResult = "-8.0"; // 返回-8表示解析结果异常
                    }
                    // 释放同步锁
                    latch.countDown();
                } else {
                    // todo 根据返回的数据处理
                }
            }
        }
        //System.out.println("response==>"+text);
    }
    //鉴权
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        //String date = format.format(new Date());
        //System.err.println(date);
        StringBuilder builder = new StringBuilder("host: ").append(url.getHost()).append("\n").//
                append("date: ").append(date).append("\n").//
                append("GET ").append(url.getPath()).append(" HTTP/1.1");
        //System.err.println(builder);
        Charset charset = Charset.forName("UTF-8");
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        //System.err.println(sha);
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        //System.err.println(authorization);
        HttpUrl httpUrl = HttpUrl.parse("https://" + url.getHost() + url.getPath()).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(charset))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();
        return httpUrl.toString();
    }
    //JSON解析
    private static class IseNewResponseData{
        private int code;
        private String message;
        private String sid;
        private Data data;
        public int getCode() {
            return code;
        }
        public void setCode(int code) {
            this.code = code;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
        public String getSid() {
            return sid;
        }
        public void setSid(String sid) {
            this.sid = sid;
        }
        public Data getData() {
            return data;
        }
        public void setData(Data data) {
            this.data = data;
        }
    }
    private static class Data{
        private int status;
        private String data;
        public int getStatus() {
            return status;
        }
        public void setStatus(int status) {
            this.status = status;
        }
        public String getData() {
            return data;
        }
        public void setData(String data) {
            this.data = data;
        }
    }
    //传参构建
    public static class ParamBuilder {
        private JsonObject jsonObject = new JsonObject();
        public ParamBuilder add(String key, String val) {
            this.jsonObject.addProperty(key, val);
            return this;
        }
        public ParamBuilder add(String key, int val) {
            this.jsonObject.addProperty(key, val);
            return this;
        }
        public ParamBuilder add(String key, boolean val) {
            this.jsonObject.addProperty(key, val);
            return this;
        }
        public ParamBuilder add(String key, float val) {
            this.jsonObject.addProperty(key, val);
            return this;
        }
        public ParamBuilder add(String key, JsonObject val) {
            this.jsonObject.add(key, val);
            return this;
        }
        public ParamBuilder add(String key, ParamBuilder val) {
            this.jsonObject.add(key, val.jsonObject);
            return this;
        }
        @Override
        public String toString() {
            return this.jsonObject.toString();
        }
    }
}