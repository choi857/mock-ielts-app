package com.apps.model.listen;

import lombok.Data;

@Data
public class ListeningUserAnswerDetail {
    private Long id;
    private Long recordId;
    private String userId;
    private Long questionId;
    private String answerType;
    private String submittedAnswer;
    private String createdAt;
    private String part;
    private boolean iscorrect;

    public Boolean getIsCorrect() {
        return iscorrect;
    }
    public void setIsCorrect(Boolean iscorrect) {
        this.iscorrect = iscorrect;
    }
}