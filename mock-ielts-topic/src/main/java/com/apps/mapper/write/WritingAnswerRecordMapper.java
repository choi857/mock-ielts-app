package com.apps.mapper.write;

import com.apps.dto.write.WritingAnswerRecordDTO;
import com.apps.model.write.WritingAnswerRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface WritingAnswerRecordMapper {
    int insert(WritingAnswerRecord record);
    WritingAnswerRecord selectByPrimaryKey(Long recordId);



    // 查询 total_score 为空或者为0的记录
    @Select("SELECT record_id, user_id, task1_id , task2_id, total_score " +
            "FROM COL_WRITE_USER_ANSWER_RECORD " +
            "WHERE total_score IS NULL OR total_score = 0")
    @Results({
            @Result(property = "recordId", column = "record_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "task1Id", column = "task1_id"),
            @Result(property = "task2Id", column = "task2_id"),
            @Result(property = "totalScore", column = "total_score")
    })
    List<WritingAnswerRecord> selectByTotalScoreIsNullOrZero();

    // 更新 col_write_user_answer_record 数据
    @Update("UPDATE COL_WRITE_USER_ANSWER_RECORD " +
            "SET total_score = #{totalScore}, updated_at = #{updatedAt} " +
            "WHERE record_id = #{recordId}")
    int updateByPrimaryKey(WritingAnswerRecord record);
    /**
     * 查询用户ID对应的答题主记录
     * @param userId 用户ID
     */
    List<WritingAnswerRecordDTO> selectUserWritingAnswerRecordsByUserId(@Param("userId") Long userId);

    /**
     * 根据 userId 和 record_id 查询答题记录
     * @param userId 用户ID
     * @param recordId 记录ID
     * @return WritingAnswerRecord 对象
     */
    WritingAnswerRecord selectByUserIdAndRecordId(@Param("userId") Long userId, @Param("recordId") Long recordId);

}