package com.apps.mapper.write;

    import com.apps.model.write.WritingTaskRecord;
    import java.util.List;

    public interface WritingTaskRecordMapper {
        int insert(WritingTaskRecord record);
        int update(WritingTaskRecord record);
        WritingTaskRecord selectById(Long id);
        int deleteById(Long id);
        List<WritingTaskRecord> selectAll();

        // Add this method
        WritingTaskRecord selectByRecordId(Long recordId);
    }