package com.apps.model.speak;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 用户口语答题主记录实体类
 */
public class SpeakUserAnswerRecord {
    private Long recordId;
    private Integer userId;
    private Long speakingId;
    private BigDecimal score;
    private String answerEvaluation;
    private Integer durationSeconds;
    private String deviceType;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String part;

    // Getters and Setters
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

    public Long getSpeakingId() {
        return speakingId;
    }

    public void setSpeakingId(Long speakingId) {
        this.speakingId = speakingId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getAnswerEvaluation() {
        return answerEvaluation;
    }

    public void setAnswerEvaluation(String answerEvaluation) {
        this.answerEvaluation = answerEvaluation;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }
}