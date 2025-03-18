package com.apps.dto.read;

import java.util.List;

public class ReadingWithQuestionsDTO {
    private Long id;                      // 阅读材料ID
    private String title;                 // 阅读材料标题
    private String content;               // 阅读材料内容
    private String imageBase64;           // 图片的 Base64 编码
    private List<QuestionDTO> questions;  // 题目列表

    // 构造方法
    public ReadingWithQuestionsDTO(Long id, String title, String content, String imageBase64, List<QuestionDTO> questions) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageBase64 = imageBase64;
        this.questions = questions;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }
}