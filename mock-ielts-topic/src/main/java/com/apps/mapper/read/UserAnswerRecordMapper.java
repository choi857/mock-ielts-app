package com.apps.mapper.read;


import com.apps.dto.read2.UserAnswerCorrectDTO;
import com.apps.model.read.UserAnswerRecord;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface UserAnswerRecordMapper {
    int insert(UserAnswerRecord record);

    int updateByPrimaryKey(UserAnswerRecord record);

    UserAnswerRecord selectByPrimaryKey(Long recordId);


    UserAnswerRecord selectByPrimaryKeyAndUserId(Long recordId,Long userId);

    int deleteByPrimaryKey(Long recordId);

    int updateScoreByRecordId(Map<String, Object> params);

    List<UserAnswerRecord> selectUserAnswerRecordsByReadSummaryId(Long readSummaryId);

    /**
     * 根据用户ID查询用户所有的主答案列表
     * @param userId 用户ID
     */
    List<UserAnswerRecord> selectUserAnswerRecordsByUserId(@Param("userId") Long userId);

}