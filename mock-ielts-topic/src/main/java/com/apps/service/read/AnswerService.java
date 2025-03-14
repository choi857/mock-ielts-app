package com.apps.service.read;

import com.apps.DTO.read.AnswerValidationDTO;
import com.apps.mapper.read.AnswerMapper;
import com.apps.mapper.read.UserAnswerDetailMapper;
import com.apps.mapper.read.UserAnswerRecordMapper;
import com.apps.model.read.UserAnswerDetail;
import com.apps.model.read.UserAnswerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;


@Service
public class AnswerService {

    @Autowired
    private UserAnswerRecordMapper userAnswerRecordMapper;

    @Autowired
    private UserAnswerDetailMapper userAnswerDetailMapper;

    @Autowired
    private AnswerMapper answerMapper;

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

        //如果主表有值就不用更新
        if(userAnswerRecordMapper.selectByPrimaryKey(recordId) == null) {
//            userAnswerRecordMapper.updateByPrimaryKey(userAnswerRecord);
            userAnswerRecordMapper.insert(userAnswerRecord);
        }

        List FILL_IN_THE_BLANK_ANSWER = new ArrayList<>();
        // 插入到 COL_USER_ANSWER_DETAIL 表
        for (int i = 1; i < answersList.size(); i++) {
            Random random = new Random();
            long randomId = random.nextLong(); // 生成一个随机的 long 值
            Map<String, Object> answerInfo = answersList.get(i);
            UserAnswerDetail userAnswerDetail = new UserAnswerDetail();

            //判断是否是填空题
            if ("FILL_IN_THE_BLANK".equals(answerInfo.get("answerType"))) {
                FILL_IN_THE_BLANK_ANSWER.add(answerInfo.get("answer"));
                if(FILL_IN_THE_BLANK_ANSWER.size() == 3) {
                    userAnswerDetail.setSubmittedAnswer(FILL_IN_THE_BLANK_ANSWER.toString());
                    userAnswerDetail.setDetailId(randomId);
                    userAnswerDetail.setRecordId(recordId);
                    userAnswerDetail.setUserId(Math.toIntExact(userId));
                    userAnswerDetail.setAnswerType(String.valueOf(answerInfo.get("answerType")));
                    userAnswerDetail.setQuestionId(Long.valueOf((Integer) answerInfo.get("questionId")));
                    userAnswerDetail.setCreatedAt(creatAt);
                    userAnswerDetailMapper.insert(userAnswerDetail);
                    FILL_IN_THE_BLANK_ANSWER.clear(); // 清空列表
                    continue;
                }else {
                    continue;
                }
            }


            //主键，用随机生成的
            userAnswerDetail.setDetailId(randomId);
            userAnswerDetail.setRecordId(recordId);
            userAnswerDetail.setUserId(Math.toIntExact(userId));
            userAnswerDetail.setAnswerType(String.valueOf(answerInfo.get("answerType")));
            userAnswerDetail.setQuestionId(Long.valueOf((Integer) answerInfo.get("questionId")));
            userAnswerDetail.setCreatedAt(creatAt);
            userAnswerDetail.setSubmittedAnswer(String.valueOf(answerInfo.get("answer")));


            // 设置其他字段（如 userId, answerType, blankIndex 等）
            userAnswerDetailMapper.insert(userAnswerDetail);
        }
    }
public void validateAnswers(Long recordId, Integer userId) {
    // 查询用户答题记录
    Map<String, Object> params = new HashMap<>();
    params.put("recordId", recordId);
    params.put("userId", userId);
    List<AnswerValidationDTO> answerDetails = userAnswerDetailMapper.selectUserAnswerDetailsByRecordIdAndUserId(params);

    // 校验答案并计算分数
    double totalScore = 0;
    Map<String, Integer> errorCountByType = new HashMap<>();
    for (AnswerValidationDTO detail : answerDetails) {
        boolean isCorrect = false;
        double totalScoreTemporary = 0;
        if ("FILL_IN_THE_BLANK".equals(detail.getAnswerType())) {
            // 从COL_ANSWER表中查询正确答案并按COL_BLANK_NUMBER排序
            List<String> correctAnswers = answerMapper.selectCorrectAnswersByQuestionId(detail.getQuestionId());
            // 将正确答案字符串分割成单个答案
            List<String> correctAnswerList = Arrays.asList(correctAnswers.get(0).split(",\\s*"));
            // 去掉首尾的方括号并分割字符串
            String submittedAnswerStr = detail.getSubmittedAnswer();
            if (submittedAnswerStr.startsWith("[") && submittedAnswerStr.endsWith("]")) {
                submittedAnswerStr = submittedAnswerStr.substring(1, submittedAnswerStr.length() - 1);
            }
            List<String> submittedAnswers = Arrays.asList(submittedAnswerStr.split(",\\s*"));

            // 比较两个List中的值
            if (correctAnswerList.size() == submittedAnswers.size()) {
                isCorrect = true;
                for (int i = 0; i < correctAnswerList.size(); i++) {
                    if (correctAnswerList.get(i).equals(submittedAnswers.get(i))) {
                        totalScoreTemporary += 1.0; // 每个正确答案的分数
                    } else {
                        isCorrect = false;
                    }
                }
            }

        } else {
            // 从COL_ANSWER表中查询正确答案，COL_IS_CORRECT为1
            String correctAnswer = answerMapper.selectCorrectAnswerByQuestionId(detail.getQuestionId());
            isCorrect = correctAnswer.equals(detail.getSubmittedAnswer());
            if (isCorrect) {
                totalScoreTemporary = 1.0; // 非填空题每题1分，选择和匹配题
            }
        }
        detail.setIsCorrect(isCorrect);
        if (isCorrect) {
            totalScore += totalScoreTemporary; // 累加临时分数
        } else {
            errorCountByType.put(detail.getAnswerType(), errorCountByType.getOrDefault(detail.getAnswerType(), 0) + 1);
        }
    }

    // 确定错误最多的题型
    String mostErrorType = null;
    int maxErrors = 0;
    for (Map.Entry<String, Integer> entry : errorCountByType.entrySet()) {
        if (entry.getValue() > maxErrors) {
            maxErrors = entry.getValue();
            mostErrorType = entry.getKey();
        }
    }

// 插入评语到answerEvaluation
    String evaluation = "未发现错误题数过多的题型。";
    if (mostErrorType != null) {
        Map<String, String> errorTypeEvaluationMap = new HashMap<>();
        errorTypeEvaluationMap.put("FILL_IN_THE_BLANK", "填空题错误最多。需要努力！");
        errorTypeEvaluationMap.put("MULTIPLE_CHOICE", "选择题错误最多。需要努力！");
        errorTypeEvaluationMap.put("MATCHING", "匹配题错误最多。需要努力！");
        //前面已经判断了mostErrorType不为空，这个可以直接get的，
        evaluation = errorTypeEvaluationMap.getOrDefault(mostErrorType, evaluation);
    }

    // 更新用户答题记录的评语
    Map<String, Object> scoreParams = new HashMap<>();
    scoreParams.put("recordId", recordId);
    scoreParams.put("score", totalScore);
    scoreParams.put("answerEvaluation", evaluation);
    userAnswerRecordMapper.updateScoreByRecordId(scoreParams);
}
}