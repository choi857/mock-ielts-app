package com.apps.dto.listen;

import com.apps.model.listen.Listening;
import com.apps.model.listen.ListeningAnswer;
import com.apps.model.listen.ListeningQuestion;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ListeningWithQuestionsAndAnswersDTO   implements Serializable {
    @JsonProperty("listening")
    private Listening listening;

    @JsonProperty("parts")
    private Map<String, List<QuestionWithAnswers>> parts;

    public Listening getListening() {
        return listening;
    }

    public void setListening(Listening listening) {
        this.listening = listening;
    }

    public Map<String, List<QuestionWithAnswers>> getParts() {
        return parts;
    }

    public void setParts(Map<String, List<QuestionWithAnswers>> parts) {
        this.parts = parts;
    }

    public static class QuestionWithAnswers implements Serializable{
        @JsonProperty("question")
        private ListeningQuestion question;

        @JsonProperty("answers")
        private List<ListeningAnswer> answers;

        public ListeningQuestion getQuestion() {
            return question;
        }

        public void setQuestion(ListeningQuestion question) {
            this.question = question;
        }

        public List<ListeningAnswer> getAnswers() {
            return answers;
        }

        public void setAnswers(List<ListeningAnswer> answers) {
            this.answers = answers;
        }
    }
}