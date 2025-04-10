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

# 阅读题汇总表
CREATE TABLE COL_READING_SUMMARY (
               COL_ID BIGINT PRIMARY KEY AUTO_INCREMENT, -- 阅读题ID
               COL_TITLE VARCHAR(255)  ,          -- 阅读题标题
               COL_CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
               COL_UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 更新时间
) COMMENT='阅读题汇总表';
# 阅读材料表
CREATE TABLE COL_READING (
                             COL_ID BIGINT PRIMARY KEY AUTO_INCREMENT, -- 阅读材料ID
                             COL_READING_SUMMARY_ID BIGINT, -- 关联的阅读题汇总ID
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
                              COL_PART ENUM('Part1', 'Part2', 'Part3', 'Part4') COMMENT '题目所属部分',
                              COL_SERIAL INT  DEFAULT 0 COMMENT '题目序号',
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
                                        answer_evaluation TEXT COMMENT '答题评价',
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
                                        answer_type ENUM('SINGLE_CHOICE', 'FILL_IN_THE_BLANK', 'MATCHING') COMMENT '对应题目类型',
                                        submitted_answer TEXT NOT NULL COMMENT '用户提交答案（JSON格式）',
                                        is_correct BOOLEAN COMMENT '是否正确（自动批改时填充）',
                                        blank_index INT COMMENT '填空题空序号（当answer_type=FILL_BLANK时有效）',
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间'
) COMMENT='用户答案明细表（通过record_id实现批量关联）';

