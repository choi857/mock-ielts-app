package com.apps.dto.write;

import java.io.Serializable;

public class UpdateWritingQuestionsRequest  implements Serializable {
    private String Id;
    private String task1Title;
    private String task1Requirements;
    private String task2Title;
    private String task2Requirements;
    private  String colTitle;

    private String taskDescription1;
    private String taskDescription2;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTaskDescription1() {
        return taskDescription1;
    }

    public void setTaskDescription1(String taskDescription1) {
        this.taskDescription1 = taskDescription1;
    }

    public String getTaskDescription2() {
        return taskDescription2;
    }

    public void setTaskDescription2(String taskDescription2) {
        this.taskDescription2 = taskDescription2;
    }

    public String getColTitle() {
        return colTitle;
    }

    public void setColTitle(String colTitle) {
        this.colTitle = colTitle;
    }

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