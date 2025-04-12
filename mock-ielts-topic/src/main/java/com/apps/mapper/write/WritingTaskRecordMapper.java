package com.apps.mapper.write;

    import com.apps.model.write.WritingTask;
    import com.apps.model.write.WritingTaskRecord;
    import org.apache.ibatis.annotations.Result;
    import org.apache.ibatis.annotations.Results;
    import org.apache.ibatis.annotations.Select;
    import org.springframework.data.repository.query.Param;

    import java.util.List;

    public interface WritingTaskRecordMapper {
        int insert(WritingTaskRecord record);
        int update(WritingTaskRecord record);
        WritingTaskRecord selectById(Long id);
        int deleteById(Long id);
        List<WritingTaskRecord> selectAll();

        // Add this method
        WritingTaskRecord selectByRecordId(Long recordId);



        // 根据 task_id 查询对应的 col_writing_task 数据
        @Select("SELECT task_id AS taskId, task_title AS taskTitle, task_description AS taskDescription " +
                "FROM COL_WRITING_TASK " +
                "WHERE task_id = #{taskId}")
        @Results({
                @Result(property = "taskId", column = "task_id"),
                @Result(property = "taskTitle", column = "task_title"),
                @Result(property = "taskDescription", column = "task_description")
        })
        WritingTask selectByTaskId(@Param("taskId") Long taskId);

    }