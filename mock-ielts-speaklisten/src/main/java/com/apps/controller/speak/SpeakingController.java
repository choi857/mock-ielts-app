package com.apps.controller.speak;

import com.apps.common.ResponseResult;
import com.apps.dto.speak.SpeakingDTO;
import com.apps.service.speak.impl.SpeakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 口语材料控制器
 */
@RestController
@RequestMapping("/speaking")
public class SpeakingController {

    @Autowired
    private SpeakingService speakingService;

    /**
     * 创建口语材料
     * @param speakingDTO 口语材料DTO
     * @return 包裹在ResponseResult中的创建的口语材料DTO
     */
    @PostMapping("/admin/add")
    public ResponseResult<SpeakingDTO> createSpeaking(@RequestBody SpeakingDTO speakingDTO) {
        SpeakingDTO createdSpeaking = speakingService.createSpeaking(speakingDTO);
        return ResponseResult.success("口语材料创建成功", createdSpeaking);
    }

    /**
     * 更新口语材料
     * @param id 口语材料ID
     * @param speakingDTO 口语材料DTO
     * @return 包裹在ResponseResult中的更新后的口语材料DTO
     */
    @PostMapping("/update/{id}")
    public ResponseResult<SpeakingDTO> updateSpeaking(@PathVariable Long id, @RequestBody SpeakingDTO speakingDTO) {
        SpeakingDTO updatedSpeaking = speakingService.updateSpeaking(id, speakingDTO);
        return ResponseResult.success("口语材料更新成功", updatedSpeaking);
    }

    /**
     * 删除口语材料
     * @param id 口语材料ID
     * @return 包裹在ResponseResult中的空响应
     */
    @PostMapping("/delete/{id}")
    public ResponseResult<Void> deleteSpeaking(@PathVariable Long id) {
        speakingService.deleteSpeaking(id);
        return ResponseResult.success("口语材料删除成功");
    }

    /**
     * 根据ID获取口语材料
     * @param id 口语材料ID
     * @return 包裹在ResponseResult中的口语材料DTO
     */
    @GetMapping("/{id}")
    public ResponseResult<SpeakingDTO> getSpeakingById(@PathVariable Long id) {
        SpeakingDTO speakingDTO = speakingService.getSpeakingById(id);
        return ResponseResult.success("口语材料获取成功", speakingDTO);
    }

    /**
     * 获取所有口语材料
     * @return 包裹在ResponseResult中的口语材料DTO列表
     */
    @GetMapping("/all")
    public ResponseResult<List<SpeakingDTO>> getAllSpeakings() {
        List<SpeakingDTO> speakings = speakingService.getAllSpeakings();
        return ResponseResult.success("所有口语材料获取成功", speakings);
    }
}