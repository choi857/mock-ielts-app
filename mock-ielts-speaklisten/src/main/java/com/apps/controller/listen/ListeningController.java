package com.apps.controller.listen;

import com.apps.dto.listen.ListeningAnswerSubmissionDTO;
import com.apps.dto.listen.ListeningWithQuestionsAndAnswersDTO;
import com.apps.dto.listen.ListeningWithQuestionsDTO;
import com.apps.model.listen.Listening;
import com.apps.service.listen.ListeningAnswerService;
import com.apps.service.listen.ListeningService;
import com.apps.common.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/listening")
public class ListeningController {

    @Autowired
    private ListeningService listeningService;
    @Autowired
    private ListeningAnswerService listeningAnswerService;

    @Autowired
    private CacheManager cacheManager;
    /**
     * 新增听力题及其题目和答案
     *
     * @param dto 听力题对象
     * @return ResponseResult<String> 响应结果
     */
    @PostMapping("admin/add")
    @CacheEvict(value = "allListenings", key = "'all'")
    public ResponseResult<Object> addListeningWithQuestionsAndAnswers(@RequestBody ListeningWithQuestionsAndAnswersDTO dto) {
        return listeningService.addListeningWithQuestionsAndAnswers(dto);
    }

    /**
     * 查询听力题目及答案
     * @param listeningId
     * @return
     */
    @GetMapping("admin/getListeningWithQuestionsAndAnswers/{listeningId}")
    @Cacheable(value = "getListeningWithQuestionsAndAnswers", key = "#listeningId")
    public ResponseResult<Object> getListeningWithQuestionsAndAnswers(@PathVariable Long listeningId) {
        return listeningService.getListeningWithQuestionsAndAnswers(listeningId);
    }
    /**
     * 更新听力题目
     * @param dto 听力题目对象
     * @return ResponseResult<String> 响应结果
     */
    @PostMapping("admin/update")
    @Caching(evict = {
            @CacheEvict(value = "getListeningWithQuestionsAndAnswers", key = "#dto.listening.id"),
            @CacheEvict(value = "getQuestionsByListeningId", key = "#dto.listening.id")
    })
    public ResponseResult<Object> updateQuestion(@RequestBody ListeningWithQuestionsAndAnswersDTO dto) {
        return listeningService.updateListeningWithQuestionsAndAnswers(dto);
    }


    /**
     * 查询听力题目部分
     * @param listeningId 听力材料ID
     * @return ResponseResult<ListeningWithQuestionsDTO> 响应结果
     */
    @GetMapping("/getQuestions/{listeningId}")
    @Cacheable(value = "getQuestionsByListeningId", key = "#listeningId")
    public ResponseResult<ListeningWithQuestionsDTO> getQuestionsByListeningId(@PathVariable Long listeningId) {
        return listeningService.getQuestionsByListeningIdAndPart(listeningId);
    }


    /**
     * 查询所有听力材料
     * @return 所有听力材料
     */
    @GetMapping("/all")
    @Cacheable(value = "allListenings", key = "'all'")
    public ResponseResult<List<Listening>> getAllListenings() {
        return listeningService.getAllListenings();
    }

    /**
     * 提交听力题答案
     * @param submission 提交的答案
     * @return ResponseResult<String> 响应结果
     */
    @PostMapping("/submit/answers")
    public ResponseResult<String> submitAnswers(@RequestBody ListeningAnswerSubmissionDTO submission) {
        Long recordId = listeningAnswerService.submitAnswers(submission);
        // 手动清除缓存
        cacheManager.getCache("listeningAnswerRecords").evict(submission.getListening().getUserId());
        cacheManager.getCache("getUserAnswerRecordDetails")
                .evict(submission.getListening().getUserId() + "-" + recordId);

        return ResponseResult.success("答案提交成功");
    }
}