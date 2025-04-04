# JWT认证模块使用指南

## 1. 模块概述

JWT认证模块提供了以下功能：
- JWT令牌的生成和验证
- 用户认证拦截
- 基于角色的访问控制
- 无状态会话管理
- Token刷新机制

## 2. 配置说明

### 2.1 添加依赖

在需要使用JWT认证的模块的`pom.xml`中添加以下依赖：

```xml
<dependency>
    <groupId>com.apps</groupId>
    <artifactId>mock-ielts-common</artifactId>
    <version>${project.version}</version>
</dependency>
```

### 2.2 配置文件

在`application.yml`中添加JWT配置：

```yaml
jwt:
  secret: your-secret-key-must-be-at-least-32-characters-long  # JWT密钥
  expiration: 86400000    # 令牌过期时间（毫秒）
  refresh-expiration: 604800000  # 刷新令牌过期时间（毫秒）
  header: Authorization   # 请求头名称
  token-prefix: Bearer    # Token前缀
  exclude-paths:         # 不需要验证的路径
    - /login
    - /register
    - /error
    - /auth/token
    - /auth/refresh
```

## 3. 认证接口

### 3.1 获取Token

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/token")
    public Result<Map<String, String>> getToken(@RequestBody LoginRequest request) {
        return Result.success(authService.generateToken(request));
    }

    @PostMapping("/refresh")
    public Result<Map<String, String>> refreshToken(@RequestBody RefreshTokenRequest request) {
        return Result.success(authService.refreshToken(request));
    }
}
```

### 3.2 请求参数

登录请求：
```json
{
    "username": "admin",
    "password": "123456"
}
```

刷新Token请求：
```json
{
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 3.3 响应结果

成功响应：
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9...",
        "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
        "expiresIn": 86400000,
        "refreshExpiresIn": 604800000
    }
}
```

失败响应：
```json
{
    "code": 401,
    "message": "用户名或密码错误",
    "data": null
}
```

## 4. 使用示例

### 4.1 在Controller中使用

```java
@RestController
@RequestMapping("/api/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    // 公开接口，无需认证
    @GetMapping("/public")
    public Result<List<TopicDTO>> getPublicTopics() {
        return Result.success(topicService.getPublicTopics());
    }

    // 需要USER角色认证的接口
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/list")
    public Result<List<TopicDTO>> getUserTopics() {
        return Result.success(topicService.getUserTopics());
    }

    // 需要ADMIN角色认证的接口
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public Result<Void> createTopic(@RequestBody TopicDTO topicDTO) {
        topicService.createTopic(topicDTO);
        return Result.success();
    }

    // 需要ADMIN角色认证的接口
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public Result<Void> updateTopic(@PathVariable Long id, @RequestBody TopicDTO topicDTO) {
        topicService.updateTopic(id, topicDTO);
        return Result.success();
    }

    // 需要ADMIN角色认证的接口
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return Result.success();
    }
}
```

### 4.2 获取当前用户信息

```java
@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private SecurityContextHolder securityContextHolder;

    public List<TopicDTO> getUserTopics() {
        // 获取当前认证用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // 获取用户角色
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // 根据用户角色返回不同的数据
        if (isAdmin) {
            return topicMapper.selectAll();
        } else {
            return topicMapper.selectByUsername(username);
        }
    }
}
```

### 4.3 全局异常处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e) {
        return Result.error(403, "没有权限访问该资源");
    }

    @ExceptionHandler(AuthenticationException.class)
    public Result<Void> handleAuthenticationException(AuthenticationException e) {
        return Result.error(401, "认证失败：" + e.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public Result<Void> handleTokenExpiredException(TokenExpiredException e) {
        return Result.error(401, "Token已过期，请使用refreshToken刷新");
    }
}
```

## 5. API调用示例

### 5.1 获取Token

```http
POST /api/auth/token
Content-Type: application/json

{
    "username": "admin",
    "password": "123456"
}
```

### 5.2 刷新Token

```http
POST /api/auth/refresh
Content-Type: application/json

{
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 5.3 调用需要认证的接口

```http
GET /api/topic/list
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## 6. 注意事项

### 6.1 Token格式
- Token格式为：`Bearer <token>`
- Token必须放在请求头的`Authorization`字段中
- Token过期后需要使用refreshToken获取新的token

### 6.2 安全建议
- 使用HTTPS传输
- 设置合理的token过期时间
- 定期轮换密钥
- 实现token刷新机制
- 敏感操作需要二次验证

### 6.3 性能考虑
- JWT验证是无状态的，不需要查询数据库
- 但token中不要存储过多信息，以免影响性能
- 合理设置token过期时间，避免频繁刷新

### 6.4 错误处理
- 401：未认证或token无效
- 403：已认证但无权限
- 400：请求参数错误
- 500：服务器内部错误

### 6.5 常见问题
1. Token过期
   - 客户端需要重新登录获取新token
   - 可以使用refreshToken刷新token
   - 建议在token过期前主动刷新

2. Token无效
   - 检查token格式是否正确
   - 确认token是否被篡改
   - 验证token签名是否正确
   - 检查token是否过期

3. 权限不足
   - 检查用户角色是否正确
   - 确认接口权限配置是否正确
   - 验证用户是否被禁用

4. 刷新Token失败
   - 检查refreshToken是否过期
   - 确认refreshToken是否有效
   - 验证refreshToken是否被撤销 