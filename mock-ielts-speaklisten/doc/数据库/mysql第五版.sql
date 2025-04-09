CREATE TABLE COL_SPEAKING (
                              COL_ID BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '口语材料ID',
                              COL_TITLE VARCHAR(255) COMMENT '口语材料标题',
                              COL_AUDIO_URL VARCHAR(255) COMMENT '口语材料音频URL',
                              COL_TRANSCRIPT TEXT COMMENT '口语材料转文字文本',
                              COL_CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              COL_UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='口语材料表';

CREATE TABLE COL_SPEAKING_QUESTION (
                                       COL_ID BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '题目ID',
                                       COL_SPEAKING_ID BIGINT COMMENT '关联的口语材料ID', -- 关联COL_SPEAKING(COL_ID)
                                       COL_TYPE ENUM('CUE_CARD', 'DISCUSSION', 'INTRODUCTION') COMMENT '题目类型',
                                       COL_CONTENT TEXT COMMENT '题目内容',
                                       COL_IMAGE_URL VARCHAR(255) NULL COMMENT '题目图片URL',
                                       COL_CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       COL_UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       COL_PART ENUM('Part1', 'Part2', 'Part3') COMMENT '题目所属部分',
                                       COL_SERIAL INT DEFAULT 0 COMMENT '题目序号'
) COMMENT='口语题目表';

CREATE TABLE COL_SPEAK_USER_ANSWER_RECORD (
                                              RECORD_ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '唯一答题记录ID',
                                              USER_ID INT COMMENT '关联COL_Users.user_id', -- 关联COL_Users(user_id)
                                              SPEAKING_ID BIGINT COMMENT '关联口语材料ID', -- 关联COL_SPEAKING(COL_ID)
                                              SCORE DECIMAL(5,2) COMMENT '本次答题得分',
                                              ANSWER_EVALUATION TEXT COMMENT '答题评价',
                                              DURATION_SECONDS INT COMMENT '答题耗时（秒）',
                                              DEVICE_TYPE ENUM('WEB','MOBILE','TABLET') COMMENT '答题设备类型',
                                              CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '答题开始时间',
                                              UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
                                              COL_PART ENUM('Part1', 'Part2', 'Part3') COMMENT '题目所属部分'
) COMMENT='用户口语答题主记录表（1条记录对应多个答案明细）';

CREATE TABLE COL_SPEAK_USER_ANSWER_DETAIL (
                                              DETAIL_ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '答案明细ID',
                                              RECORD_ID BIGINT COMMENT '关联COL_SPEAK_USER_ANSWER_RECORD.RECORD_ID', -- 关联用户口语答题记录主表ID
                                              USER_ID INT COMMENT '关联COL_Users.user_id', -- 关联COL_Users(user_id)
                                              QUESTION_ID BIGINT COMMENT '关联COL_SPEAKING_QUESTION.COL_ID', -- 关联COL_SPEAKING_QUESTION(COL_ID)
                                              USER_AUDIO_URL VARCHAR(255) COMMENT '用户回答的音频URL',
                                              USER_TRANSCRIPT TEXT COMMENT '用户回答的文本转录',
                                              SCORE DECIMAL(5,2) COMMENT '用户回答的得分',
                                              FEEDBACK TEXT COMMENT '用户回答的评价和反馈',
                                              CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
                                              COL_PART ENUM('Part1', 'Part2', 'Part3') COMMENT '题目所属部分'
) COMMENT='用户口语答案明细表（通过RECORD_ID实现批量关联）';

