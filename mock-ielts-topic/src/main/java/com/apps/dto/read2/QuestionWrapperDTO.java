package com.apps.dto.read2;

import java.io.Serializable;

/**
 * QuestionWrapperDTO 表示题目外层结构
 */
public class QuestionWrapperDTO  implements Serializable {
    private QuestionDTO question; // 单个题目的数据传输对象

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }
}
