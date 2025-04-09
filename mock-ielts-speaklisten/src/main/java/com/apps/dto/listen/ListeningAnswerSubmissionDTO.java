package com.apps.dto.listen;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.List;

public class ListeningAnswerSubmissionDTO {
    @JsonProperty("listening")
    private ListeningDTO listening;

    @JsonProperty("parts")
    private Map<String, List<QuestionWrapperDTO>> parts;

    public ListeningDTO getListening() {
        return listening;
    }

    public void setListening(ListeningDTO listening) {
        this.listening = listening;
    }

    public Map<String, List<QuestionWrapperDTO>> getParts() {
        return parts;
    }

    public void setParts(Map<String, List<QuestionWrapperDTO>> parts) {
        this.parts = parts;
    }

    public static class QuestionWrapperDTO {
        @JsonProperty("question")
        private QuestionDTO question;

        public QuestionDTO getQuestion() {
            return question;
        }

        public void setQuestion(QuestionDTO question) {
            this.question = question;
        }
    }

    public static class ListeningDTO {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("title")
        private String title;

        @JsonProperty("content")
        private String content;

        @JsonProperty("audioUrl")
        private String audioUrl;

        @JsonProperty("createdAt")
        private String createdAt;

        @JsonProperty("updatedAt")
        private String updatedAt;

        @JsonProperty("username")
        private String username;

        @JsonProperty("userId")
        private String userId;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAudioUrl() {
            return audioUrl;
        }

        public void setAudioUrl(String audioUrl) {
            this.audioUrl = audioUrl;
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    public static class QuestionDTO {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("listeningId")
        private Long listeningId;

        @JsonProperty("type")
        private String type;

        @JsonProperty("content")
        private String content;

        @JsonProperty("placeholderFormat")
        private String placeholderFormat;

        @JsonProperty("answers")
        private String answers;

        @JsonProperty("createdAt")
        private String createdAt;

        @JsonProperty("updatedAt")
        private String updatedAt;

        @JsonProperty("part")
        private String part;

        @JsonProperty("colImageUrl")
        private String colImageUrl;

        @JsonProperty("serial")
        private Integer serial;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getListeningId() {
            return listeningId;
        }

        public void setListeningId(Long listeningId) {
            this.listeningId = listeningId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPlaceholderFormat() {
            return placeholderFormat;
        }

        public void setPlaceholderFormat(String placeholderFormat) {
            this.placeholderFormat = placeholderFormat;
        }

        public String getAnswers() {
            return answers;
        }

        public void setAnswers(String answers) {
            this.answers = answers;
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

        public String getColImageUrl() {
            return colImageUrl;
        }

        public void setColImageUrl(String colImageUrl) {
            this.colImageUrl = colImageUrl;
        }

        public Integer getSerial() {
            return serial;
        }

        public void setSerial(Integer serial) {
            this.serial = serial;
        }
    }
}