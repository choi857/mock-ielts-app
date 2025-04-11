package com.apps.service.write;

         import com.apps.mapper.write.WritingQuestionMapper;
         import com.apps.mapper.write.WritingTaskRecordMapper;
         import com.apps.model.write.WritingQuestion;
         import com.apps.model.write.WritingTaskRecord;
         import org.springframework.beans.factory.annotation.Autowired;
         import org.springframework.stereotype.Service;
         import java.sql.Timestamp;
         import java.util.Arrays;
         import java.util.List;
         import java.util.Random;

         @Service
         public class WritingQuestionService {

             @Autowired
             private WritingQuestionMapper writingQuestionMapper;

             @Autowired
             private WritingTaskRecordMapper writingTaskRecordMapper;

             /**
              * 插入新的写作题
              * @param task1Title Task1的标题
              * @param task1Requirements Task1的具体内容
              * @param task2Title Task2的标题
              * @param task2Requirements Task2的具体内容
              * @return 插入结果
              */
             public boolean addWritingQuestions(String task1Title, String task1Requirements, String task2Title, String task2Requirements) {
                 if (task1Title == null || task1Title.isEmpty() || task1Requirements == null || task1Requirements.isEmpty() ||
                     task2Title == null || task2Title.isEmpty() || task2Requirements == null || task2Requirements.isEmpty()) {
                     throw new IllegalArgumentException("写作题目标题和内容不能为null，可以为空字符");
                 }

                 Timestamp currentTime = new Timestamp(System.currentTimeMillis());

                 Random random = new Random();
                 long task1Id = random.nextLong(); // 生成一个随机的 long 值
                 long task2Id = random.nextLong(); // 生成一个随机的 long 值

                 WritingQuestion task1 = new WritingQuestion();
                 task1.setTaskId(task1Id);
                 task1.setTaskType("TASK1");
                 task1.setTaskTitle(task1Title);
                 task1.setTaskRequirements(task1Requirements);
                 task1.setCreatedAt(currentTime);
                 task1.setUpdatedAt(currentTime);

                 WritingQuestion task2 = new WritingQuestion();
                 task2.setTaskId(task2Id);
                 task2.setTaskType("TASK2");
                 task2.setTaskTitle(task2Title);
                 task2.setTaskRequirements(task2Requirements);
                 task2.setCreatedAt(currentTime);
                 task2.setUpdatedAt(currentTime);

                 writingQuestionMapper.insert(task1);
                 writingQuestionMapper.insert(task2);

                 WritingTaskRecord record = new WritingTaskRecord();
                 record.setTask1Id(task1Id);
                 record.setTask2Id(task2Id);
                 record.setCreatedAt(currentTime);
                 record.setUpdatedAt(currentTime);

                 writingTaskRecordMapper.insert(record);

                 return true;
             }



             public List<WritingQuestion> getWritingQuestionsByRecordId(Long recordId) {
                 WritingTaskRecord record = writingTaskRecordMapper.selectByRecordId(recordId);
                 if (record == null) {
                     throw new IllegalArgumentException("Record not found for id: " + recordId);
                 }
                 List<Long> taskIds = Arrays.asList(record.getTask1Id(), record.getTask2Id());
                 return writingQuestionMapper.selectByTaskIds(taskIds);
             }

             /**
              * 根据 task_id 获取写作题目详情
              * @param taskId 写作题目ID
              * @return 写作题目详情
              */
             public WritingQuestion getWritingQuestionById(Long taskId) {
                 WritingQuestion question = writingQuestionMapper.selectById(taskId);
                 if (question == null) {
                     throw new IllegalArgumentException("Writing question not found for id: " + taskId);
                 }
                 return question;
             }
          }