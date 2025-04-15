package com.apps.model.listen;

import lombok.Data;

import java.io.Serializable;

@Data
public class ListeningUserAnswerRecord  implements Serializable {
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