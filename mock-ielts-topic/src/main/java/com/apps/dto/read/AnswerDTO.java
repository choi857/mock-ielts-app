package com.apps.dto.read;

public class AnswerDTO {
    private Long id;                      // 答案ID
    private String content;               // 答案内容
    private Boolean isCorrect;            // 是否为正确答案
    private Integer blankNumber;          // 填空题空的序号
    private String matchingKey;           // 配对题的匹配键

    // 构造方法
    public AnswerDTO(Long id, String content, Boolean isCorrect, Integer blankNumber, String matchingKey) {
        this.id = id;
        this.content = content;
        this.isCorrect = isCorrect;
        this.blankNumber = blankNumber;
        this.matchingKey = matchingKey;
    }

    // Getters and Setters
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

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
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