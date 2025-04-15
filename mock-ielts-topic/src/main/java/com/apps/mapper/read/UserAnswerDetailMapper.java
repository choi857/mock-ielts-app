package com.apps.mapper.read;


import com.apps.dto.read.AnswerValidationDTO;
import com.apps.dto.read2.UserAnswerCorrectDTO;
import com.apps.model.read.UserAnswerDetail;
import com.apps.model.read.UserAnswerRecord;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface UserAnswerDetailMapper {
    int insert(UserAnswerDetail record);

    int updateByPrimaryKey(UserAnswerDetail record);
    int updatecCrrectByPrimaryKey(Long detailId,Boolean isCorrect);

    UserAnswerDetail selectByPrimaryKey(Long detailId);

    int deleteByPrimaryKey(Long detailId);

    List<AnswerValidationDTO> selectUserAnswerDetailsByRecordIdAndUserId(Map<String, Object> params);


    /**
     * 根据主答案表的ID查询用户明细答题的内容
     * @param recordId 主答案表的ID
     */
    List<UserAnswerDetail> selectByRecordId(@Param("recordId") Long recordId);

    List<UserAnswerCorrectDTO> selectUserAnswerCorrectDTOByRecordId(@Param("recordId") Long recordId);


}
