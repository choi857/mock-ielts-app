package com.apps.mapper.read;

import com.apps.model.read.Answer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnswerMapper {

    void insertAnswer(Answer answer);

    Answer selectAnswerById(Long id);

    List<Answer> selectAnswersByQuestionId(Long questionId);

    void updateAnswer(Answer answer);

    void deleteAnswerById(Long id);

    String selectCorrectAnswersByQuestionId(Long questionId);
    String selectmatchingKeyAnswerByQuestionId(Long questionId);

    void deleteAnswersByQuestionId(Long questionId);
}