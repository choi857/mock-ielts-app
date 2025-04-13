package com.apps.service.speak.impl;

import com.apps.dto.speak.SpeakingDTO;
import com.apps.model.speak.Speaking;

import java.util.List;

/**
 * 口语材料服务接口
 */
public interface SpeakingService {
    /**
     * 创建口语材料
     * @param speakingDTO 口语材料DTO
     * @return 创建的口语材料DTO
     */
    SpeakingDTO createSpeaking(SpeakingDTO speakingDTO);

    /**
     * 更新口语材料
     * @param id 口语材料ID
     * @param speakingDTO 口语材料DTO
     * @return 更新后的口语材料DTO
     */
    SpeakingDTO updateSpeaking(Long id, SpeakingDTO speakingDTO);

    /**
     * 删除口语材料
     * @param id 口语材料ID
     */
    void deleteSpeaking(Long id);

    /**
     * 根据ID获取口语材料
     * @param id 口语材料ID
     * @return 口语材料DTO
     */
    SpeakingDTO getSpeakingById(Long id);

    /**
     * 获取所有口语材料
     * @return 口语材料DTO列表
     */
    List<Speaking> getAllSpeakings();
}