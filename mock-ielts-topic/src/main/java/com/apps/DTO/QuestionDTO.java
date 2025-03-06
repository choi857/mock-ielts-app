package com.apps.DTO;

import java.util.List;

public class QuestionDTO {
    private Long id;                      // 题目ID
    private String type;                  // 题目类型（SINGLE_CHOICE, FILL_IN_THE_BLANK, MATCHING）
    private String content;               // 题目内容
    private String placeholderFormat;     // 填空题占位符格式
    private List<AnswerDTO> answers;      // 答案列表

    // 构造方法
    public QuestionDTO(Long id, String type, String content, String placeholderFormat, List<AnswerDTO> answers) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.placeholderFormat = placeholderFormat;
        this.answers = answers;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPlaceholderFormat() {
        return placeholderFormat;
    }

    public void setPlaceholderFormat(String placeholderFormat) {
        this.placeholderFormat = placeholderFormat;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }
}