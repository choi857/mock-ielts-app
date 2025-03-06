package com.apps.mapper;


import com.apps.model.UserAnswerDetail;

public interface UserAnswerDetailMapper {
    int insert(UserAnswerDetail record);

    int updateByPrimaryKey(UserAnswerDetail record);

    UserAnswerDetail selectByPrimaryKey(Long detailId);

    int deleteByPrimaryKey(Long detailId);
}
