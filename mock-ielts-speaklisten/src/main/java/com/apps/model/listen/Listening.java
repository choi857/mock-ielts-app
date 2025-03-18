package com.apps.model.listen;

import lombok.Data;

import java.util.List;
@Data
public class Listening {
    private Long id;
    private String title;
    private String content;
    private String audioUrl;
    private List<ListeningQuestion> questions;
    private String createdAt;
    private String updatedAt;

    // Getters and Setters
}