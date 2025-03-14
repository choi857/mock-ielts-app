package com.apps.mapper.read;


import com.apps.DTO.read.AnswerValidationDTO;
import com.apps.model.read.UserAnswerDetail;

import java.util.List;
import java.util.Map;

public interface UserAnswerDetailMapper {
    int insert(UserAnswerDetail record);

    int updateByPrimaryKey(UserAnswerDetail record);

    UserAnswerDetail selectByPrimaryKey(Long detailId);

    int deleteByPrimaryKey(Long detailId);

    List<AnswerValidationDTO> selectUserAnswerDetailsByRecordIdAndUserId(Map<String, Object> params);
}
