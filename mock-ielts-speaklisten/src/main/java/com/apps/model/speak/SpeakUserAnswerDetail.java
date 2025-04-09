package com.apps.model.speak;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 用户口语答案明细实体类
 */
public class SpeakUserAnswerDetail {
    private Long detailId;
    private Long recordId;
    private Integer userId;
    private Long questionId;
    private String userAudioUrl;
    private String userTranscript;
    private BigDecimal score;
    private String feedback;
    private Timestamp createdAt;
    private String part;
    private String questionContent;
    private String userAudioUrlToAi;

    public String getUserAudioUrltoAi() {
        return userAudioUrlToAi;
    }

    public void setUserAudioUrltoAi(String userAudioUrltoAi) {
        this.userAudioUrlToAi = userAudioUrltoAi;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    // Getters and Setters
    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getUserAudioUrl() {
        return userAudioUrl;
    }

    public void setUserAudioUrl(String userAudioUrl) {
        this.userAudioUrl = userAudioUrl;
    }

    public String getUserTranscript() {
        return userTranscript;
    }

    public void setUserTranscript(String userTranscript) {
        this.userTranscript = userTranscript;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }
}