package com.apps.dto.read2;

import java.io.Serializable;
import java.util.List;

public class PartDTO  implements Serializable {
    private ReadingDTO reading; // Each part has its own reading
    private List<QuestionWrapperDTO> questions; // Questions associated with this part

    // Getters and Setters
    public ReadingDTO getReading() {
        return reading;
    }

    public void setReading(ReadingDTO reading) {
        this.reading = reading;
    }

    public List<QuestionWrapperDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionWrapperDTO> questions) {
        this.questions = questions;
    }
}