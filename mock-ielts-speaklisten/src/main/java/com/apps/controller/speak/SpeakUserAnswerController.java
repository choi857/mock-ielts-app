package com.apps.controller.speak;

import com.apps.common.ResponseResult;
import com.apps.dto.speak.SpeakUserAnswerDTO;
import com.apps.dto.speak.SpeakingAnswerRecordDTO;
import com.apps.service.speak.impl.SpeakUserAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户口语答题控制器
 * 提供用户提交口语题答案的接口
 */
@RestController
@RequestMapping("/speak/userAnswer")
public class SpeakUserAnswerController {

    @Autowired
    private SpeakUserAnswerService speakUserAnswerService;

    /**
     * 用户提交口语题答案
     * @param speakUserAnswerDTO 用户口语答题数据传输对象
     * @return 包裹在ResponseResult中的成功消息
     */
    @PostMapping("/submit")
    public ResponseResult<Void> submitUserAnswer(@RequestBody SpeakUserAnswerDTO speakUserAnswerDTO) {
        try {
            speakUserAnswerService.saveUserAnswer(speakUserAnswerDTO);
            return ResponseResult.success("用户口语题答案提交成功");
        } catch (IllegalArgumentException e) {
            return ResponseResult.fail("参数错误: " + e.getMessage());
        } catch (Exception e) {
            return ResponseResult.fail("提交失败: " + e.getMessage());
        }
    }

    /**
     * 根据记录ID获取用户口语答题记录
     * @param recordId 答题记录ID
     * @return 包裹在ResponseResult中的用户答题数据传输对象
     */
    @GetMapping("/{recordId}")
    public ResponseResult<SpeakUserAnswerDTO> getUserAnswerByRecordId(@PathVariable Long recordId) {
        try {
            SpeakUserAnswerDTO userAnswer = speakUserAnswerService.getUserAnswerByRecordId(recordId);
            return ResponseResult.success("获取用户口语答题记录成功", userAnswer);
        } catch (IllegalArgumentException e) {
            return ResponseResult.fail("参数错误: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseResult.fail("记录不存在: " + e.getMessage());
        } catch (Exception e) {
            return ResponseResult.fail("获取失败: " + e.getMessage());
        }
    }
/**
 * 根据recordId进行ai评分
 */
@GetMapping("/score/{recordId}")
public ResponseResult<SpeakUserAnswerDTO> getUserAnswerScoreByRecordId(@PathVariable Long recordId) {
    speakUserAnswerService.setUserAnswerScoreByRecordId(recordId);
    return ResponseResult.success("评分成功");
}

    /**
     * 根据用户id和记录id获取用户答题详情
     * @param userId
     * @param recordId
     * @return
     */
    @GetMapping("/user/{userId}/record/{recordId}/details")
    public ResponseResult<SpeakUserAnswerDTO> getUserAnswerDetailsByRecordIdAndUserId(
            @PathVariable Long userId, @PathVariable Long recordId) {
        try {
            SpeakUserAnswerDTO dto = speakUserAnswerService.getUserAnswerDetailsByRecordIdAndUserId(userId, recordId);
            return ResponseResult.success(dto);
        } catch (Exception e) {
            return ResponseResult.fail(e.getMessage());
        }
    }

    /**
     * 查询用户ID对应的口语答题主记录
     * @param userId 用户ID
     */
    @GetMapping("/user/{userId}")
    public ResponseResult<List<SpeakingAnswerRecordDTO>> getUserSpeakingAnswerRecords(@PathVariable Long userId) {
        List<SpeakingAnswerRecordDTO> answerRecords = speakUserAnswerService.getUserSpeakingAnswerRecords(userId);
        return ResponseResult.success(answerRecords);
    }
}