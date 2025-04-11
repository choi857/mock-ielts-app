package com.apps.service.read;

import com.apps.common.CreateId;
import com.apps.dto.read.AnswerValidationDTO;
import com.apps.dto.read2.AnswerRecordDTO;
import com.apps.dto.read2.UserAnswerDetailDTO;
import com.apps.mapper.read.AnswerMapper;
import com.apps.mapper.read.QuestionMapper;
import com.apps.mapper.read.UserAnswerDetailMapper;
import com.apps.mapper.read.UserAnswerRecordMapper;
import com.apps.model.read.Question;
import com.apps.model.read.UserAnswerDetail;
import com.apps.model.read.UserAnswerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
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

    @Autowired
    private QuestionMapper questionMapper;

    /**
     * 保存答案列表
     * @param answersList 前端传过来的答案列表
     */
    @Transactional
    public Map<String, Long> saveAnswers(List<Map<String, Object>> answersList) {
        // 从列表的第一个元素中提取 recordId 和 detailId
        if(answersList==null){
            System.out.println("答案列表为空 is null");
            return null;
        }
        Map<String, Object> recordInfo = answersList.get(0);
        long recordId = new CreateId().generateId();
        long recordSonId = new CreateId().generateId();
    //    Long recordId = Long.valueOf(String.valueOf(recordInfo.get("COL_Answer_Record_ID")));
    //    Long detailId = Long.valueOf(String.valueOf(recordInfo.get("COL_Answer_Record_ID_son")));
        Long readingId = Long.valueOf(String.valueOf(recordInfo.get("COL_READING")));
        Long userId  = Long.valueOf(String.valueOf(recordInfo.get("UserId")));
        Long summaryId = Long.valueOf(String.valueOf(recordInfo.get("COL_READING_SUMMARY_ID")));
        Timestamp creatAt = new Timestamp(System.currentTimeMillis()); // 当前时间的毫秒数

        // 插入到主表 COL_USER_ANSWER_RECORD 表
        UserAnswerRecord userAnswerRecord = new UserAnswerRecord();
         userAnswerRecord.setRecordId(recordId);
         userAnswerRecord.setReadSummaryId(summaryId);
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


        // 插入到 COL_USER_ANSWER_DETAIL 表
        for (int i = 1; i < answersList.size(); i++) {

            long randomId = new CreateId().generateId();; // 生成一个随机的 long 值
            Map<String, Object> answerInfo = answersList.get(i);
            UserAnswerDetail userAnswerDetail = new UserAnswerDetail();

            //主键，用随机生成的
            userAnswerDetail.setDetailId(randomId);
            userAnswerDetail.setRecordId(recordId);
            userAnswerDetail.setUserId(Math.toIntExact(userId));
            userAnswerDetail.setAnswerType(String.valueOf(answerInfo.get("answerType")));
            userAnswerDetail.setQuestionId(Long.valueOf( String.valueOf(answerInfo.get("questionId"))));
            userAnswerDetail.setCreatedAt(creatAt);
            userAnswerDetail.setSubmittedAnswer(String.valueOf(answerInfo.get("answer")));

            // 设置其他字段（如 userId, answerType, blankIndex 等）
            userAnswerDetailMapper.insert(userAnswerDetail);
        }
        HashMap<String, Long> stringLongHashMap = new HashMap<>();
        stringLongHashMap.put("readSummaryId",summaryId);
        stringLongHashMap.put("userId",userId);

        return stringLongHashMap;
    }

    /**
     * 校验答案并更新分数,设置评语
     * @param readSummaryId
     * @param userId
     */
    @Transactional
    public void validateAnswers(Long readSummaryId, Long userId) {
        // 查询用户答题记录列表
        List<UserAnswerRecord> userAnswerRecords = userAnswerRecordMapper.selectUserAnswerRecordsByReadSummaryId(readSummaryId);


        for (UserAnswerRecord userAnswerRecord : userAnswerRecords) {
            //答案主表
            Long recordId = userAnswerRecord.getRecordId();

            // 查询用户答题记录的初始值
            UserAnswerRecord initialRecord = userAnswerRecordMapper.selectByPrimaryKey(recordId);
            double totalScore = initialRecord.getScore() != null ? initialRecord.getScore() : 0.0;
            String eva = "第一部分: ";
            String evaluation = initialRecord.getAnswerEvaluation() != null ? initialRecord.getAnswerEvaluation() : eva;
            if(eva.equals(evaluation)){
                evaluation = evaluation+" 第二三Part部分: ";
            }
            // 查询用户答题详情
            Map<String, Object> params = new HashMap<>();
            params.put("recordId", recordId);
            params.put("userId", userId);
            List<AnswerValidationDTO> answerDetails = userAnswerDetailMapper.selectUserAnswerDetailsByRecordIdAndUserId(params);

            // 校验答案并计算分数
            Map<String, Integer> errorCountByType = new HashMap<>();
            for (AnswerValidationDTO detail : answerDetails) {
                boolean isCorrect = false;
                double totalScoreTemporary = 0;
                if ("FILL_IN_THE_BLANK".equals(detail.getAnswerType())) {
                    // 从COL_ANSWER表中查询正确答案并按COL_BLANK_NUMBER排序
                    Question question  =   questionMapper.selectplaceholderFormatAnswersByQuestionId(detail.getQuestionId());
                     String correctAnswers = question.getPlaceholderFormat();
                //    String correctAnswers = answerMapper.selectCorrectAnswersByQuestionId(detail.getQuestionId());
                    if(correctAnswers == null || correctAnswers.isEmpty()){
                        isCorrect = true;
                        totalScoreTemporary += 1.0;
                    }
                    else {
                        // 去掉首尾的花括号
                        while (correctAnswers.startsWith("{") && correctAnswers.endsWith("}")) {
                            correctAnswers = correctAnswers.substring(1, correctAnswers.length() - 1).trim();
                        }


                        // 获取提交的答案
                        String submittedAnswerStr = detail.getSubmittedAnswer();

                        // 去掉提交答案的首尾方括号（如果有的话）
                        if (submittedAnswerStr.startsWith("{") && submittedAnswerStr.endsWith("}")) {
                            submittedAnswerStr = submittedAnswerStr.substring(1, submittedAnswerStr.length() - 1);
                        }

                        // 直接比较两个字符串
                        if (correctAnswers.equals(submittedAnswerStr)) {
                            isCorrect = true;
                            totalScoreTemporary += 1.0; // 每个正确答案的分数
                        } else {
                            isCorrect = false;
                        }
                    }
                }
            else {
                    // 从COL_ANSWER表中查询正确答案，COL_IS_CORRECT为1
                    String correctAnswer = answerMapper.selectmatchingKeyAnswerByQuestionId(detail.getQuestionId());
                    //判空，防止空指针
                    String answer = detail.getSubmittedAnswer();
                    String submittedAnswer = (answer != null && !answer.isEmpty()) ? answer : "1";

                    isCorrect = submittedAnswer.equals(correctAnswer);
                    if (isCorrect) {
                        totalScoreTemporary = 1.0; // 非填空题每题1分，选择和匹配题
                    }
                    else {
                        System.out.println("错误答案：" + submittedAnswer + " 对应的答案：" + correctAnswer);
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
            String newEvaluation = "";
            if (mostErrorType != null) {
                Map<String, String> errorTypeEvaluationMap = new HashMap<>();
                errorTypeEvaluationMap.put("FILL_IN_THE_BLANK", "填空题错误最多。需要努力！");
                errorTypeEvaluationMap.put("MULTIPLE_CHOICE", "选择题错误最多。需要努力！");
                errorTypeEvaluationMap.put("MATCHING", "匹配题错误最多。需要努力！");
                newEvaluation = errorTypeEvaluationMap.getOrDefault(mostErrorType, "");
            }
            else {
                newEvaluation = "恭喜你，没有错误。";
            }

            // 追加新的评语到原有的评语
            if (!newEvaluation.isEmpty()) {
                if (!evaluation.isEmpty()) {
                    evaluation += " ";
                }
                evaluation += newEvaluation;
            }

            // 更新用户答题记录的评语
            Map<String, Object> scoreParams = new HashMap<>();
            scoreParams.put("recordId", recordId);
            scoreParams.put("score", totalScore);
            evaluation = totalScore ==0.00 ? "很遗憾，没有正确回答任何问题。" : evaluation;
            evaluation = totalScore ==40.0 ? "很棒，居然全对！" : evaluation;
            scoreParams.put("answerEvaluation", evaluation);
            userAnswerRecordMapper.updateScoreByRecordId(scoreParams);
        }
    }


    /**
     * 查询用户所有的主答案列表
     * @param userId 用户ID
     */
    @Transactional(readOnly = true)
    public List<AnswerRecordDTO> getUserAnswerRecords(Long userId) {
        List<UserAnswerRecord> userAnswerRecords = userAnswerRecordMapper.selectUserAnswerRecordsByUserId(userId);
        List<AnswerRecordDTO> answerRecordDTOs = new ArrayList<>();

        for (UserAnswerRecord record : userAnswerRecords) {
            AnswerRecordDTO dto = new AnswerRecordDTO();
            dto.setRecordId(record.getRecordId());
            dto.setUserId(record.getUserId());
            dto.setReadingId(record.getReadingId());
            dto.setScore(record.getScore());
            dto.setDurationSeconds(record.getDurationSeconds());
            dto.setDeviceType(record.getDeviceType());
            dto.setCreatedAt(record.getCreatedAt());
            dto.setUpdatedAt(record.getUpdatedAt());
            dto.setAnswerEvaluation(record.getAnswerEvaluation());
            dto.setReadSummaryId(record.getReadSummaryId());

            // 查询明细答题内容
            List<UserAnswerDetail> details = userAnswerDetailMapper.selectByRecordId(record.getRecordId());
            List<UserAnswerDetailDTO> detailDTOs = new ArrayList<>();
            for (UserAnswerDetail detail : details) {
                UserAnswerDetailDTO detailDTO = new UserAnswerDetailDTO();
                detailDTO.setDetailId(detail.getDetailId());
                detailDTO.setRecordId(detail.getRecordId());
                detailDTO.setUserId(Long.valueOf(detail.getUserId()));
                detailDTO.setQuestionId(detail.getQuestionId());
                detailDTO.setAnswerType(detail.getAnswerType());
                detailDTO.setSubmittedAnswer(detail.getSubmittedAnswer());
                detailDTO.setIsCorrect(detail.getIsCorrect());
                detailDTO.setBlankIndex(detail.getBlankIndex());
                detailDTO.setCreatedAt(detail.getCreatedAt());
                detailDTOs.add(detailDTO);
            }
            dto.setAnswerDetails(detailDTOs);
            answerRecordDTOs.add(dto);
        }

        return answerRecordDTOs;
    }

    /**
     * 根据主答案表的ID查询用户明细答题的内容
     * @param recordId 主答案表的ID
     */
    @Transactional(readOnly = true)
    public AnswerRecordDTO getUserAnswerRecordDetails(Long recordId) {
        UserAnswerRecord record = userAnswerRecordMapper.selectByPrimaryKey(recordId);
        if (record == null) {
            return null;
        }

        AnswerRecordDTO dto = new AnswerRecordDTO();
        dto.setRecordId(record.getRecordId());
        dto.setUserId(record.getUserId());
        dto.setReadingId(record.getReadingId());
        dto.setScore(record.getScore());
        dto.setDurationSeconds(record.getDurationSeconds());
        dto.setDeviceType(record.getDeviceType());
        dto.setCreatedAt(record.getCreatedAt());
        dto.setUpdatedAt(record.getUpdatedAt());
        dto.setAnswerEvaluation(record.getAnswerEvaluation());
        dto.setReadSummaryId(record.getReadSummaryId());

        // 查询明细答题内容
        List<UserAnswerDetail> details = userAnswerDetailMapper.selectByRecordId(record.getRecordId());
        List<UserAnswerDetailDTO> detailDTOs = new ArrayList<>();
        for (UserAnswerDetail detail : details) {
            UserAnswerDetailDTO detailDTO = new UserAnswerDetailDTO();
            detailDTO.setDetailId(detail.getDetailId());
            detailDTO.setRecordId(detail.getRecordId());
            detailDTO.setUserId(Long.valueOf(detail.getUserId()));
            detailDTO.setQuestionId(detail.getQuestionId());
            detailDTO.setAnswerType(detail.getAnswerType());
            detailDTO.setSubmittedAnswer(detail.getSubmittedAnswer());
            detailDTO.setIsCorrect(detail.getIsCorrect());
            detailDTO.setBlankIndex(detail.getBlankIndex());
            detailDTO.setCreatedAt(detail.getCreatedAt());
            detailDTOs.add(detailDTO);
        }
        dto.setAnswerDetails(detailDTOs);

        return dto;
    }

}