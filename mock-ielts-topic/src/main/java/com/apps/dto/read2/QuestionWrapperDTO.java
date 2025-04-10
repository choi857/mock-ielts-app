package com.apps.dto.read2;

/**
 * QuestionWrapperDTO 表示题目外层结构
 */
public class QuestionWrapperDTO {
    private QuestionDTO question; // 单个题目的数据传输对象

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }
}
