package com.apps.dto.write;

public class WritingQuestionsRequest {
    private String task1Title;
    private String task1Requirements;
    private String task2Title;
    private String task2Requirements;

    // Getters and Setters
    public String getTask1Title() {
        return task1Title;
    }

    public void setTask1Title(String task1Title) {
        this.task1Title = task1Title;
    }

    public String getTask1Requirements() {
        return task1Requirements;
    }

    public void setTask1Requirements(String task1Requirements) {
        this.task1Requirements = task1Requirements;
    }

    public String getTask2Title() {
        return task2Title;
    }

    public void setTask2Title(String task2Title) {
        this.task2Title = task2Title;
    }

    public String getTask2Requirements() {
        return task2Requirements;
    }

    public void setTask2Requirements(String task2Requirements) {
        this.task2Requirements = task2Requirements;
    }
}