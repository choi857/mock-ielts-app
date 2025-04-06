package com.apps.model.listen;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ListeningAnswer {
    @JsonProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @JsonProperty("questionId")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
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