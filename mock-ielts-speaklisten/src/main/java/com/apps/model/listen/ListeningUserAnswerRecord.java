package com.apps.model.listen;

import lombok.Data;

@Data
public class ListeningUserAnswerRecord {
    private Long id;
    private String userId;
    private Long listeningId;
    private String createdAt;
    private String updatedAt;
    private double score;
    private String answerEvaluation;
    private Integer durationSeconds;
    private String deviceType;
}