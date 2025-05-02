# Mock IELTS 应用

## 项目简介
这是一个基于Spring Cloud的雅思模拟考试系统，提供阅读、写作、听力和口语等模块的练习功能。

## 技术栈
- Spring Boot 2.6.4
- Spring Cloud 2021.0.1
- Spring Cloud Alibaba 2021.0.1.0
- MyBatis
- MySQL 8.0
- Redis
- JWT
- Nacos
- Sentinel

## 项目结构
```
mock-ielts-app/
├── mock-ielts-common/     # 公共模块
├── mock-ielts-users/      # 用户服务
├── mock-ielts-topic/      # 话题服务
├── mock-ielts-speaklisten/# 口语听力服务
└── mock-ielts-filesystem/ # 文件系统服务
```

## 功能模块
1. 用户管理
   - 用户注册
   - 用户登录
   - 角色权限管理

2. 话题管理
   - 话题创建
   - 话题列表
   - 话题详情
   - 话题更新
   - 话题删除

3. 口语听力
   - 听力练习
   - 口语练习
   - 评分系统

4. 文件系统
   - 文件上传
   - 文件下载
   - 文件管理

## 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Nacos 1.4.2
- Sentinel 1.8.0

### 配置说明
1. 数据库配置
   - 创建数据库：mock_ielts
   - 执行SQL脚本：`mysql第五版.sql`

2. Nacos配置
   - 启动Nacos服务
   - 配置地址：localhost:8848
   - 命名空间：1695f1a3-225f-4479-9dc1-1f71f7c845a7

3. Redis配置
   - 启动Redis服务
   - 默认配置：localhost:6379

### 启动顺序
1. 启动Nacos
2. 启动Sentinel
3. 启动Redis
4. 启动各个微服务模块

### 服务端口
- mock-ielts-common: 9490
- mock-ielts-users: 9480
- mock-ielts-topic: 9485
- mock-ielts-speaklisten: 9486
- mock-ielts-filesystem: 9487

## 开发指南

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用统一的代码格式化工具
- 保持代码注释完整

### 分支管理
- master: 主分支，用于生产环境
- develop: 开发分支
- feature/*: 功能分支
- hotfix/*: 紧急修复分支

### 提交规范
- feat: 新功能
- fix: 修复bug
- docs: 文档更新
- style: 代码格式调整
- refactor: 重构
- test: 测试相关
- chore: 构建过程或辅助工具的变动

## 部署说明
1. 打包
```bash
mvn clean package -DskipTests
```

2. 运行
```bash
java -jar target/mock-ielts-xxx.jar
```

## 注意事项
1. 确保所有依赖服务都已正确启动
2. 检查配置文件中的连接信息是否正确
3. 注意数据库的字符集设置
4. 确保Redis有足够的内存空间

## 常见问题
1. 服务注册失败
   - 检查Nacos服务是否正常运行
   - 验证网络连接是否正常
   - 确认配置信息是否正确

2. 数据库连接失败
   - 检查数据库服务是否启动
   - 验证用户名密码是否正确
   - 确认数据库名称是否正确

3. Redis连接失败
   - 检查Redis服务是否启动
   - 验证Redis密码是否正确
   - 确认Redis端口是否被占用

## 联系方式
欢迎各位交流，应届不断学习中的小菜鸡一枚，邮箱：caixianduo@foxmail.com