package com.apps.controller.listen;

import com.apps.dto.listen.ListeningAnswerSubmissionDTO;
import com.apps.dto.listen.ListeningWithQuestionsAndAnswersDTO;
import com.apps.dto.listen.ListeningWithQuestionsDTO;
import com.apps.service.listen.ListeningAnswerService;
import com.apps.service.listen.ListeningService;
import com.apps.common.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/listening")
public class ListeningController {

    @Autowired
    private ListeningService listeningService;
    @Autowired
    private ListeningAnswerService listeningAnswerService;
    /**
     * 新增听力题及其题目和答案
     *
     * @param dto 听力题对象
     * @return ResponseResult<String> 响应结果
     */
    @PostMapping("admin/add")
    public ResponseResult<Object> addListeningWithQuestionsAndAnswers(@RequestBody ListeningWithQuestionsAndAnswersDTO dto) {
        return listeningService.addListeningWithQuestionsAndAnswers(dto);
    }

    /**
     * 查询听力题目及答案
     * @param listeningId
     * @return
     */
    @GetMapping("admin/getListeningWithQuestionsAndAnswers/{listeningId}")
    public ResponseResult<Object> getListeningWithQuestionsAndAnswers(@PathVariable Long listeningId) {
        return listeningService.getListeningWithQuestionsAndAnswers(listeningId);
    }
    /**
     * 更新听力题目
     * @param dto 听力题目对象
     * @return ResponseResult<String> 响应结果
     */
    @PostMapping("admin/update")
    public ResponseResult<Object> updateQuestion(@RequestBody ListeningWithQuestionsAndAnswersDTO dto) {
        return listeningService.updateListeningWithQuestionsAndAnswers(dto);
    }

    /**
     * 查询听力题目部分
     * @param listeningId 听力材料ID
     * @return ResponseResult<ListeningWithQuestionsDTO> 响应结果
     */
    @GetMapping("/getQuestions/{listeningId}")
    public ResponseResult<ListeningWithQuestionsDTO> getQuestionsByListeningId(@PathVariable Long listeningId) {
        return listeningService.getQuestionsByListeningIdAndPart(listeningId);
    }




    /**
     * 提交听力题答案
     * @param submission 提交的答案
     * @return ResponseResult<String> 响应结果
     */
    @PostMapping("/submit/answers")
    public ResponseResult<String> submitAnswers(@RequestBody ListeningAnswerSubmissionDTO submission) {
        listeningAnswerService.submitAnswers(submission);
        return ResponseResult.success("答案提交成功");
    }
}