package com.apps.model.read;


import com.apps.model.read.Question;
import lombok.Data;

 import java.util.Date;
import java.util.List;


@Data
public class Reading {
    private Long id;            // 阅读材料ID
    private String title;       // 阅读材料标题
    private String content;     // 阅读材料内容
    private String imageBase64; // 图片的 Base64 编码
    private Date createdAt;     // 创建时间
    private Date updatedAt;     // 更新时间

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
    private List<Question> questions;     // 题目列表,需要新增
    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }


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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
// Getters and Setters
}