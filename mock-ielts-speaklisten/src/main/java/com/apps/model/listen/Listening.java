package com.apps.model.listen;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class Listening {
    @JsonProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String title;
    private String content;
    private String audioUrl;
    private List<ListeningQuestion> questions;
    private String createdAt;
    private String updatedAt;

    // Getters and Setters
}