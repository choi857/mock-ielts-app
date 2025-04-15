package com.apps.dto.speak;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 口语材料部分DTO类
 */
public class PartDTO implements Serializable {
    @JsonProperty("part")
    private String part;

    @JsonProperty("questions")
    private List<SpeakingQuestionDTO> questions;

    // Getters and Setters
    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public List<SpeakingQuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<SpeakingQuestionDTO> questions) {
        this.questions = questions;
    }
}