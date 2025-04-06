package com.apps.mapper.listen;

import com.apps.model.listen.ListeningAnswer;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ListeningAnswerMapper {
   // void insertAnswer(ListeningAnswer answer);

    @Insert("INSERT INTO COL_LISTENING_ANSWER (COL_ID,COL_QUESTION_ID, COL_CONTENT, COL_IS_CORRECT, COL_BLANK_NUMBER, COL_MATCHING_KEY, COL_CREATED_AT, COL_UPDATED_AT) VALUES (#{id},#{questionId}, #{content}, #{isCorrect}, #{blankNumber}, #{matchingKey}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
    void insertAnswer(ListeningAnswer answer);


    @Select("SELECT * FROM COL_LISTENING_ANSWER WHERE COL_QUESTION_ID = #{questionId}")
    @Results({
            @Result(property = "id", column = "COL_ID"),
            @Result(property = "questionId", column = "COL_QUESTION_ID"),
            @Result(property = "content", column = "COL_CONTENT"),
            @Result(property = "isCorrect", column = "COL_IS_CORRECT"),
            @Result(property = "blankNumber", column = "COL_BLANK_NUMBER"),
            @Result(property = "matchingKey", column = "COL_MATCHING_KEY"),
            @Result(property = "createdAt", column = "COL_CREATED_AT"),
            @Result(property = "updatedAt", column = "COL_UPDATED_AT")
    })
    List<ListeningAnswer> findAnswersByQuestionId(Long questionId);


    @Update("UPDATE COL_LISTENING_ANSWER SET COL_CONTENT = #{content}, COL_IS_CORRECT = #{isCorrect}, COL_BLANK_NUMBER = #{blankNumber}, COL_MATCHING_KEY = #{matchingKey}, COL_UPDATED_AT = CURRENT_TIMESTAMP WHERE COL_ID = #{id}")
    void updateAnswer(ListeningAnswer answer);


//    @Select("SELECT * FROM COL_LISTENING_ANSWER WHERE COL_QUESTION_ID = #{questionId} AND COL_IS_CORRECT = TRUE")
//    ListeningAnswer findCorrectAnswerByQuestionId(Long questionId);
@Select("SELECT * FROM COL_LISTENING_ANSWER WHERE COL_QUESTION_ID = #{questionId} AND COL_IS_CORRECT = TRUE")
@Results({
        @Result(property = "id", column = "COL_ID"),
        @Result(property = "questionId", column = "COL_QUESTION_ID"),
        @Result(property = "content", column = "COL_CONTENT"),
        @Result(property = "isCorrect", column = "COL_IS_CORRECT"),
        @Result(property = "blankNumber", column = "COL_BLANK_NUMBER"),
        @Result(property = "matchingKey", column = "COL_MATCHING_KEY"),
        @Result(property = "createdAt", column = "COL_CREATED_AT"),
        @Result(property = "updatedAt", column = "COL_UPDATED_AT"),
        @Result(property = "part", column = "COL_PART")
})
ListeningAnswer findCorrectAnswerByQuestionId(Long questionId);
}