package com.apps.mapper.speak;

import com.apps.model.speak.SpeakUserAnswerDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SpeakUserAnswerDetailMapper {

    @Insert("INSERT INTO COL_SPEAK_USER_ANSWER_DETAIL (RECORD_ID, USER_ID, QUESTION_ID, USER_AUDIO_URL, USER_TRANSCRIPT, SCORE, FEEDBACK, CREATED_AT, COL_PART,DETAIL_ID,QUESTION_CONTENT,USER_AUDIO_URL_TO_AI) " +
            "VALUES (#{recordId}, #{userId}, #{questionId}, #{userAudioUrl}, #{userTranscript}, #{score}, #{feedback}, #{createdAt}, #{part},#{detailId},#{questionContent},#{userAudioUrlToAi})")
    @Options(useGeneratedKeys = true, keyProperty = "detailId")
    void insert(SpeakUserAnswerDetail detail);

    @Select("SELECT * FROM COL_SPEAK_USER_ANSWER_DETAIL WHERE RECORD_ID = #{recordId} AND (SCORE IS NULL OR SCORE = 0.00 )")
    @Results({
            @Result(column = "DETAIL_ID", property = "detailId"),
            @Result(column = "RECORD_ID", property = "recordId"),
            @Result(column = "USER_ID", property = "userId"),
            @Result(column = "QUESTION_ID", property = "questionId"),
            @Result(column = "USER_AUDIO_URL", property = "userAudioUrl"),
            @Result(column = "USER_TRANSCRIPT", property = "userTranscript"),
            @Result(column = "SCORE", property = "score"),
            @Result(column = "FEEDBACK", property = "feedback"),
            @Result(column = "CREATED_AT", property = "createdAt"),
            @Result(column = "COL_PART", property = "part"),
            @Result(column = "QUESTION_CONTENT", property = "questionContent"),
            @Result(column = "USER_AUDIO_URL_TO_AI", property = "userAudioUrlToAi")
    })
    List<SpeakUserAnswerDetail> findByRecordId(Long recordId);

    @Update("UPDATE COL_SPEAK_USER_ANSWER_DETAIL SET SCORE = #{score} WHERE DETAIL_ID = #{detailId}")
    void updateScoreById(@Param("detailId") Long detailId, @Param("score") double score);
    @Select("SELECT " +
            "DETAIL_ID AS detailId, " +
            "RECORD_ID AS recordId, " +
            "USER_ID AS userId, " +
            "QUESTION_ID AS questionId, " +
            "USER_AUDIO_URL AS userAudioUrl, " +
            "USER_TRANSCRIPT AS userTranscript, " +
            "SCORE AS score, " +
            "FEEDBACK AS feedback, " +
            "CREATED_AT AS createdAt, " +
            "COL_PART AS part, " +
            "QUESTION_CONTENT AS questionContent " +
            "FROM COL_SPEAK_USER_ANSWER_DETAIL " +
            "WHERE RECORD_ID = #{recordId} AND USER_ID = #{userId}")
    @Results({
            @Result(property = "detailId", column = "detailId"),
            @Result(property = "recordId", column = "recordId"),
            @Result(property = "userId", column = "userId"),
            @Result(property = "questionId", column = "questionId"),
            @Result(property = "userAudioUrl", column = "userAudioUrl"),
            @Result(property = "userTranscript", column = "userTranscript"),
            @Result(property = "score", column = "score"),
            @Result(property = "feedback", column = "feedback"),
            @Result(property = "createdAt", column = "createdAt"),
            @Result(property = "part", column = "part"),
            @Result(property = "questionContent", column = "questionContent")
    })
    List<SpeakUserAnswerDetail> findByRecordIdAndUserId(@Param("recordId") Long recordId, @Param("userId") Long userId);

}