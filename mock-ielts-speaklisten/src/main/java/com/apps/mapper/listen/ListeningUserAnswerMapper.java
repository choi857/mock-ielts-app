package com.apps.mapper.listen;

import com.apps.model.listen.ListeningUserAnswerDetail;
import com.apps.model.listen.ListeningUserAnswerRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 听力用户答案Mapper接口
 */
@Mapper
public interface ListeningUserAnswerMapper {

    @Insert("INSERT INTO COL_LISTEN_USER_ANSWER_RECORD (USER_ID, LISTENING_ID, CREATED_AT, UPDATED_AT,RECORD_ID) VALUES (#{userId}, #{listeningId}, #{createdAt}, #{updatedAt}, #{id})")
    void insertUserAnswerRecord(ListeningUserAnswerRecord record);

    @Insert("INSERT INTO COL_LISTEN_USER_ANSWER_DETAIL (RECORD_ID, USER_ID, QUESTION_ID, ANSWER_TYPE, SUBMITTED_ANSWER, CREATED_AT, COL_PART) VALUES (#{recordId}, #{userId}, #{questionId}, #{answerType}, #{submittedAnswer}, #{createdAt}, #{part})")
    void insertUserAnswerDetail(ListeningUserAnswerDetail detail);

    @Select("SELECT * FROM COL_LISTEN_USER_ANSWER_RECORD WHERE USER_ID = #{userId}")
    @Results({
            @Result(property = "id", column = "RECORD_ID"),
            @Result(property = "userId", column = "USER_ID"),
            @Result(property = "listeningId", column = "LISTENING_ID"),
            @Result(property = "createdAt", column = "CREATED_AT"),
            @Result(property = "updatedAt", column = "UPDATED_AT")
    })
    List<ListeningUserAnswerRecord> findUserAnswerRecordsByUserId(Long userId);


    @Select("SELECT * FROM COL_LISTEN_USER_ANSWER_DETAIL WHERE RECORD_ID = #{recordId}")
    @Results({
            @Result(property = "id", column = "DETAIL_ID"),
            @Result(property = "recordId", column = "RECORD_ID"),
            @Result(property = "userId", column = "USER_ID"),
            @Result(property = "questionId", column = "QUESTION_ID"),
            @Result(property = "answerType", column = "ANSWER_TYPE"),
            @Result(property = "submittedAnswer", column = "SUBMITTED_ANSWER"),
            @Result(property = "isCorrect", column = "IS_CORRECT"),
            @Result(property = "createdAt", column = "CREATED_AT"),
            @Result(property = "part", column = "COL_PART")
    })
    List<ListeningUserAnswerDetail> findUserAnswerDetailsByRecordId(Long recordId);

    @Update("UPDATE COL_LISTEN_USER_ANSWER_RECORD SET SCORE = #{score}, ANSWER_EVALUATION = #{answerEvaluation}, DURATION_SECONDS = #{durationSeconds}, DEVICE_TYPE = #{deviceType}, UPDATED_AT = CURRENT_TIMESTAMP WHERE RECORD_ID = #{id}")
    void updateUserAnswerRecord(ListeningUserAnswerRecord record);


    @Update("UPDATE COL_LISTEN_USER_ANSWER_DETAIL SET IS_CORRECT = #{isCorrect} WHERE DETAIL_ID = #{id}")
    void updateUserAnswerDetail(ListeningUserAnswerDetail detail);
}