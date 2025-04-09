package com.apps.mapper.speak;

import com.apps.model.speak.SpeakingQuestion;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 口语题目Mapper接口
 */
@Mapper
public interface SpeakingQuestionMapper {

    @Insert("INSERT INTO COL_SPEAKING_QUESTION (COL_SPEAKING_ID, COL_TYPE, COL_CONTENT, COL_IMAGE_URL, COL_PART, COL_SERIAL,COL_ID) VALUES (#{speakingId}, #{type}, #{content}, #{imageUrl}, #{part}, #{serial}, #{id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertSpeakingQuestion(SpeakingQuestion speakingQuestion);

    @Update("UPDATE COL_SPEAKING_QUESTION SET COL_SPEAKING_ID = #{speakingId}, COL_TYPE = #{type}, COL_CONTENT = #{content}, COL_IMAGE_URL = #{imageUrl}, COL_PART = #{part}, COL_SERIAL = #{serial} WHERE COL_ID = #{id}")
    void updateSpeakingQuestion(SpeakingQuestion speakingQuestion);

    @Delete("DELETE FROM COL_SPEAKING_QUESTION WHERE COL_ID = #{id}")
    void deleteSpeakingQuestion(Long id);

    @Select("SELECT * FROM COL_SPEAKING_QUESTION WHERE COL_ID = #{id}")
    @Results({
            @Result(property = "id", column = "COL_ID"),
            @Result(property = "speakingId", column = "COL_SPEAKING_ID"),
            @Result(property = "type", column = "COL_TYPE"),
            @Result(property = "content", column = "COL_CONTENT"),
            @Result(property = "imageUrl", column = "COL_IMAGE_URL"),
            @Result(property = "createdAt", column = "COL_CREATED_AT"),
            @Result(property = "updatedAt", column = "COL_UPDATED_AT"),
            @Result(property = "part", column = "COL_PART"),
            @Result(property = "serial", column = "COL_SERIAL")
    })
    SpeakingQuestion findSpeakingQuestionById(Long id);

    @Select("SELECT * FROM COL_SPEAKING_QUESTION")
    @Results({
            @Result(property = "id", column = "COL_ID"),
            @Result(property = "speakingId", column = "COL_SPEAKING_ID"),
            @Result(property = "type", column = "COL_TYPE"),
            @Result(property = "content", column = "COL_CONTENT"),
            @Result(property = "imageUrl", column = "COL_IMAGE_URL"),
            @Result(property = "createdAt", column = "COL_CREATED_AT"),
            @Result(property = "updatedAt", column = "COL_UPDATED_AT"),
            @Result(property = "part", column = "COL_PART"),
            @Result(property = "serial", column = "COL_SERIAL")
    })
    List<SpeakingQuestion> findAllSpeakingQuestions();
}