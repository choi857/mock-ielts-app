package com.apps.controller.speak;

import com.apps.common.ResponseResult;
import com.apps.dto.speak.SpeakingQuestionDTO;
import com.apps.service.speak.impl.SpeakingQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 口语题目控制器
 */
@RestController
@RequestMapping("/speaking/questions")
public class SpeakingQuestionController {

    @Autowired
    private SpeakingQuestionService speakingQuestionService;

    /**
     * 创建口语题目
     * @param speakingQuestionDTO 口语题目DTO
     * @return 包裹在ResponseResult中的创建的口语题目DTO
     */
    @PostMapping("/create")
    public ResponseResult<SpeakingQuestionDTO> createSpeakingQuestion(@RequestBody SpeakingQuestionDTO speakingQuestionDTO) {
        SpeakingQuestionDTO createdQuestion = speakingQuestionService.createSpeakingQuestion(speakingQuestionDTO);
        return ResponseResult.success("口语题目创建成功", createdQuestion);
    }

    /**
     * 更新口语题目
     * @param id 口语题目ID
     * @param speakingQuestionDTO 口语题目DTO
     * @return 包裹在ResponseResult中的更新后的口语题目DTO
     */
    @PostMapping("/update/{id}")
    public ResponseResult<SpeakingQuestionDTO> updateSpeakingQuestion(@PathVariable Long id, @RequestBody SpeakingQuestionDTO speakingQuestionDTO) {
        SpeakingQuestionDTO updatedQuestion = speakingQuestionService.updateSpeakingQuestion(id, speakingQuestionDTO);
        return ResponseResult.success("口语题目更新成功", updatedQuestion);
    }

    /**
     * 删除口语题目
     * @param id 口语题目ID
     * @return 包裹在ResponseResult中的空响应
     */
    @PostMapping("/delete/{id}")
    public ResponseResult<Void> deleteSpeakingQuestion(@PathVariable Long id) {
        speakingQuestionService.deleteSpeakingQuestion(id);
        return ResponseResult.success("口语题目删除成功");
    }

    /**
     * 根据ID获取口语题目
     * @param id 口语题目ID
     * @return 包裹在ResponseResult中的口语题目DTO
     */
    @GetMapping("/{id}")
    public ResponseResult<SpeakingQuestionDTO> getSpeakingQuestionById(@PathVariable Long id) {
        SpeakingQuestionDTO questionDTO = speakingQuestionService.getSpeakingQuestionById(id);
        return ResponseResult.success("口语题目获取成功", questionDTO);
    }

    /**
     * 获取所有口语题目
     * @return 包裹在ResponseResult中的口语题目DTO列表
     */
    @GetMapping
    public ResponseResult<List<SpeakingQuestionDTO>> getAllSpeakingQuestions() {
        List<SpeakingQuestionDTO> questions = speakingQuestionService.getAllSpeakingQuestions();
        return ResponseResult.success("所有口语题目获取成功", questions);
    }

    /**
     * 根据口语材料ID获取所有口语题目
     * @param speakingId 口语材料ID
     * @return 包裹在ResponseResult中的口语题目DTO列表
     */
    @GetMapping("/by-speaking/{speakingId}")
    public ResponseResult<List<SpeakingQuestionDTO>> getSpeakingQuestionsBySpeakingId(@PathVariable Long speakingId) {
        List<SpeakingQuestionDTO> questions = speakingQuestionService.getSpeakingQuestionsBySpeakingId(speakingId);
        return ResponseResult.success("根据口语材料ID获取口语题目成功", questions);
    }
}