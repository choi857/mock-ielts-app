-- 创建用户表（COL_Users）只用了第一版的COL_Users
CREATE TABLE COL_Users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
                       username VARCHAR(255) NOT NULL COMMENT '用户名',
                       password VARCHAR(255) NOT NULL COMMENT '密码',
                       email VARCHAR(255) NOT NULL COMMENT '电子邮件',
                       role VARCHAR(50) NOT NULL COMMENT '用户角色',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                       deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
) COMMENT='用户表';

# 阅读材料表
CREATE TABLE COL_READING (
                             COL_ID BIGINT PRIMARY KEY AUTO_INCREMENT, -- 阅读材料ID
                             COL_TITLE VARCHAR(255) NOT NULL,          -- 阅读材料标题
                             COL_CONTENT TEXT NOT NULL,                -- 阅读材料内容
                             COL_IMAGE_BASE64 TEXT DEFAULT NULL, -- 存储图片的 Base64 编码
                             COL_CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
                             COL_UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 更新时间
)COMMENT='阅读材料表';

# 题目表
CREATE TABLE COL_QUESTION (
                              COL_ID BIGINT PRIMARY KEY AUTO_INCREMENT, -- 题目ID
                              COL_READING_ID BIGINT NOT NULL,           -- 关联的阅读材料ID
                              COL_TYPE ENUM('SINGLE_CHOICE', 'FILL_IN_THE_BLANK', 'MATCHING') NOT NULL, -- 题目类型
                              COL_CONTENT TEXT NOT NULL,                -- 题目内容
                              COL_PLACEHOLDER_FORMAT VARCHAR(255) DEFAULT NULL, -- 填空题占位符格式（如 "{{1}} and {{2}}"）
                              COL_CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
                              COL_UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新时间
                              FOREIGN KEY (COL_READING_ID) REFERENCES COL_READING(COL_ID) ON DELETE CASCADE
)COMMENT='题目表';

# 答案表
CREATE TABLE COL_ANSWER (
                            COL_ID BIGINT PRIMARY KEY AUTO_INCREMENT, -- 答案ID
                            COL_QUESTION_ID BIGINT NOT NULL,          -- 关联的题目ID
                            COL_CONTENT TEXT NOT NULL,                -- 答案内容
                            COL_IS_CORRECT BOOLEAN DEFAULT FALSE,     -- 是否为正确答案（用于选择题）
                            COL_BLANK_NUMBER INT DEFAULT NULL,        -- 填空题空的序号（如 1, 2）
                            COL_MATCHING_KEY VARCHAR(50) DEFAULT NULL, -- 配对题的匹配键（如 "A", "B"）
                            COL_CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
                            COL_UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新时间
                            FOREIGN KEY (COL_QUESTION_ID) REFERENCES COL_QUESTION(COL_ID) ON DELETE CASCADE
)COMMENT='答案表';


# 新增表单
CREATE TABLE COL_ROLE (
                          role_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
                          role_name VARCHAR(50) NOT NULL  COMMENT '角色名称(如ADMIN/STUDENT/TEACHER)',
                          permissions TEXT COMMENT 'JSON格式存储的权限配置',
                          description VARCHAR(255) COMMENT '角色描述',
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='用户角色表（通过COL_Users.role字段关联）';

CREATE TABLE COL_USER_ANSWER_RECORD (
                                        record_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '唯一答题记录ID',
                                        user_id INT NOT NULL COMMENT '关联COL_Users.user_id',
                                        reading_id BIGINT NOT NULL COMMENT '关联COL_READING.COL_ID',
                                        score DECIMAL(5,2) COMMENT '本次答题得分',
                                        duration_seconds INT COMMENT '答题耗时（秒）',
                                        device_type ENUM('WEB','MOBILE','TABLET') COMMENT '答题设备类型',
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '答题开始时间',
                                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) COMMENT='用户答题主记录表（1条记录对应多个答案明细）';

CREATE TABLE COL_USER_ANSWER_DETAIL (
                                        detail_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '答案明细ID',
                                        record_id BIGINT NOT NULL COMMENT '关联COL_USER_ANSWER_RECORD.record_id',
                                        user_id INT NOT NULL COMMENT '关联COL_Users.user_id',
                                        question_id BIGINT NOT NULL COMMENT '关联COL_QUESTION.COL_ID',
                                        answer_type ENUM('SINGLE','FILL_BLANK','MATCH') COMMENT '对应题目类型',
                                        submitted_answer TEXT NOT NULL COMMENT '用户提交答案（JSON格式）',
                                        is_correct BOOLEAN COMMENT '是否正确（自动批改时填充）',
                                        blank_index INT COMMENT '填空题空序号（当answer_type=FILL_BLANK时有效）',
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间'
) COMMENT='用户答案明细表（通过record_id实现批量关联）';