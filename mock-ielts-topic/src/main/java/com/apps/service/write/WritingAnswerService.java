package com.apps.service.write;

          import com.apps.common.CreateId;
          import com.apps.common.SparkModelUtil;
          import com.apps.dto.write.WritingAnswerRecordDTO;
          import com.apps.dto.write.WritingAnswerRecordDetailDTO;
          import com.apps.mapper.write.WritingAnswerDetailMapper;
          import com.apps.mapper.write.WritingAnswerRecordMapper;
          import com.apps.mapper.write.WritingTaskRecordMapper;
          import com.apps.model.write.WritingAnswerDetail;
          import com.apps.model.write.WritingAnswerRecord;
          import com.apps.model.write.WritingTaskRecord;
          import org.springframework.beans.factory.annotation.Autowired;
          import org.springframework.stereotype.Service;
          import org.springframework.transaction.annotation.Transactional;

          import java.sql.Timestamp;
          import java.util.HashMap;
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

                  Long recordId = Long.valueOf(new CreateId().generateId());
                  Long userId = Long.valueOf(String.valueOf(recordInfo.get("userId")));
                  Timestamp createdAt = new Timestamp(System.currentTimeMillis());
                  Long writerId = Long.valueOf(String.valueOf(recordInfo.get("recordId")));
                  // 查询 task1Id 和 task2Id
                  WritingTaskRecord taskRecord = writingTaskRecordMapper.selectByRecordId(writerId);
                  if(taskRecord == null){
                      throw new RuntimeException("查不到对应的taskId");
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

                      writingAnswerRecordMapper.insert(writingAnswerRecord);


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



              // 封装方法，接收topic和userWriterContent作为参数
              public static Map<String, String> assessIELTS(String topic, String answerContent) {
                  // 创建SparkModelUtil实例
                  SparkModelUtil sparkModel = SparkModelUtil.builder()
                          .appId("b2431dd3") // 这里填写控制台的appid
                          .apiKey("ca4fb6c939220ad1a715e995cef83a05") // 这里填写控制台的apiKey
                          .apiSecret("NGM1NDc1YjYzY2E2OTRmMWJhNGRkYjQz") // 这里填写控制台的apiSecret
                          .build();

                  // 系统角色设定
                  String systemContent = "你是一位专业的雅思写作评分专家，你的任务是根据雅思写作评分标准对考生的作文进行评分。回答的内容只需要以键值对的方式：键和值之间用等号（=）分隔，键值对之间用,分割，例如key=value,key2=value2。输出总分\"score\",作文评价\"answerEvaluation\"。不需要多余的任何内容且score为数字类型，只需要输出对应的数字分数";

                  // 构建用户输入内容
                  String userContent = "请评价以下作文：\n\n" +
                          "题目：" + topic +
                          "作文：\n" + answerContent;

                  // 获取AI回答
                  String answer = null;
                  try {
                      answer = sparkModel.chat(systemContent, userContent);
                  } catch (Exception e) {
                      throw new RuntimeException(e);
                  }

                  // 解析AI回答，提取score和answerEvaluation
                  Map<String, String> result = new HashMap<>();
                  String[] keyValuePairs = answer.split(",");
                  for (String pair : keyValuePairs) {
                      String[] keyValue = pair.split("=", 2);
                      if (keyValue.length == 2) {
                          result.put(keyValue[0].trim(), keyValue[1].trim());
                      }
                  }

                  return result;
              }



              /**
               * 查询用户ID对应的答题主记录
               * @param userId 用户ID
               */
              @Transactional(readOnly = true)
              public List<WritingAnswerRecordDTO> getUserWritingAnswerRecords(Long userId) {
                  return writingAnswerRecordMapper.selectUserWritingAnswerRecordsByUserId(userId);
              }


              /**
               * 根据 userId 和 record_id 查询答题详细信息
               * @param userId 用户ID
               * @param recordId 记录ID
               * @return WritingAnswerRecordDTO 对象
               */
              @Transactional(readOnly = true)
              public WritingAnswerRecordDetailDTO getWritingAnswerRecordByUserIdAndRecordId(Long userId, Long recordId) {
                  // 查询主记录
                  WritingAnswerRecord record = writingAnswerRecordMapper.selectByUserIdAndRecordId(userId, recordId);
                  if (record == null) {
                      return null;
                  }

                  // 创建DTO对象
                  WritingAnswerRecordDetailDTO dto = new WritingAnswerRecordDetailDTO();
                  dto.setRecordId(record.getRecordId());
                  dto.setUserId(record.getUserId());
                  dto.setTask1Id(record.getTask1Id());
                  dto.setTask2Id(record.getTask2Id());
                  dto.setCreatedAt(record.getCreatedAt());
                  dto.setUpdatedAt(record.getUpdatedAt());
                  dto.setTotalScore(record.getTotalScore());

                  // 查询明细记录
                  List<WritingAnswerDetail> details = writingAnswerDetailMapper.selectByRecordId(record.getRecordId());
                  dto.setDetails(details);

                  return dto;
              }


          }