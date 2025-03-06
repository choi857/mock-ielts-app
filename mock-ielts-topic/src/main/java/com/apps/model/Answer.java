package com.apps.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Answer {
    private Long id;            // 答案ID
    private Long questionId;    // 关联的题目ID
    private String content;     // 答案内容
    private Boolean isCorrect;  // 是否为正确答案
    private Integer blankNumber; // 填空题空的序号
    private String matchingKey; // 配对题的匹配键
    private Date createdAt;     // 创建时间
    private Date updatedAt;     // 更新时间

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}