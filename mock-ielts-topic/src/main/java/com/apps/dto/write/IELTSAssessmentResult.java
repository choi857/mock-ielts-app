package com.apps.dto.write;


public class IELTSAssessmentResult {
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
