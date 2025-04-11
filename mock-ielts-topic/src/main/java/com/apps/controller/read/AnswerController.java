package com.apps.controller.read;

import com.apps.common.ResponseResult;
import com.apps.model.read.UserAnswerRecord;
import com.apps.service.read.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/topic/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    /**
     * 保存答案列表
     * @param answersList 前端传过来的答案列表
     */
    @PostMapping("/save")
    public ResponseResult<Map> saveAnswers(@RequestBody List<Map<String, Object>> answersList) {
        Map<String, Long> stringLongMap = answerService.saveAnswers(answersList);
        return ResponseResult.success(stringLongMap);
    }


    /**
     * 校验答案并更新分数
     *   recordId 答题记录ID
     *   userId 用户ID
     */
    @PostMapping("/getscores")
    public void validateAnswers(@RequestBody UserAnswerRecord request) {
        Long readSummaryId = request.getReadSummaryId();
        Long userId = Long.valueOf(request.getUserId());
        answerService.validateAnswers(readSummaryId, userId);
    }
}