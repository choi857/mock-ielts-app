package com.apps.mapper.read;

import com.apps.model.read.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper {

    void insertQuestion(Question question);

    Question selectQuestionById(Long id);

    List<Question> selectQuestionsByReadingId(Long readingId);

    void updateQuestion(Question question);

    void deleteQuestionById(Long id);

    void deleteQuestionsByReadingId(Long readingId);

}