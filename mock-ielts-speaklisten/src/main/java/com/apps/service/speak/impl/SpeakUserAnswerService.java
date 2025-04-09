package com.apps.service.speak.impl;

import com.apps.dto.speak.SpeakUserAnswerDTO;

/**
 * 用户口语答题服务接口
 * 提供保存和查询用户答题记录的功能
 */
public interface SpeakUserAnswerService {

    /**
     * 保存用户答题记录
     * @param speakUserAnswerDTO 用户答题数据传输对象
     */
    void saveUserAnswer(SpeakUserAnswerDTO speakUserAnswerDTO);

    /**
     * 根据记录ID获取用户答题记录
     * @param recordId 答题记录ID
     * @return 用户答题数据传输对象
     */
    SpeakUserAnswerDTO getUserAnswerByRecordId(Long recordId);

    /**
     * 根据记录ID设置用户答题评分
     * @param recordId 答题记录ID
     * @return 用户答题数据传输对象
     */
    void setUserAnswerScoreByRecordId(Long recordId);
}