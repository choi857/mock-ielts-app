package com.apps.model.read;



import com.apps.model.read.Answer;

import java.util.Date;
import java.util.List;

public class Question {
    private Long id;            // 题目ID
    private Long readingId;     // 关联的阅读材料ID
    private String type;        // 题目类型（SINGLE_CHOICE, FILL_IN_THE_BLANK, MATCHING）
    private String content;     // 题目内容
    private String placeholderFormat; // 填空题占位符格式
    private Date createdAt;     // 创建时间
    private Date updatedAt;     // 更新时间

    private List<Answer> answers;         // 答案列表,需要新增
    private String part;    //所属部分
    private String serial; //题目序号

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReadingId() {
        return readingId;
    }

    public void setReadingId(Long readingId) {
        this.readingId = readingId;
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
