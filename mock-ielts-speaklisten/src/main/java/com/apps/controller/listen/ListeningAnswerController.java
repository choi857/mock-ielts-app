package com.apps.controller.listen;

import com.apps.common.ResponseResult;
import com.apps.dto.listen.ListeningAnswerRecordDTO;
import com.apps.dto.listen.ListeningUserAnswerCorrectDetail;
import com.apps.dto.speak.SpeakingAnswerRecordDTO;
import com.apps.model.listen.ListeningUserAnswerDetail;
import com.apps.service.listen.ListeningAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/topic/listening/answers")
public class ListeningAnswerController {

    @Autowired
    private ListeningAnswerService listeningAnswerService;



    /**
     * 查询用户ID对应的听力答题主记录
     * @param userId 用户ID
     */
    @GetMapping("/user/{userId}")
    @Cacheable(value = "listeningAnswerRecords", key = "#userId")
    public ResponseResult<List<ListeningAnswerRecordDTO>> getUserListeningAnswerRecords(@PathVariable Long userId) {
        List<ListeningAnswerRecordDTO> answerRecords = listeningAnswerService.getUserListeningAnswerRecords(userId);
        return ResponseResult.success(answerRecords);
    }


    /**
     * 根据 recordId 和 userId 查询答题详细信息
     * @param userId 用户ID
     * @param recordId 记录ID
     * @return 答题详细信息
     */
    @GetMapping("/user/{userId}/record/{recordId}")
    @Cacheable(value = "listeningAnswerDetails", key = "#userId + '-' + #recordId")
    public ResponseResult<List<ListeningUserAnswerCorrectDetail>> getListeningAnswerDetailsByRecordIdAndUserId(
            @PathVariable Long userId, @PathVariable Long recordId) {
        List<ListeningUserAnswerCorrectDetail> answerDetails = listeningAnswerService.getListeningAnswerDetailsByRecordIdAndUserId(userId, recordId);
        if (answerDetails == null || answerDetails.isEmpty()) {
            return ResponseResult.fail("未找到对应的答题记录");
        }
        return ResponseResult.success(answerDetails);
    }

    /**
     *
     * @param recordId
     * @return
     */
    @GetMapping("/score/record/{recordId}")
    public ResponseResult getUserAnswerScoreByRecordId(@PathVariable Long recordId) {
        listeningAnswerService.calculateScoreAndEvaluation(recordId);
        return ResponseResult.success("评分成功");
    }
}
