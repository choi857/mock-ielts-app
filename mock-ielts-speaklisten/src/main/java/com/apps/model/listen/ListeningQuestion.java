package com.apps.model.listen;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class ListeningQuestion  implements Serializable {
    @JsonProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @JsonProperty("listeningId")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
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