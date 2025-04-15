package com.apps.model.read;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserAnswerRecord  implements Serializable {
    private Long recordId;
    private Integer userId;
    private Long readingId;
    private Double score;
    private Integer durationSeconds;
    private String deviceType;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String answerEvaluation;
    private Long readSummaryId; //阅读题总表id

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getReadSummaryId() {
        return readSummaryId;
    }

    public void setReadSummaryId(Long readSummaryId) {
        this.readSummaryId = readSummaryId;
    }
    // Getters 和 Setters

    public String getAnswerEvaluation() {
        return answerEvaluation;
    }

    public void setAnswerEvaluation(String answerEvaluation) {
        this.answerEvaluation = answerEvaluation;
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

    public Long getReadingId() {
        return readingId;
    }

    public void setReadingId(Long readingId) {
        this.readingId = readingId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
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
}