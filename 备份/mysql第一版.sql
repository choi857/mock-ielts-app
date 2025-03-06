-- 创建用户表（Users）
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

-- 创建题目类型表（QuestionTypes）
    CREATE TABLE COL_QuestionTypes (
                               question_type_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '题目类型ID',
                               name VARCHAR(255) NOT NULL COMMENT '题目类型名称',
                               deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
) COMMENT='题目类型表';

-- 创建听力题目表（ListeningQuestions）
CREATE TABLE COL_ListeningQuestions (
                                    listening_question_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '听力题目ID',
                                    question_type_id INT COMMENT '题目类型ID',
                                    content TEXT NOT NULL COMMENT '题目内容',
                                    correct_answer_id INT COMMENT '正确答案ID',
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
) COMMENT='听力题目表';

-- 创建阅读题目表（ReadingQuestions）
CREATE TABLE COL_ReadingQuestions (
                                  reading_question_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '阅读题目ID',
                                  question_type_id INT COMMENT '题目类型ID',
                                  content TEXT NOT NULL COMMENT '题目内容',
                                  correct_answer_id INT COMMENT '正确答案ID',
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
) COMMENT='阅读题目表';

-- 创建写作题目表（WritingQuestions）
CREATE TABLE COL_WritingQuestions (
                                  writing_question_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '写作题目ID',
                                  question_type_id INT COMMENT '题目类型ID',
                                  content TEXT NOT NULL COMMENT '题目内容',
                                  correct_answer_id INT COMMENT '正确答案ID',
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
) COMMENT='写作题目表';

-- 创建口语题目表（SpeakingQuestions）
CREATE TABLE COL_SpeakingQuestions (
                                   speaking_question_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '口语题目ID',
                                   question_type_id INT COMMENT '题目类型ID',
                                   content TEXT NOT NULL COMMENT '题目内容',
                                   correct_answer_id INT COMMENT '正确答案ID',
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
) COMMENT='口语题目表';

-- 创建正确答案表（CorrectAnswers）
CREATE TABLE COL_CorrectAnswers (
                                correct_answer_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '正确答案ID',
                                answer TEXT NOT NULL COMMENT '答案内容',
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
) COMMENT='正确答案表';

-- 创建用户答案表（UserAnswers）
CREATE TABLE COL_UserAnswers (
                             user_answer_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户答案ID',
                             user_id INT COMMENT '用户ID',
                             question_id INT COMMENT '题目ID',
                             answer TEXT COMMENT '用户答案',
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
) COMMENT='用户答案表';

-- 创建练习设置表（PracticeSettings）
CREATE TABLE COL_PracticeSettings (
                                  practice_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '练习设置ID',
                                  user_id INT COMMENT '用户ID',
                                  question_type_id INT COMMENT '题目类型ID',
                                  difficulty INT COMMENT '难度',
                                  time_limit INT COMMENT '时间限制',
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
) COMMENT='练习设置表';

-- 创建考试环境设置表（ExamEnvironmentSettings）
CREATE TABLE COL_ExamEnvironmentSettings (
                                         environment_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '环境设置ID',
                                         interface_design TEXT COMMENT '界面设计',
                                         operation_flow TEXT COMMENT '操作流程',
                                         time_management INT COMMENT '时间管理',
                                         deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
) COMMENT='考试环境设置表';

-- 创建自动评分规则表（AutoScoringRules）
CREATE TABLE COL_AutoScoringRules (
                                  scoring_rule_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '评分规则ID',
                                  criteria TEXT NOT NULL COMMENT '评分标准',
                                  accuracy FLOAT NOT NULL COMMENT '准确度',
                                  deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
) COMMENT='自动评分规则表';

-- 创建题库管理表（QuestionBankManagement）
CREATE TABLE COL_QuestionBankManagement (
                                        management_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '管理ID',
                                        question_id INT COMMENT '题目ID',
                                        status VARCHAR(255) COMMENT '状态',
                                        last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
                                        deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除'
)

