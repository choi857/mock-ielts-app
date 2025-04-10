package com.apps.dto.read2;

import java.util.List;

/**
 * QuestionDTO 表示单个题目的数据传输对象
 */
public class QuestionDTO {
    private Long id; // 题目ID
    private String type; // 题目类型（如：单选题、填空题、配对题等）
    private String content; // 题目内容
    private String placeholderFormat; // 填空题的占位符格式
    private String part; // 题目所属部分
    private String serial; // 题目序号
    private List<AnswerDTO> answers; // 题目的答案列表

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

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }
}