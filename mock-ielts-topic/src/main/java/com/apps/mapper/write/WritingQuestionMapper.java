package com.apps.mapper.write;

import com.apps.model.write.WritingQuestion;
import java.util.List;

public interface WritingQuestionMapper {
    int insert(WritingQuestion question);
    int update(WritingQuestion question);
    WritingQuestion selectById(Long id);
    int deleteById(Long id);
    List<WritingQuestion> selectAll();

    List<WritingQuestion> selectByTaskIds(List<Long> taskIds);
}