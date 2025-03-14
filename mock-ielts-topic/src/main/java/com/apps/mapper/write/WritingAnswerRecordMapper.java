package com.apps.mapper.write;

import com.apps.model.write.WritingAnswerRecord;
import java.util.List;

public interface WritingAnswerRecordMapper {
    int insert(WritingAnswerRecord record);
    WritingAnswerRecord selectByPrimaryKey(Long recordId);

}