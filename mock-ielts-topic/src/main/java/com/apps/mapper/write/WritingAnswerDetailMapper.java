package com.apps.mapper.write;

import com.apps.model.write.WritingAnswerDetail;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WritingAnswerDetailMapper {
    int insert(WritingAnswerDetail detail);
    // 根据 record_id 查询对应的 col_write_user_answer_detail 数据
    @Select("SELECT detail_id, record_id, user_id, task_type, answer_content,score,answer_evaluation " +
            "FROM COL_WRITE_USER_ANSWER_DETAIL " +
            "WHERE record_id = #{recordId}")
    @Results({
            @Result(property = "detailId", column = "detail_id"),
            @Result(property = "recordId", column = "record_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "taskType", column = "task_type"),
            @Result(property = "answerContent", column = "answer_content"),
            @Result(property = "score", column = "score"),
            @Result(property = "answerEvaluation", column = "answer_evaluation")
    })
    List<WritingAnswerDetail> selectByRecordId(@Param("recordId") Long recordId);

    // 更新 col_write_user_answer_detail 数据
    @Update("UPDATE COL_WRITE_USER_ANSWER_DETAIL " +
            "SET score = #{score}, answer_evaluation = #{answerEvaluation} " +
            "WHERE detail_id = #{detailId}")
    int updateByPrimaryKey(WritingAnswerDetail detail);

}