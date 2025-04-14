package com.apps.controller.write;

import com.apps.common.ResponseResult;
import com.apps.dto.write.WritingAnswerRecordDTO;
import com.apps.model.write.WritingAnswerDetail;
import com.apps.service.write.WritingAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/topic/writing/answers")
public class WritingAnswerController {

    @Autowired
    private WritingAnswerService writingAnswerService;

    /**
     * 保存用户答案列表
     * @param answersList
     */
    @PostMapping("/save")
    public void saveWritingAnswers(@RequestBody List<Map<String, Object>> answersList) {
        writingAnswerService.saveWritingAnswers(answersList);
    }


    /**
     * 查询用户ID对应的答题主记录
     * @param userId 用户ID
     */
    @GetMapping("/user/{userId}")
    public ResponseResult<List<WritingAnswerRecordDTO>> getUserWritingAnswerRecords(@PathVariable Long userId) {
        List<WritingAnswerRecordDTO> answerRecords = writingAnswerService.getUserWritingAnswerRecords(userId);
        return ResponseResult.success(answerRecords);
    }


    /**
     * 根据 userId 和 record_id 查询答题详细信息
     * @param userId 用户ID
     * @param recordId 记录ID
     * @return 答题详细信息
     */
    @GetMapping("/user/{userId}/record/{recordId}")
    public ResponseResult<List<WritingAnswerDetail>> getWritingAnswerRecordByUserIdAndRecordId(@PathVariable Long userId, @PathVariable Long recordId) {
        List<WritingAnswerDetail> answerDetail = writingAnswerService.getWritingAnswerRecordByUserIdAndRecordId(userId, recordId);
        if (answerDetail == null) {
            return ResponseResult.fail("未找到对应的答题记录");
        }
        return ResponseResult.success(answerDetail);
    }
}