package com.apps.dto.listen;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ListeningUserAnswerCorrectDetail  implements Serializable {


        @JsonProperty("id")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long id;

        @JsonProperty("recordId")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long recordId;

        @JsonProperty("userId")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long userId;

        @JsonProperty("questionId")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long questionId;

        private String answerType;

        private String submittedAnswer;

        private Boolean isCorrect;

        private Integer blankIndex;

        private String createdAt;

        private String part;

        private String answerEvaluation;

        private String mergedColumn;
        private Double score;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getMergedColumn() {
        return mergedColumn;
    }

    public void setMergedColumn(String mergedColumn) {
        this.mergedColumn = mergedColumn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public String getSubmittedAnswer() {
        return submittedAnswer;
    }

    public void setSubmittedAnswer(String submittedAnswer) {
        this.submittedAnswer = submittedAnswer;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public Integer getBlankIndex() {
        return blankIndex;
    }

    public void setBlankIndex(Integer blankIndex) {
        this.blankIndex = blankIndex;
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

    public String getAnswerEvaluation() {
        return answerEvaluation;
    }

    public void setAnswerEvaluation(String answerEvaluation) {
        this.answerEvaluation = answerEvaluation;
    }
}
