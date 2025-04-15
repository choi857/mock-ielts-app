package com.apps.dto.write;


import java.io.Serializable;

public class IELTSAssessmentResult   implements Serializable {
    private double score;
    private String answerEvaluation;

    public IELTSAssessmentResult(double score, String answerEvaluation) {
        this.score = score;
        this.answerEvaluation = answerEvaluation;
    }

    public double getScore() {
        return score;
    }

    public String getAnswerEvaluation() {
        return answerEvaluation;
    }
}
