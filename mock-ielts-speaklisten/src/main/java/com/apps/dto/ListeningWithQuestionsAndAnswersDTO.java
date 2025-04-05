package com.apps.dto;

import com.apps.model.listen.Listening;
import com.apps.model.listen.ListeningAnswer;
import com.apps.model.listen.ListeningQuestion;

import java.util.List;
import java.util.Map;

/**
 * 查询听力题目答案和题目的dto
 */
public class ListeningWithQuestionsAndAnswersDTO {
    private Listening listening;
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

    public static class QuestionWithAnswers {
        private ListeningQuestion question;
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