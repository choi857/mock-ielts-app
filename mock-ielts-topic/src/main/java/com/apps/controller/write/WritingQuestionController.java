package com.apps.controller.write;

     import com.apps.DTO.write.WritingQuestionsRequest;
     import com.apps.model.write.WritingQuestion;
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
             return writingQuestionService.addWritingQuestions(
                        task1Title,
                        task1Requirements,
                        task2Title,
                        task2Requirements
             );
         }


    @GetMapping("/get/questions/{recordId}")
    public List<WritingQuestion> getWritingQuestionsByRecordId(@PathVariable Long recordId) {
        return writingQuestionService.getWritingQuestionsByRecordId(recordId);
    }

}