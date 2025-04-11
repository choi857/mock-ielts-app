package com.apps.controller.read;

import com.apps.common.ResponseResult;
import com.apps.dto.read2.ReadingInsertDTO;
import com.apps.mapper.read.ReadingMapper;
import com.apps.model.read.ReadingSummary;
import com.apps.service.read.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topic/reading")
public class ReadingController {

    @Autowired
    private ReadingService readingService;

    @Autowired
    private ReadingMapper readingMapper;

    /**
     * 插入阅读材料及其题目和答案
     * @param readingInsertDTO 包含阅读材料、题目和答案的 DTO
     * @return 插入结果
     */
    @PostMapping("/add")
    public ResponseResult<Void> addReadingWithQuestionsAndAnswers(@RequestBody ReadingInsertDTO readingInsertDTO) {
        try {
            // 调用服务层方法插入阅读材料及其题目和答案
            readingService.insertReadingWithQuestions(readingInsertDTO);
            return ResponseResult.success("阅读材料添加成功");
        } catch (Exception e) {
            // 捕获异常并返回失败信息
            return ResponseResult.fail("添加阅读材料失败，异常为: " + e.getMessage());
        }
    }

    /**
     * 根据阅读材料 ID 获取阅读材料及其题目和答案
     * @param id 阅读材料 ID
     * @return 阅读材料及其题目和答案
     */
    @GetMapping("/{id}")
    public ResponseResult<ReadingInsertDTO> getReadingWithQuestionsAndAnswers(@PathVariable Long id) {
        try {
            // 调用服务层方法根据 ID 获取阅读材料及其题目和答案
            ReadingInsertDTO readingInsertDTO = readingService.getReadingSummaryById(id);
            return ResponseResult.success("查询成功", readingInsertDTO);
        } catch (Exception e) {
            // 捕获异常并返回失败信息
            return ResponseResult.fail("查询阅读材料失败，异常为: " + e.getMessage());
        }
    }

/**
 * 查询所有阅读汇总，包括阅读汇总ID 和标题
 * @return 所有阅读汇总及其 ID 和标题
 */
@GetMapping("/all")
public ResponseResult<List<ReadingSummary>> getAllReadingIDs() {
    try {
        // 调用服务层方法获取所有阅读汇总
        List<ReadingSummary> readingSummaries = readingService.getAllReadingSummary();
        return ResponseResult.success("查询成功", readingSummaries);
    } catch (Exception e) {
        // 捕获异常并返回失败信息
        return ResponseResult.fail("查询所有阅读汇总失败，异常为:  " + e.getMessage());
    }
}

    /**
     * 根据阅读材料 ID 删除阅读材料及其题目和答案
     * @param id 阅读材料 ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteReadingAndQuestions(@PathVariable Long id) {
        try {
            // 调用服务层方法根据 ID 删除阅读材料及其题目和答案
            readingService.deleteReadingSummaryById(id);
            return ResponseResult.success("阅读材料及其题目删除成功");
        } catch (Exception e) {
            // 捕获异常并返回失败信息
            return ResponseResult.fail("删除阅读材料失败，异常为: " + e.getMessage());
        }
    }
}