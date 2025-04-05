package com.apps.model.listen;

import lombok.Data;

import java.util.List;
@Data
public class ListeningQuestion {
    private Long id;
    private Long listeningId;
    private String type;
    private String content;
    private String placeholderFormat;
    private List<ListeningAnswer> answers;
    private String createdAt;
    private String updatedAt;
    private String part;
    private String colImageUrl;
    private Integer serial;
    // Getters and Setters
}