package com.apps.mapper;

import com.apps.model.Answer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnswerMapper {

    void insertAnswer(Answer answer);

    Answer selectAnswerById(Long id);

    List<Answer> selectAnswersByQuestionId(Long questionId);

    void updateAnswer(Answer answer);

    void deleteAnswerById(Long id);



}