# 第三版开始
CREATE TABLE COL_WRITING_TASK_RECORD (
                                         record_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
                                         task1_id BIGINT NOT NULL COMMENT '关联Task1的题目ID',
                                         task2_id BIGINT NOT NULL COMMENT '关联Task2的题目ID',
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                         CONSTRAINT fk_task1 FOREIGN KEY (task1_id) REFERENCES COL_WRITING_TASK(task_id) ON DELETE CASCADE,
                                         CONSTRAINT fk_task2 FOREIGN KEY (task2_id) REFERENCES COL_WRITING_TASK(task_id) ON DELETE CASCADE
) COMMENT='记录Task1和Task2的题目ID表';
# 写作题目表
CREATE TABLE COL_WRITING_TASK (
                                  task_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '写作题目ID',
                                  task_title VARCHAR(500) COMMENT '写作题目标题',
                                  task_description LONGTEXT COMMENT '写作题目描述',
                                  task_type VARCHAR(255) COMMENT '写作题目类型ENUM(''TASK1'', ''TASK2'')',
                                  task_requirements LONGTEXT COMMENT '写作要求',
                                  word_limit INT COMMENT '字数限制',
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='雅思机考写作题目表';

# 用户写作答题记录主表
CREATE TABLE COL_WRITE_USER_ANSWER_RECORD (
                                              record_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '唯一答题记录ID',
                                              user_id INT COMMENT '关联COL_Users.user_id',
                                              test_id BIGINT COMMENT '关联测试ID（如果适用）',
                                              task1_id BIGINT COMMENT '关联Task1的题目ID',
                                              task2_id BIGINT COMMENT '关联Task2的题目ID',
                                              total_score DECIMAL(5,2) COMMENT '总得分',
                                              duration_seconds INT COMMENT '答题总耗时（秒）',
                                              device_type ENUM('WEB','MOBILE','TABLET') COMMENT '作答设备类型',
                                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '答题开始时间',
                                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) COMMENT='用户写作答题记录表';

# 用户写作答题明细表
CREATE TABLE COL_WRITE_USER_ANSWER_DETAIL (
                                              detail_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '明细ID',
                                              user_id INT COMMENT '关联COL_Users.user_id',
                                              record_id BIGINT NOT NULL COMMENT '关联用户写作答题记录主表ID。COL_WRITE_USER_ANSWER_RECORD.record_id',
                                              task_type VARCHAR(255) NOT NULL COMMENT '写作题目类型ENUM(''TASK1'', ''TASK2'')',
                                              answer_content TEXT COMMENT '写作答案内容',
                                              answer_evaluation TEXT COMMENT '写作答案评价',
                                              score DECIMAL(5,2) COMMENT '该任务得分',
                                              duration_seconds INT COMMENT '该任务答题耗时（秒）',
                                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间'
) COMMENT='用户写作答题明细表';


-- 外键创建语句（注释掉）
/*
ALTER TABLE COL_WRITE_USER_ANSWER_RECORD
ADD CONSTRAINT fk_write_record_user
FOREIGN KEY (user_id) REFERENCES COL_Users(user_id) ON DELETE CASCADE;

ALTER TABLE COL_WRITE_USER_ANSWER_RECORD
ADD CONSTRAINT fk_write_record_task1
FOREIGN KEY (task1_id) REFERENCES COL_WRITING_TASK(task_id) ON DELETE SET NULL;

ALTER TABLE COL_WRITE_USER_ANSWER_RECORD
ADD CONSTRAINT fk_write_record_task2
FOREIGN KEY (task2_id) REFERENCES COL_WRITING_TASK(task_id) ON DELETE SET NULL;

ALTER TABLE COL_WRITE_USER_ANSWER_DETAIL
ADD CONSTRAINT fk_write_detail_record
FOREIGN KEY (record_id) REFERENCES COL_WRITE_USER_ANSWER_RECORD(record_id) ON DELETE CASCADE;
*/

# 写作评分维度表
CREATE TABLE COL_WRITING_SCORE_DIMENSION (
                                             dimension_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评分维度ID',
                                             task_id BIGINT NOT NULL COMMENT '关联写作题目表COL_WRITING_TASK.task_id',
                                             dimension_name VARCHAR(255)   COMMENT '评分维度名称',
                                             dimension_description TEXT   COMMENT '评分维度描述',
                                             max_score INT DEFAULT 25 COMMENT '该维度满分',
                                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='雅思机考写作评分维度表';

-- 外键创建语句（注释掉）
/*
ALTER TABLE COL_WRITING_SCORE_DIMENSION
ADD CONSTRAINT fk_writing_score_dimension_task
FOREIGN KEY (task_id) REFERENCES COL_WRITING_TASK(task_id) ON DELETE CASCADE;
*/

# 写作答案评分明细表
CREATE TABLE COL_WRITING_ANSWER_SCORE (
       score_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评分明细ID',
       answer_id BIGINT NOT NULL COMMENT '关联COL_WRITE_USER_ANSWER_DETAIL.detail_id',
       dimension_id BIGINT NOT NULL COMMENT '关联写作评分维度表COL_WRITING_SCORE_DIMENSION.dimension_id',
       max_score INT DEFAULT 25 COMMENT '该维度满分',
       score DECIMAL(5,2) COMMENT '得分',
       comments TEXT COMMENT '评分评语',
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '评分时间'
) COMMENT='雅思机考写作答案评分明细表';

-- 外键创建语句（注释掉）
/*
ALTER TABLE COL_WRITING_ANSWER_SCORE
ADD CONSTRAINT fk_writing_answer_score_answer
FOREIGN KEY (answer_id) REFERENCES COL_USER_WRITING_ANSWER(answer_id) ON DELETE CASCADE;

ALTER TABLE COL_WRITING_ANSWER_SCORE
ADD CONSTRAINT fk_writing_answer_score_dimension
FOREIGN KEY (dimension_id) REFERENCES COL_WRITING_SCORE_DIMENSION(dimension_id) ON DELETE CASCADE;
*/
-- 创建听力材料表（COL_LISTENING）
CREATE TABLE COL_LISTENING (
     COL_ID BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '听力材料ID',
     COL_TITLE VARCHAR(255) COMMENT '听力材料标题',
     COL_CONTENT TEXT COMMENT '听力材料内容',
     COL_AUDIO_URL VARCHAR(255) COMMENT '听力材料音频URL',
     COL_CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     COL_UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='听力材料表';

-- 创建听力题目表（COL_LISTENING_QUESTION）
CREATE TABLE COL_LISTENING_QUESTION (
    COL_ID BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '题目ID',
    COL_LISTENING_ID BIGINT COMMENT '关联的听力材料ID', -- 关联COL_LISTENING(COL_ID)
    COL_TYPE ENUM('SINGLE_CHOICE', 'FILL_IN_THE_BLANK', 'MATCHING') COMMENT '题目类型',
    COL_CONTENT TEXT COMMENT '题目内容',
    COL_PLACEHOLDER_FORMAT VARCHAR(255) COMMENT '填空题占位符格式（如 "{{1}},{{2}}"）',
    COL_CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    COL_UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    COL_PART ENUM('Part1', 'Part2', 'Part3', 'Part4') COMMENT '题目所属部分',
    COL_IMAGE_URL  VARCHAR(255) NULL COMMENT '题目图片URL',
    COL_SERIAL INT  DEFAULT 0 COMMENT '题目序号'
) COMMENT='听力题目表';

-- 创建听力答案表（COL_LISTENING_ANSWER）
CREATE TABLE COL_LISTENING_ANSWER (
    COL_ID BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '答案ID',
    COL_QUESTION_ID BIGINT COMMENT '关联的题目ID', -- 关联COL_LISTENING_QUESTION(COL_ID)
    COL_CONTENT TEXT COMMENT '答案内容',
    COL_IS_CORRECT BOOLEAN DEFAULT FALSE COMMENT '是否为正确答案（用于选择题）',
    COL_BLANK_NUMBER INT COMMENT '填空题空的序号（如 1, 2）',
    COL_MATCHING_KEY VARCHAR(50) COMMENT '配对题的匹配键（如 "A", "B"）',
    COL_CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    COL_UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    COL_PART ENUM('Part1', 'Part2', 'Part3', 'Part4') COMMENT '题目所属部分'
) COMMENT='听力答案表';

-- 创建用户听力答题记录主表（COL_LISTEN_USER_ANSWER_RECORD）
CREATE TABLE COL_LISTEN_USER_ANSWER_RECORD (
    RECORD_ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '唯一答题记录ID',
    USER_ID INT COMMENT '关联COL_Users.user_id', -- 关联COL_Users(user_id)
    LISTENING_ID BIGINT COMMENT '关联听力材料ID', -- 关联COL_LISTENING(COL_ID)
    SCORE DECIMAL(5,2) COMMENT '本次答题得分',
    ANSWER_EVALUATION TEXT COMMENT '答题评价',
    DURATION_SECONDS INT COMMENT '答题耗时（秒）',
    DEVICE_TYPE ENUM('WEB','MOBILE','TABLET') COMMENT '答题设备类型',
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '答题开始时间',
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    COL_PART ENUM('Part1', 'Part2', 'Part3', 'Part4') COMMENT '题目所属部分'
) COMMENT='用户听力答题主记录表（1条记录对应多个答案明细）';

-- 创建用户听力答题明细表（COL_LISTEN_USER_ANSWER_DETAIL）
CREATE TABLE COL_LISTEN_USER_ANSWER_DETAIL (
    DETAIL_ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '答案明细ID',
    RECORD_ID BIGINT COMMENT '关联COL_LISTEN_USER_ANSWER_RECORD.RECORD_ID', -- 关联用户听力答题记录主表ID
    USER_ID INT COMMENT '关联COL_Users.user_id', -- 关联COL_Users(user_id)
    QUESTION_ID BIGINT COMMENT '关联COL_LISTENING_QUESTION.COL_ID', -- 关联COL_LISTENING_QUESTION(COL_ID)
    ANSWER_TYPE ENUM('SINGLE_CHOICE', 'FILL_IN_THE_BLANK', 'MATCHING') COMMENT '对应题目类型',
    SUBMITTED_ANSWER TEXT COMMENT '用户提交答案',
    IS_CORRECT BOOLEAN COMMENT '是否正确（自动批改时填充）',
    BLANK_INDEX INT COMMENT '填空题空序号（当ANSWER_TYPE=FILL_IN_THE_BLANK时有效）',
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    COL_PART ENUM('Part1', 'Part2', 'Part3', 'Part4') COMMENT '题目所属部分'
) COMMENT='用户听力答案明细表（通过RECORD_ID实现批量关联）';

# -- 修改听力题目表（COL_LISTENING_QUESTION），增加字段 COL_PART
# ALTER TABLE COL_LISTENING_QUESTION
#     ADD COLUMN COL_PART ENUM('Part1', 'Part2', 'Part3', 'Part4') COMMENT '题目所属部分' AFTER COL_UPDATED_AT;