package com.apps.controller;

import com.apps.service.AnswerService;
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
    public void saveAnswers(@RequestBody List<Map<String, Object>> answersList) {
        answerService.saveAnswers(answersList);
    }
}