package com.apps.controller.read;

import com.apps.common.ResponseResult;
import com.apps.dto.read2.AnswerRecordDTO;
import com.apps.dto.read2.UserAnswerCorrectDTO;
import com.apps.dto.read2.UserAnswerCorrectRecordDTO;
import com.apps.model.read.UserAnswerRecord;
import com.apps.service.read.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/topic/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private CacheManager cacheManager;
    /**
     * 保存答案列表
     * @param answersList 前端传过来的答案列表
     */
    @PostMapping("/save")
    public ResponseResult<Map> saveAnswers(@RequestBody List<Map<String, Object>> answersList) {
        Map<String, Long> stringLongMap = answerService.saveAnswers(answersList);
        // 手动清除缓存
        cacheManager.getCache("getUserAnswerRecordsALL").evict(stringLongMap.get("userId"));
        cacheManager.getCache("getUserAnswerRecordDetails")
                .evict(stringLongMap.get("userId") + "-" + stringLongMap.get("recordId"));
        return ResponseResult.success(stringLongMap);
    }


    /**
     * 校验答案并更新分数
     *   recordId 答题记录ID
     *   userId 用户ID
     */
    @PostMapping("/getscores")
    @Caching(evict = {
            @CacheEvict(value = "getUserAnswerRecordsALL", key = "#request.userId"),
            @CacheEvict(value = "getUserAnswerRecordDetails", key = "#request.userId + '-' + #request.recordId")
    })
    public void validateAnswers(@RequestBody UserAnswerRecord request) {
        Long readSummaryId = request.getReadSummaryId();
        Long userId = Long.valueOf(request.getUserId());
        answerService.validateAnswers(readSummaryId, userId);
    }


    /**
     * 查询用户所有的主答案列表
     * @param userId 用户ID
     */
    @GetMapping("/user/{userId}")
    @Cacheable(value = "getUserAnswerRecordsALL", key = "#userId")
    public ResponseResult<List<AnswerRecordDTO>> getUserAnswerRecordsALL(@PathVariable Long userId) {
        List<AnswerRecordDTO> answerRecords = answerService.getUserAnswerRecords(userId);
        return ResponseResult.success(answerRecords);
    }

    /**
     * 根据主答案表的ID查询用户明细答题的内容
     * @param recordId 主答案表的ID
     */
    @GetMapping("/user/{userId}/record/{recordId}")
    @Cacheable(value = "getUserAnswerRecordDetails", key = "#userId + '-' + #recordId")
    public ResponseResult<UserAnswerCorrectRecordDTO> getUserAnswerRecordDetails(@PathVariable("recordId") Long recordId,
                                                                           @PathVariable("userId") Long userId) {
        UserAnswerCorrectRecordDTO answerRecordDTO = answerService.getUserAnswerRecordDetails(recordId,userId);
        return ResponseResult.success(answerRecordDTO);
    }
}