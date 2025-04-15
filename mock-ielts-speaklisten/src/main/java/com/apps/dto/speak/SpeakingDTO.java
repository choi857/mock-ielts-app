package com.apps.dto.speak;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 口语材料DTO类，新增口语题的dto
 */
public class SpeakingDTO  implements Serializable {
    @JsonProperty("speaking")
    private Speaking speaking;

    @JsonProperty("parts")
    private Map<String, List<QuestionWrapper>> parts;

    // Getters and Setters
    public Speaking getSpeaking() {
        return speaking;
    }

    public void setSpeaking(Speaking speaking) {
        this.speaking = speaking;
    }

    public Map<String, List<QuestionWrapper>> getParts() {
        return parts;
    }

    public void setParts(Map<String, List<QuestionWrapper>> parts) {
        this.parts = parts;
    }

    public static class Speaking {
        @JsonProperty("id")
        private String id;

        @JsonProperty("title")
        private String title;

        @JsonProperty("imageUrl")
        private String imageUrl;

        @JsonProperty("transcript")
        private String transcript;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getTranscript() {
            return transcript;
        }

        public void setTranscript(String transcript) {
            this.transcript = transcript;
        }
    }

    public static class QuestionWrapper {
        @JsonProperty("question")
        private SpeakingQuestionDTO question;

        // Getters and Setters
        public SpeakingQuestionDTO getQuestion() {
            return question;
        }

        public void setQuestion(SpeakingQuestionDTO question) {
            this.question = question;
        }
    }
}