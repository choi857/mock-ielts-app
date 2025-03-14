package com.apps.controller.read;

import com.apps.common.ResponseResult;
import com.apps.model.read.Reading;
import com.apps.service.read.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topic/reading")
public class ReadingController {

    @Autowired
    private ReadingService readingService;

    /**
     * 获取阅读材料及题目
     *
     * @param id
     * @return
     */
    @GetMapping("user/{id}")
    public ResponseResult<Reading> getReadingWithQuestions(@PathVariable Long id) {
        // 调用服务层方法获取数据
       // ReadingWithQuestionsDTO dto = readingService.getReadingWithQuestions(id);
        Reading dto = readingService.getReadingWithQuestionsAndAnswers(id);
        // 返回响应
        return ResponseResult.success("成功",dto);
    }

    /**
     * 插入阅读材料及其题目和答案
     * @param reading
     * @return
     */
    @PostMapping("admin/add")
    public ResponseResult<String> addReadingWithQuestionsAndAnswers(@RequestBody Reading reading) {
        // 调用服务层方法插入数据
        return   readingService.addReadingWithQuestionsAndAnswers(reading);
        // 返回响应
     //   return ResponseResult.success("阅读题新增成功");
    }

    @DeleteMapping("admin/delete/{id}")
    public ResponseResult<String> deleteReadingWithQuestionsAndAnswers(@PathVariable Long id) {
        return readingService.deleteReadingWithQuestionsAndAnswers(id);
    }

    @GetMapping("/test")
    public ResponseResult<String> test() {
        return ResponseResult.success("Test successful");
    }



}