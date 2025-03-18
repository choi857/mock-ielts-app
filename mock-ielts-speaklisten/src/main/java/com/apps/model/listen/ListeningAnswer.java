package com.apps.model.listen;

import lombok.Data;

@Data
public class ListeningAnswer {
    private Long id;
    private Long questionId;
    private String content;
    private Boolean isCorrect;
    private Integer blankNumber;
    private String matchingKey;
    private String createdAt;
    private String updatedAt;
    private String part;

    // Getters and Setters
}