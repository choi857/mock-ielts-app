package com.apps.mapper.read;


import com.apps.model.read.UserAnswerRecord;

import java.util.List;
import java.util.Map;

public interface UserAnswerRecordMapper {
    int insert(UserAnswerRecord record);

    int updateByPrimaryKey(UserAnswerRecord record);

    UserAnswerRecord selectByPrimaryKey(Long recordId);

    int deleteByPrimaryKey(Long recordId);

    int updateScoreByRecordId(Map<String, Object> params);

    List<UserAnswerRecord> selectUserAnswerRecordsByReadSummaryId(Long readSummaryId);

}