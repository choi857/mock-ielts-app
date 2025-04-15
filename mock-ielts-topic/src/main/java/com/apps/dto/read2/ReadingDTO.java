package com.apps.dto.read2;

import java.io.Serializable;

/**
 * ReadingDTO 表示阅读材料的基本信息
 */
public class ReadingDTO  implements Serializable {
    private Long id; // 阅读材料ID
    private String title; // 阅读材料标题
    private String content; // 阅读材料内容
    private String imageBase64; // 图片的Base64编码

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
}