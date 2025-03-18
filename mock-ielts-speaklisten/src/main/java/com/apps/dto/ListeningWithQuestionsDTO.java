package com.apps.dto;

import com.apps.model.listen.Listening;
import com.apps.model.listen.ListeningQuestion;

import java.util.List;
import java.util.Map;

public class ListeningWithQuestionsDTO {
    private Listening listening;
    private Map<String, List<ListeningQuestion>> parts;

    // Getters and Setters
    public Listening getListening() {
        return listening;
    }

    public void setListening(Listening listening) {
        this.listening = listening;
    }

    public Map<String, List<ListeningQuestion>> getParts() {
        return parts;
    }

    public void setParts(Map<String, List<ListeningQuestion>> parts) {
        this.parts = parts;
    }
}