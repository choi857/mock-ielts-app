package com.apps.dto.speak;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * 用户口语答题DTO类
 */
public class SpeakUserAnswerDTO {
    @JsonProperty("SpeakUserAnswerRecord")
    private SpeakUserAnswerRecord speakUserAnswerRecord;

    @JsonProperty("parts")
    private Map<String, List<AnswerWrapper>> parts;

    // Getters and Setters
    public SpeakUserAnswerRecord getSpeakUserAnswerRecord() {
        return speakUserAnswerRecord;
    }

    public void setSpeakUserAnswerRecord(SpeakUserAnswerRecord speakUserAnswerRecord) {
        this.speakUserAnswerRecord = speakUserAnswerRecord;
    }

    public Map<String, List<AnswerWrapper>> getParts() {
        return parts;
    }

    public void setParts(Map<String, List<AnswerWrapper>> parts) {
        this.parts = parts;
    }

    public static class SpeakUserAnswerRecord {
        @JsonProperty("recordId")
        private Long recordId;

        @JsonProperty("userId")
        private Integer userId;

        @JsonProperty("speakingId")
        private Long speakingId;

        @JsonProperty("score")
        private String score;

        @JsonProperty("answerEvaluation")
        private String answerEvaluation;

        @JsonProperty("durationSeconds")
        private Integer durationSeconds;

        @JsonProperty("deviceType")
        private String deviceType;

        @JsonProperty("createdAt")
        private String createdAt;

        @JsonProperty("updatedAt")
        private String updatedAt;

        @JsonProperty("part")
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

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
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

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getPart() {
            return part;
        }

        public void setPart(String part) {
            this.part = part;
        }
    }

    public static class AnswerWrapper {
        @JsonProperty("answer")
        private SpeakUserAnswerDetail answer;

        // Getters and Setters
        public SpeakUserAnswerDetail getAnswer() {
            return answer;
        }

        public void setAnswer(SpeakUserAnswerDetail answer) {
            this.answer = answer;
        }
    }

    public static class SpeakUserAnswerDetail {
        @JsonProperty("detailId")
        private Long detailId;

        @JsonProperty("recordId")
        private Long recordId;

        @JsonProperty("userId")
        private Integer userId;

        @JsonProperty("questionId")
        private Long questionId;

        @JsonProperty("userAudioUrl")
        private String userAudioUrl;

        @JsonProperty("userTranscript")
        private String userTranscript;

        @JsonProperty("score")
        private String score;

        @JsonProperty("feedback")
        private String feedback;

        @JsonProperty("createdAt")
        private String createdAt;

        @JsonProperty("part")
        private String part;

        @JsonProperty("questionContent")
        private String questionContent;

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

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getPart() {
            return part;
        }

        public void setPart(String part) {
            this.part = part;
        }
    }
}