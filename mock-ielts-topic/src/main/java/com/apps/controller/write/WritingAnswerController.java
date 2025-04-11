package com.apps.controller.write;

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
}