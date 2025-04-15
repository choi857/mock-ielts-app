package com.apps.mapper.listen;

import com.apps.dto.listen.ListeningAnswerRecordDTO;
import com.apps.dto.listen.ListeningUserAnswerCorrectDetail;
import com.apps.model.listen.ListeningUserAnswerDetail;
import com.apps.model.listen.ListeningUserAnswerRecord;
import org.apache.ibatis.annotations.*;

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
    @Update("UPDATE COL_LISTEN_USER_ANSWER_RECORD SET SCORE = #{score}, ANSWER_EVALUATION = #{answerEvaluation},UPDATED_AT = CURRENT_TIMESTAMP WHERE RECORD_ID = #{id}")
    void updateUserAnswerScoreRecord(ListeningUserAnswerRecord record);

    @Update("UPDATE COL_LISTEN_USER_ANSWER_DETAIL SET IS_CORRECT = #{isCorrect} WHERE DETAIL_ID = #{id}")
    void updateUserAnswerDetail(ListeningUserAnswerDetail detail);

    @Select("SELECT DETAIL_ID AS id, RECORD_ID AS recordId, USER_ID AS userId, QUESTION_ID AS questionId, CREATED_AT AS createdAt, COL_PART AS part,SUBMITTED_ANSWER AS submittedAnswer,IS_CORRECT AS correct, \n" +
            "    COALESCE(\n" +
            "            (SELECT COL_PLACEHOLDER_FORMAT FROM col_listening_question WHERE col_id = user.question_id AND COL_TYPE = 'FILL_IN_THE_BLANK'  LIMIT 1),\n" +
            "            (SELECT COL_CONTENT FROM col_listening_answer WHERE COL_QUESTION_ID = user.question_id AND COL_IS_CORRECT = TRUE LIMIT 1),\n" +
            "            (SELECT COL_MATCHING_KEY FROM col_listening_answer WHERE COL_QUESTION_ID = user.question_id AND COL_IS_CORRECT = TRUE LIMIT 1)\n" +
            "        ) AS mergedColumn, " +
            "(SELECT score FROM col_listen_user_answer_record WHERE  record_id = user.RECORD_ID) AS score \n"+
            "FROM COL_LISTEN_USER_ANSWER_DETAIL user " +
            "WHERE RECORD_ID = #{recordId} AND USER_ID = #{userId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "recordId", column = "recordId"),
            @Result(property = "userId", column = "userId"),
            @Result(property = "questionId", column = "questionId"),
            @Result(property = "createdAt", column = "createdAt"),
            @Result(property = "part", column = "part"),
            @Result(property = "submittedAnswer", column = "submittedAnswer"),
            @Result(property = "mergedColumn", column = "mergedColumn"),
            @Result(property = "correct", column = "correct"),
            @Result(property = "score", column = "score")
    })
    List<ListeningUserAnswerCorrectDetail> findUserAnswerDetailsByRecordIdAndUserId(@Param("userId") Long userId, @Param("recordId") Long recordId);

 }