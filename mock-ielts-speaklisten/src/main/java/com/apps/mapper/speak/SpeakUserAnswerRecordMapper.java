package com.apps.mapper.speak;

import com.apps.dto.speak.SpeakingAnswerRecordDTO;
import com.apps.model.speak.SpeakUserAnswerRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户口语答题主记录Mapper
 */
@Mapper
public interface SpeakUserAnswerRecordMapper {

    @Insert("INSERT INTO COL_SPEAK_USER_ANSWER_RECORD (USER_ID, SPEAKING_ID, SCORE, ANSWER_EVALUATION, DURATION_SECONDS, DEVICE_TYPE, CREATED_AT, UPDATED_AT, COL_PART,RECORD_ID) " +
            "VALUES (#{userId}, #{speakingId}, #{score}, #{answerEvaluation}, #{durationSeconds}, #{deviceType}, #{createdAt}, #{updatedAt}, #{part},#{recordId})")
    @Options(useGeneratedKeys = true, keyProperty = "recordId")
    void insert(SpeakUserAnswerRecord record);

    @Select("SELECT * FROM COL_SPEAK_USER_ANSWER_RECORD WHERE RECORD_ID = #{recordId}")
    @Results({
            @Result(column = "RECORD_ID", property = "recordId"),
            @Result(column = "USER_ID", property = "userId"),
            @Result(column = "SPEAKING_ID", property = "speakingId"),
            @Result(column = "SCORE", property = "score"),
            @Result(column = "ANSWER_EVALUATION", property = "answerEvaluation"),
            @Result(column = "DURATION_SECONDS", property = "durationSeconds"),
            @Result(column = "DEVICE_TYPE", property = "deviceType"),
            @Result(column = "CREATED_AT", property = "createdAt"),
            @Result(column = "UPDATED_AT", property = "updatedAt"),
            @Result(column = "COL_PART", property = "part")
    })
    SpeakUserAnswerRecord findById(Long recordId);



    @Select("SELECT * FROM COL_SPEAK_USER_ANSWER_RECORD WHERE USER_ID = #{userId}")
    @Results({
            @Result(column = "RECORD_ID", property = "recordId"),
            @Result(column = "USER_ID", property = "userId"),
            @Result(column = "SPEAKING_ID", property = "speakingId"),
            @Result(column = "SCORE", property = "score"),
            @Result(column = "ANSWER_EVALUATION", property = "answerEvaluation"),
            @Result(column = "DURATION_SECONDS", property = "durationSeconds"),
            @Result(column = "DEVICE_TYPE", property = "deviceType"),
            @Result(column = "CREATED_AT", property = "createdAt"),
            @Result(column = "UPDATED_AT", property = "updatedAt"),
            @Result(column = "COL_PART", property = "part")
    })
    List<SpeakUserAnswerRecord> findByUserId(Integer userId);

    @Update("UPDATE COL_SPEAK_USER_ANSWER_RECORD SET SCORE = #{score} WHERE RECORD_ID = #{recordId}")
    void updateScoreById(@Param("recordId") Long recordId, @Param("score") double score);
    /**
     * 查询用户ID对应的口语答题主记录
     * @param userId 用户ID
     */
    @Select("SELECT " +
            "user.record_id AS recordId, " +
            "user.user_id AS userId, " +
            "user.speaking_id AS speakingId, " +
            "user.score AS score, " +
            "user.duration_seconds AS durationSeconds, " +
            "user.device_type AS deviceType, " +
            "user.created_at AS createdAt, " +
            "user.updated_at AS updatedAt, " +
            "user.answer_evaluation AS answerEvaluation, " +
            "user.col_part AS part, " +
            "spe.COL_TITLE AS title " +
            "FROM COL_SPEAK_USER_ANSWER_RECORD user LEFT JOIN col_speaking spe ON user.SPEAKING_ID = spe.COL_ID" +
            " WHERE user.USER_ID = #{userId}")
    List<SpeakingAnswerRecordDTO> selectUserSpeakingAnswerRecordsByUserId(@Param("userId") Long userId);

}