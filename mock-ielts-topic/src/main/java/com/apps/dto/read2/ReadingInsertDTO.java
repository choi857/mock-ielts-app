package com.apps.dto.read2;

import java.io.Serializable;
import java.util.List;
/**
 * ReadingInsertDTO 表示用户插入阅读题的顶层数据传输对象
 */

public class ReadingInsertDTO  implements Serializable {
    private ReadingSummaryDTO readingSummary; // Reading summary
    private List<PartDTO> parts; // List of parts, each with its own reading and questions

    // Getters and Setters
    public ReadingSummaryDTO getReadingSummary() {
        return readingSummary;
    }

    public void setReadingSummary(ReadingSummaryDTO readingSummary) {
        this.readingSummary = readingSummary;
    }

    public List<PartDTO> getParts() {
        return parts;
    }

    public void setParts(List<PartDTO> parts) {
        this.parts = parts;
    }
}