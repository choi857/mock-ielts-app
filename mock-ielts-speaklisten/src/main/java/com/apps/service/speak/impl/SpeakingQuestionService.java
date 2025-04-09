package com.apps.service.speak.impl;

import com.apps.dto.speak.SpeakingQuestionDTO;
import java.util.List;

/**
 * 口语题目服务接口
 */
public interface SpeakingQuestionService {
    /**
     * 创建口语题目
     * @param speakingQuestionDTO 口语题目DTO
     * @return 创建的口语题目DTO
     */
    SpeakingQuestionDTO createSpeakingQuestion(SpeakingQuestionDTO speakingQuestionDTO);

    /**
     * 更新口语题目
     * @param id 口语题目ID
     * @param speakingQuestionDTO 口语题目DTO
     * @return 更新后的口语题目DTO
     */
    SpeakingQuestionDTO updateSpeakingQuestion(Long id, SpeakingQuestionDTO speakingQuestionDTO);

    /**
     * 删除口语题目
     * @param id 口语题目ID
     */
    void deleteSpeakingQuestion(Long id);

    /**
     * 根据ID获取口语题目
     * @param id 口语题目ID
     * @return 口语题目DTO
     */
    SpeakingQuestionDTO getSpeakingQuestionById(Long id);

    /**
     * 获取所有口语题目
     * @return 口语题目DTO列表
     */
    List<SpeakingQuestionDTO> getAllSpeakingQuestions();

    /**
     * 根据口语材料ID获取所有口语题目
     * @param speakingId 口语材料ID
     * @return 口语题目DTO列表
     */
    List<SpeakingQuestionDTO> getSpeakingQuestionsBySpeakingId(Long speakingId);
}