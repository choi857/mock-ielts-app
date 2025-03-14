package com.apps.service.write;

          import com.apps.mapper.write.WritingAnswerDetailMapper;
          import com.apps.mapper.write.WritingAnswerRecordMapper;
          import com.apps.mapper.write.WritingTaskRecordMapper;
          import com.apps.model.write.WritingAnswerDetail;
          import com.apps.model.write.WritingAnswerRecord;
          import com.apps.model.write.WritingTaskRecord;
          import org.springframework.beans.factory.annotation.Autowired;
          import org.springframework.stereotype.Service;

          import java.sql.Timestamp;
          import java.util.List;
          import java.util.Map;
          import java.util.Random;

          @Service
          public class WritingAnswerService {

              @Autowired
              private WritingAnswerRecordMapper writingAnswerRecordMapper;

              @Autowired
              private WritingAnswerDetailMapper writingAnswerDetailMapper;

              @Autowired
              private WritingTaskRecordMapper writingTaskRecordMapper;

              /**
               * 保存写作答案
               * @param answersList 前端传过来的答案列表
               */
              public void saveWritingAnswers(List<Map<String, Object>> answersList) {
                  // 从列表的第一个元素中提取 recordId 和 userId
                  Map<String, Object> recordInfo = answersList.get(0);
                  Long recordId = Long.valueOf(String.valueOf(recordInfo.get("recordId")));
                  Long userId = Long.valueOf(String.valueOf(recordInfo.get("userId")));
                  Timestamp createdAt = new Timestamp(System.currentTimeMillis());

                  // 查询 task1Id 和 task2Id
                  WritingTaskRecord taskRecord = writingTaskRecordMapper.selectByRecordId(recordId);
                  if (taskRecord == null) {
                      throw new IllegalArgumentException("Record not found for id: " + recordId);
                  }
                  Long task1Id = taskRecord.getTask1Id();
                  Long task2Id = taskRecord.getTask2Id();

                  // 插入到主表 COL_WRITE_USER_ANSWER_RECORD 表
                  WritingAnswerRecord writingAnswerRecord = new WritingAnswerRecord();
                  writingAnswerRecord.setRecordId(recordId);
                  writingAnswerRecord.setUserId(userId);
                  writingAnswerRecord.setTask1Id(task1Id);
                  writingAnswerRecord.setTask2Id(task2Id);
                  writingAnswerRecord.setCreatedAt(createdAt);
                  writingAnswerRecord.setUpdatedAt(createdAt);

                  // 如果主表有值就不用更新
                  if (writingAnswerRecordMapper.selectByPrimaryKey(recordId) == null) {
                      writingAnswerRecordMapper.insert(writingAnswerRecord);
                  }

                  // 插入到 COL_WRITE_USER_ANSWER_DETAIL 表
                  for (int i = 1; i < answersList.size(); i++) {
                      Random random = new Random();
                      long detailId = random.nextLong(); // 生成一个随机的 long 值
                      Map<String, Object> answerInfo = answersList.get(i);
                      WritingAnswerDetail writingAnswerDetail = new WritingAnswerDetail();

                      writingAnswerDetail.setDetailId(detailId);
                      writingAnswerDetail.setRecordId(recordId);
                      writingAnswerDetail.setUserId(userId);
                      writingAnswerDetail.setTaskType(String.valueOf(answerInfo.get("taskType")));
                      writingAnswerDetail.setAnswerContent(String.valueOf(answerInfo.get("answerContent")));
                      writingAnswerDetail.setCreatedAt(createdAt);

                      writingAnswerDetailMapper.insert(writingAnswerDetail);
                  }
              }
          }