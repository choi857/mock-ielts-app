package com.apps.dto.read2;


import java.io.Serializable;

public class AnswerDTO  implements Serializable {
    private Long id; // 答案ID
    private String content; // 答案内容
    private Boolean isCorrect; // 是否为正确答案
    private Integer blankNumber; // 填空题空的序号
    private String matchingKey; // 配对题的匹配键

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public Integer getBlankNumber() {
        return blankNumber;
    }

    public void setBlankNumber(Integer blankNumber) {
        this.blankNumber = blankNumber;
    }

    public String getMatchingKey() {
        return matchingKey;
    }

    public void setMatchingKey(String matchingKey) {
        this.matchingKey = matchingKey;
    }
}