package com.apps.mapper.write;

import com.apps.model.write.WritingTask;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;

public interface WritingTaskMapper {
    // 根据 task_id 查询对应的 col_writing_task 数据
    @Select("SELECT task_id, task_title, task_description " +
            "FROM COL_WRITING_TASK " +
            "WHERE task_id = #{taskId}")
    @Results({
            @Result(property = "taskId", column = "task_id"),
            @Result(property = "taskTitle", column = "task_title"),
            @Result(property = "taskDescription", column = "task_description")
    })
    WritingTask selectByTaskId(@Param("taskId") Long taskId);
}
