package com.apps.mapper.write;

import com.apps.model.write.WritingAnswerRecord;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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


}