package com.apps.service.listen;

import com.apps.dto.listen.ListeningAnswerSubmissionDTO;
import com.apps.mapper.listen.ListeningAnswerMapper;
import com.apps.mapper.listen.ListeningUserAnswerMapper;
import com.apps.model.listen.ListeningAnswer;
import com.apps.model.listen.ListeningUserAnswerDetail;
import com.apps.model.listen.ListeningUserAnswerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 听力答案服务类
 */
@Service
public class ListeningAnswerService {
    private static final Logger logger = LoggerFactory.getLogger(ListeningAnswerService.class);

    @Autowired
    private ListeningUserAnswerMapper listeningUserAnswerMapper;
    @Autowired
    private ListeningAnswerMapper listeningAnswerMapper;

    /**
     * 提交答案
     * @param submission 提交的答案
     */
    @Transactional
    public void submitAnswers(ListeningAnswerSubmissionDTO submission) {
        if (submission == null || submission.getListening() == null || submission.getParts() == null) {
            throw new IllegalArgumentException("提交的答案数据不完整");
        }
        Random random = new Random();
        long randomId = random.nextLong(); // 生成一个随机的 long 值
        // 创建并保存答题记录
        ListeningUserAnswerRecord record = new ListeningUserAnswerRecord();
        record.setUserId(submission.getListening().getUserId());
        record.setListeningId(submission.getListening().getId());
        record.setCreatedAt(submission.getListening().getCreatedAt());
        record.setUpdatedAt(submission.getListening().getUpdatedAt());
        record.setId(randomId);
        listeningUserAnswerMapper.insertUserAnswerRecord(record);

        // 遍历并保存每个题目的答案
        for (Map.Entry<String, List<ListeningAnswerSubmissionDTO.QuestionWrapperDTO>> entry : submission.getParts().entrySet()) {
            String part = entry.getKey();
            List<ListeningAnswerSubmissionDTO.QuestionWrapperDTO> questions = entry.getValue();
            for (ListeningAnswerSubmissionDTO.QuestionWrapperDTO question : questions) {
                ListeningUserAnswerDetail detail = new ListeningUserAnswerDetail();
                detail.setRecordId(record.getId());
                detail.setUserId(submission.getListening().getUserId());
                detail.setQuestionId(question.getQuestion().getId());
                detail.setAnswerType(question.getQuestion().getType());
                detail.setSubmittedAnswer(question.getQuestion().getAnswers());
                detail.setCreatedAt(question.getQuestion().getCreatedAt());
                detail.setPart(part);
                listeningUserAnswerMapper.insertUserAnswerDetail(detail);
            }
        }
        // 计算得分和评价
         calculateScoreAndEvaluation(record);
    }



    /**
     * 计算得分和评价
     * @param record 答题记录
     */
    private void calculateScoreAndEvaluation(ListeningUserAnswerRecord record) {
        // 获取用户答案
        List<ListeningUserAnswerDetail> details = listeningUserAnswerMapper.findUserAnswerDetailsByRecordId(record.getId());
        int totalQuestions = details.size();
        int correctAnswers = 0;

        // 统计错误题型
        int fillInTheBlankErrors = 0;
        int singleChoiceErrors = 0;
        int matchingErrors = 0;

        for (ListeningUserAnswerDetail detail : details) {
            logger.debug("问题id-------" + detail.getQuestionId());
            // 获取正确答案
            ListeningAnswer correctAnswer = listeningAnswerMapper.findCorrectAnswerByQuestionId(detail.getQuestionId());
            if (correctAnswer != null && correctAnswer.getContent().equals(detail.getSubmittedAnswer())) {
                detail.setIsCorrect(true);
                correctAnswers++;
            } else {
                detail.setIsCorrect(false);
                // Count errors based on question type
                switch (detail.getAnswerType()) {
                    case "FILL_IN_THE_BLANK":
                        fillInTheBlankErrors++;
                        break;
                    case "SINGLE_CHOICE":
                        singleChoiceErrors++;
                        break;
                    case "MATCHING":
                        matchingErrors++;
                        break;
                }
            }
            // 更新答案是否正确的字段
            listeningUserAnswerMapper.updateUserAnswerDetail(detail);
        }

     //   double score = (double) correctAnswers / totalQuestions * 100;
        double score = (double) correctAnswers;
        // 计算错误最多的题型
        String evaluation;
        if (fillInTheBlankErrors >= singleChoiceErrors && fillInTheBlankErrors >= matchingErrors) {
            evaluation = "填空题错误最多，需要加强填空题的练习";
        } else if (singleChoiceErrors >= fillInTheBlankErrors && singleChoiceErrors >= matchingErrors) {
            evaluation = "单选题错误最多，需要加强，多做单选题";
        } else {
            evaluation = "配对题错误最多，需要加强，要理解配对题的图片或者内容意思";
        }

        record.setScore(score);
        record.setAnswerEvaluation(evaluation);
        listeningUserAnswerMapper.updateUserAnswerRecord(record);
    }
}