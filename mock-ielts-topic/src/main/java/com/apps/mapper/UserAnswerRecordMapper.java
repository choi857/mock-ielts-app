package com.apps.mapper;


import com.apps.model.UserAnswerRecord;

public interface UserAnswerRecordMapper {
    int insert(UserAnswerRecord record);

    int updateByPrimaryKey(UserAnswerRecord record);

    UserAnswerRecord selectByPrimaryKey(Long recordId);

    int deleteByPrimaryKey(Long recordId);
}