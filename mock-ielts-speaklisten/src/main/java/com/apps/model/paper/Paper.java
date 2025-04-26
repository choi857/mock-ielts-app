package com.apps.model.paper;

import java.sql.Timestamp;

public class Paper {
    private Long paperId;
    private String paperName;
    private String paperDescription;
    private Long readingSummaryId;
    private Long listenId;
    private Long writeId;
    private Long speakId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getPaperDescription() {
        return paperDescription;
    }

    public void setPaperDescription(String paperDescription) {
        this.paperDescription = paperDescription;
    }

    public Long getReadingSummaryId() {
        return readingSummaryId;
    }

    public void setReadingSummaryId(Long readingSummaryId) {
        this.readingSummaryId = readingSummaryId;
    }

    public Long getListenId() {
        return listenId;
    }

    public void setListenId(Long listenId) {
        this.listenId = listenId;
    }

    public Long getWriteId() {
        return writeId;
    }

    public void setWriteId(Long writeId) {
        this.writeId = writeId;
    }

    public Long getSpeakId() {
        return speakId;
    }

    public void setSpeakId(Long speakId) {
        this.speakId = speakId;
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
// Getters and Setters
}
