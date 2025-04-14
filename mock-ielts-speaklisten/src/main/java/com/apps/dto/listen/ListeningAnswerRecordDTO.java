package com.apps.dto.listen;

import java.sql.Timestamp;

public class ListeningAnswerRecordDTO {
    private Long recordId;
    private Long userId;
    private Long listeningId;
    private Double score;
    private Integer durationSeconds;
    private String deviceType;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String answerEvaluation;
    private String part;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    // Getters and Setters

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getListeningId() {
        return listeningId;
    }

    public void setListeningId(Long listeningId) {
        this.listeningId = listeningId;
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

    public String getAnswerEvaluation() {
        return answerEvaluation;
    }

    public void setAnswerEvaluation(String answerEvaluation) {
        this.answerEvaluation = answerEvaluation;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }
}
