package com.apps.service;

import com.apps.mapper.UserAnswerDetailMapper;
import com.apps.mapper.UserAnswerRecordMapper;
import com.apps.model.UserAnswerDetail;
import com.apps.model.UserAnswerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Random;


@Service
public class AnswerService {

    @Autowired
    private UserAnswerRecordMapper userAnswerRecordMapper;

    @Autowired
    private UserAnswerDetailMapper userAnswerDetailMapper;

    /**
     * 保存答案列表
     * @param answersList 前端传过来的答案列表
     */
    public void saveAnswers(List<Map<String, Object>> answersList) {
        // 从列表的第一个元素中提取 recordId 和 detailId
        Map<String, Object> recordInfo = answersList.get(0);
        Long recordId = Long.valueOf(String.valueOf(recordInfo.get("COL_Answer_Record_ID")));
        Long detailId = Long.valueOf(String.valueOf(recordInfo.get("COL_Answer_Record_ID_son")));
        Long readingId = Long.valueOf(String.valueOf(recordInfo.get("COL_READING")));
        Long userId  = Long.valueOf(String.valueOf(recordInfo.get("UserId")));
        Timestamp creatAt = new Timestamp(System.currentTimeMillis()); // 当前时间的毫秒数

        // 插入到主表 COL_USER_ANSWER_RECORD 表
        UserAnswerRecord userAnswerRecord = new UserAnswerRecord();
         userAnswerRecord.setRecordId(recordId);
         userAnswerRecord.setUserId(Math.toIntExact(userId));
         userAnswerRecord.setReadingId(readingId);
         userAnswerRecord.setCreatedAt(creatAt);

   //     userAnswerRecord.setReadingId(readingId);
        // 设置其他字段（如 userId, readingId 等）根据实际需求
        userAnswerRecordMapper.insert(userAnswerRecord);


        // 插入到 COL_USER_ANSWER_DETAIL 表
        for (int i = 1; i < answersList.size(); i++) {
            Random random = new Random();
            long randomId = random.nextLong(); // 生成一个随机的 long 值
            Map<String, Object> answerInfo = answersList.get(i);
            UserAnswerDetail userAnswerDetail = new UserAnswerDetail();
            //主键，用随机生成的
            userAnswerDetail.setDetailId(randomId);
            userAnswerDetail.setRecordId(recordId);
            userAnswerDetail.setUserId(Math.toIntExact(userId));
            userAnswerDetail.setQuestionId(Long.valueOf((Integer) answerInfo.get("questionId")));
            userAnswerDetail.setSubmittedAnswer(String.valueOf(answerInfo.get("answer")));
            userAnswerDetail.setCreatedAt(creatAt);
            // 设置其他字段（如 userId, answerType, blankIndex 等）根据实际需求
            userAnswerDetailMapper.insert(userAnswerDetail);
        }
    }
}