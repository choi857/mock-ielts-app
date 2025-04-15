package com.apps.model.write;

import java.io.Serializable;
import java.sql.Timestamp;

public class WritingAnswerRecord  implements Serializable {
    private Long recordId;
    private Long userId;
    private Long task1Id;
    private Long task2Id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Double totalScore;

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
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

    public Long getTask1Id() {
        return task1Id;
    }

    public void setTask1Id(Long task1Id) {
        this.task1Id = task1Id;
    }

    public Long getTask2Id() {
        return task2Id;
    }

    public void setTask2Id(Long task2Id) {
        this.task2Id = task2Id;
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