package com.apps.dto.read2;

import java.sql.Timestamp;
import java.util.List;

public class UserAnswerCorrectDTO {
        private Long detailId;
        private Long recordId;
        private Long userId;
        private Long questionId;
        private String answerType;
        private String submittedAnswer;
        private Boolean isCorrect;
        private Integer blankIndex;
        private Timestamp createdAt;

        private String mergedColumn;

        public String getMergedColumn() {
                return mergedColumn;
        }

        public void setMergedColumn(String mergedColumn) {
                this.mergedColumn = mergedColumn;
        }

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

        public Timestamp getCreatedAt() {
                return createdAt;
        }

        public void setCreatedAt(Timestamp createdAt) {
                this.createdAt = createdAt;
        }


}
