package com.apps.controller.write;

     import com.apps.dto.write.WritingQuestionsRequest;
     import com.apps.model.write.WritingQuestion;
     import com.apps.model.write.WritingTaskRecord;
     import com.apps.service.write.WritingAnswerServiceToAi;
     import com.apps.service.write.WritingQuestionService;
     import org.springframework.beans.factory.annotation.Autowired;
     import org.springframework.web.bind.annotation.*;

     import java.util.List;
     import java.util.Optional;

@RestController
     @RequestMapping("/topic/writing")
     public class WritingQuestionController {

         @Autowired
         private WritingQuestionService writingQuestionService;

         /**
          * 新增写作题
          *   task1Title Task1的标题
          *   task1Requirements Task1的具体内容
          *   task2Title Task2的标题
          *   task2Requirements Task2的具体内容
          * @return 新增结果
          */
         @PostMapping("/save")
         public boolean addWritingQuestions(@RequestBody WritingQuestionsRequest request) {
             String task1Title = Optional.ofNullable(request.getTask1Title()).orElse("");
             String task1Requirements = Optional.ofNullable(request.getTask1Requirements()).orElse("");
             String task2Title = Optional.ofNullable(request.getTask2Title()).orElse("");
             String task2Requirements = Optional.ofNullable(request.getTask2Requirements()).orElse("");
             String colTitle = Optional.ofNullable(request.getColTitle()).orElse("");
             String taskDescription1 = Optional.ofNullable(request.getTaskDescription1()).orElse("");
             String taskDescription2 = Optional.ofNullable(request.getTaskDescription2()).orElse("");
             return writingQuestionService.addWritingQuestions(
                        task1Title,
                        task1Requirements,
                        taskDescription1,
                        task2Title,
                        task2Requirements,
                         taskDescription2,
                         colTitle
             );
         }

    /**
     * 根据题目主ID获取写作题目详情
     * @param recordId 记录ID
     * @return 写作题目列表
     */
    @GetMapping("/get/questions/{recordId}")
    public List<WritingQuestion> getWritingQuestionsByRecordId(@PathVariable Long recordId) {
        return writingQuestionService.getWritingQuestionsByRecordId(recordId);
    }

    /**
     * 根据 task_id 获取写作题目详情
     * @param taskId 写作题目ID
     * @return 写作题目详情
     */
    @GetMapping("/get/question/{taskId}")
    public WritingQuestion getWritingQuestionById(@PathVariable Long taskId) {
        return writingQuestionService.getWritingQuestionById(taskId);
    }
    @Autowired
    private WritingAnswerServiceToAi writingAnswerServiceToAi;

    @GetMapping("/update/scores")
    public void updateScores() {
        writingAnswerServiceToAi.updateScoresInterface();
    }

    /**
     * 获取所有 WritingTaskRecord 记录
     * @return 所有 WritingTaskRecord 记录
     */
    @GetMapping("/all")
    public List<WritingTaskRecord> getAllWritingTaskRecords() {
        return writingQuestionService.getAllWritingTaskRecords();
    }

}