-- 暂时不设置外键，以下是设置外键的SQL代码----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- 设置外键约束
-- ListeningQuestions 表的 question_type_id 作为 QuestionTypes 表的外键
# ALTER TABLE ListeningQuestions
#     ADD CONSTRAINT fk_listening_question_type FOREIGN KEY (question_type_id) REFERENCES QuestionTypes(question_type_id);
#
# -- ListeningQuestions 表的 correct_answer_id 作为 CorrectAnswers 表的外键
# ALTER TABLE ListeningQuestions
#     ADD CONSTRAINT fk_listening_correct_answer FOREIGN KEY (correct_answer_id) REFERENCES CorrectAnswers(correct_answer_id);
#
# -- ReadingQuestions 表的 question_type_id 作为 QuestionTypes 表的外键
# ALTER TABLE ReadingQuestions
#     ADD CONSTRAINT fk_reading_question_type FOREIGN KEY (question_type_id) REFERENCES QuestionTypes(question_type_id);
#
# -- ReadingQuestions 表的 correct_answer_id 作为 CorrectAnswers 表的外键
# ALTER TABLE ReadingQuestions
#     ADD CONSTRAINT fk_reading_correct_answer FOREIGN KEY (correct_answer_id) REFERENCES CorrectAnswers(correct_answer_id);
#
# -- WritingQuestions 表的 question_type_id 作为 QuestionTypes 表的外键
# ALTER TABLE WritingQuestions
#     ADD CONSTRAINT fk_writing_question_type FOREIGN KEY (question_type_id) REFERENCES QuestionTypes(question_type_id);
#
# -- WritingQuestions 表的 correct_answer_id 作为 CorrectAnswers 表的外键
# ALTER TABLE WritingQuestions
#     ADD CONSTRAINT fk_writing_correct_answer FOREIGN KEY (correct_answer_id) REFERENCES CorrectAnswers(correct_answer_id);
#
# -- SpeakingQuestions 表的 question_type_id 作为 QuestionTypes 表的外键
# ALTER TABLE SpeakingQuestions
#     ADD CONSTRAINT fk_speaking_question_type FOREIGN KEY (question_type_id) REFERENCES QuestionTypes(question_type_id);
#
# -- SpeakingQuestions 表的 correct_answer_id 作为 CorrectAnswers 表的外键
# ALTER TABLE SpeakingQuestions
#     ADD CONSTRAINT fk_speaking_correct_answer FOREIGN KEY (correct_answer_id) REFERENCES CorrectAnswers(correct_answer_id);
#
# -- UserAnswers 表的 user_id 作为 Users 表的外键
# ALTER TABLE UserAnswers
#     ADD CONSTRAINT fk_user_answer_user FOREIGN KEY (user_id) REFERENCES Users(user_id);
#
# -- UserAnswers 表的 question_id 作为 ListeningQuestions 表的外键（假设 question_id 与 ListeningQuestions 表关联）
# ALTER TABLE UserAnswers
#     ADD CONSTRAINT fk_user_answer_listening_question FOREIGN KEY (question_id) REFERENCES ListeningQuestions(listening_question_id);
#
# -- PracticeSettings 表的 user_id 作为 Users 表的外键
# ALTER TABLE PracticeSettings
#     ADD CONSTRAINT fk_practice_user FOREIGN KEY (user_id) REFERENCES Users(user_id);
#
# -- PracticeSettings 表的 question_type_id 作为 QuestionTypes 表的外键
# ALTER TABLE PracticeSettings
#     ADD CONSTRAINT fk_practice_question_type FOREIGN KEY (question_type_id) REFERENCES QuestionTypes(question_type_id);
#
# -- QuestionBankManagement 表的 question_id 作为 ListeningQuestions 表的外键（假设 question_id 与 ListeningQuestions 表关联）
# ALTER TABLE QuestionBankManagement
#     ADD CONSTRAINT fk_question_bank_listening_question FOREIGN KEY (question_id) REFERENCES ListeningQuestions(listening_question_id);