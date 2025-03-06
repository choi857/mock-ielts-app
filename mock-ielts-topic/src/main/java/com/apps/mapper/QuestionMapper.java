package com.apps.mapper;

import com.apps.model.Question;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionMapper {

    void insertQuestion(Question question);

    Question selectQuestionById(Long id);

    List<Question> selectQuestionsByReadingId(Long readingId);

    void updateQuestion(Question question);

    void deleteQuestionById(Long id);



}