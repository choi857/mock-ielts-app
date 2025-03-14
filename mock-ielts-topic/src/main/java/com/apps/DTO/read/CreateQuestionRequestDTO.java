package com.apps.DTO.read;

import com.apps.DTO.read.AnswerDTO;
import lombok.Data;

import java.util.List;
@Data
public class CreateQuestionRequestDTO {


    private Long readingId;                  // 关联的阅读材料ID
    private String type;                     // 题目类型（SINGLE_CHOICE, FILL_IN_THE_BLANK, MATCHING）
    private String content;                  // 题目内容
    private String placeholderFormat;        // 填空题占位符格式
    private List<AnswerDTO> answers;         // 题目答案列表

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

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }


    // Getters and Setters
}

