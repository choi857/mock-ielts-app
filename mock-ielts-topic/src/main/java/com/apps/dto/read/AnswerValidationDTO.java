package com.apps.dto.read;

import java.io.Serializable;

public class AnswerValidationDTO  implements Serializable {
        private Long recordId;
        private Integer userId;
        private Long questionId;
        private String answerType;
        private String submittedAnswer;
        private String correctAnswer;
        private Boolean isCorrect;
        private String answerEvaluation;
        private Long detailId;

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

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public void setCorrectAnswer(String correctAnswer) {
            this.correctAnswer = correctAnswer;
        }

        public Boolean getCorrect() {
            return isCorrect;
        }

        public void setCorrect(Boolean correct) {
            isCorrect = correct;
        }

        public String getAnswerEvaluation() {
            return answerEvaluation;
        }

        public void setAnswerEvaluation(String answerEvaluation) {
            this.answerEvaluation = answerEvaluation;
        }

        public void setIsCorrect(boolean isCorrect) {
        }
    }