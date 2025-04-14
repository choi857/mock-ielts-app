package com.apps.service.speak;

import com.apps.common.CreateId;
import com.apps.common.SpeakToAiUtil;
import com.apps.dto.speak.SpeakUserAnswerDTO;
import com.apps.dto.speak.SpeakingAnswerRecordDTO;
import com.apps.mapper.speak.SpeakUserAnswerDetailMapper;
import com.apps.mapper.speak.SpeakUserAnswerRecordMapper;
import com.apps.model.speak.SpeakUserAnswerDetail;
import com.apps.model.speak.SpeakUserAnswerRecord;
import com.apps.service.speak.impl.SpeakUserAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 用户口语答题服务实现类
 * 实现保存和查询用户答题记录的功能
 */
@Service
public class SpeakUserAnswerServiceImpl implements SpeakUserAnswerService {

    @Autowired
    private SpeakUserAnswerRecordMapper recordMapper;

    @Autowired
    private SpeakUserAnswerDetailMapper detailMapper;

    @Value("${file.upload.base-dir}")
    private String baseDir;

    @Value("${file.upload.url-prefix}")
    private String urlPrefix;

    @Override
    @Transactional
    public void saveUserAnswer(SpeakUserAnswerDTO speakUserAnswerDTO) {
        if (speakUserAnswerDTO == null || speakUserAnswerDTO.getSpeakUserAnswerRecord() == null) {
            throw new IllegalArgumentException("用户答题数据不能为空");
        }

        // 保存主记录
        SpeakUserAnswerRecord record = new SpeakUserAnswerRecord();
        SpeakUserAnswerDTO.SpeakUserAnswerRecord dtoRecord = speakUserAnswerDTO.getSpeakUserAnswerRecord();
        record.setUserId(dtoRecord.getUserId());
        record.setSpeakingId(dtoRecord.getSpeakingId());
        record.setScore(dtoRecord.getScore() != null ? new java.math.BigDecimal(dtoRecord.getScore()) : null);
        record.setAnswerEvaluation(dtoRecord.getAnswerEvaluation());
        record.setDurationSeconds(dtoRecord.getDurationSeconds());
        record.setDeviceType(dtoRecord.getDeviceType());
        record.setPart(dtoRecord.getPart());
        long recordId = new CreateId().generateId();
        record.setRecordId(recordId);
        recordMapper.insert(record);

        // 异步调用设置用户答题评分，否则会很卡
        setUserAnswerScoreByRecordId(recordId);

        // 保存明细记录
        Map<String, List<SpeakUserAnswerDTO.AnswerWrapper>> parts = speakUserAnswerDTO.getParts();
        if (parts != null) {
            for (List<SpeakUserAnswerDTO.AnswerWrapper> wrappers : parts.values()) {
                for (SpeakUserAnswerDTO.AnswerWrapper wrapper : wrappers) {
                    SpeakUserAnswerDetail detail = new SpeakUserAnswerDetail();
                    SpeakUserAnswerDTO.SpeakUserAnswerDetail dtoDetail = wrapper.getAnswer();
                    detail.setRecordId(record.getRecordId());
                    detail.setUserId(dtoDetail.getUserId());
                    detail.setQuestionId(dtoDetail.getQuestionId());
                    String userAudioUrl = dtoDetail.getUserAudioUrl();
                    detail.setUserAudioUrl(userAudioUrl);
                    //防止空异常，然后处理音频文件路径
                    if (userAudioUrl != null && urlPrefix != null && baseDir != null) {
                        if (userAudioUrl.startsWith(urlPrefix)) {
                            // 替换前缀
                          String userAudioUrlToAi = baseDir + userAudioUrl.substring(urlPrefix.length());
                           detail.setUserAudioUrltoAi(userAudioUrlToAi);
                        }
                    }

                    detail.setUserTranscript(dtoDetail.getUserTranscript());
                    detail.setScore(dtoDetail.getScore() != null ? new java.math.BigDecimal(dtoDetail.getScore()) : null);
                    detail.setFeedback(dtoDetail.getFeedback());
                    detail.setPart(dtoDetail.getPart());
                    detail.setDetailId(new CreateId().generateId());
                    detail.setQuestionContent(dtoDetail.getQuestionContent());
                    detailMapper.insert(detail);
                }
            }
        }

    }

//    @Override
//    public SpeakUserAnswerDTO getUserAnswerByRecordId(Long recordId) {
//        if (recordId == null) {
//            throw new IllegalArgumentException("记录ID不能为空");
//        }
//
//        SpeakUserAnswerRecord record = recordMapper.findById(recordId);
//        if (record == null) {
//            throw new RuntimeException("未找到对应的答题记录");
//        }
//
//        List<SpeakUserAnswerDetail> details = detailMapper.findByRecordId(recordId);
//
//        SpeakUserAnswerDTO dto = new SpeakUserAnswerDTO();
//        SpeakUserAnswerDTO.SpeakUserAnswerRecord dtoRecord = new SpeakUserAnswerDTO.SpeakUserAnswerRecord();
//        dtoRecord.setRecordId(record.getRecordId());
//        dtoRecord.setUserId(record.getUserId());
//        dtoRecord.setSpeakingId(record.getSpeakingId());
//        dtoRecord.setScore(record.getScore() != null ? record.getScore().toString() : null);
//        dtoRecord.setAnswerEvaluation(record.getAnswerEvaluation());
//        dtoRecord.setDurationSeconds(record.getDurationSeconds());
//        dtoRecord.setDeviceType(record.getDeviceType());
//        dtoRecord.setPart(record.getPart());
//        dto.setSpeakUserAnswerRecord(dtoRecord);
//
//        Map<String, List<SpeakUserAnswerDTO.AnswerWrapper>> parts = new HashMap<>();
//
//        for (SpeakUserAnswerDetail detail : details) {
//            String part = detail.getPart();
//            SpeakUserAnswerDTO.AnswerWrapper wrapper = new SpeakUserAnswerDTO.AnswerWrapper();
//            SpeakUserAnswerDTO.SpeakUserAnswerDetail dtoDetail = new SpeakUserAnswerDTO.SpeakUserAnswerDetail();
//
//            dtoDetail.setDetailId(detail.getDetailId());
//            dtoDetail.setRecordId(detail.getRecordId());
//            dtoDetail.setUserId(detail.getUserId());
//            dtoDetail.setQuestionId(detail.getQuestionId());
//            dtoDetail.setUserAudioUrl(detail.getUserAudioUrl());
//            dtoDetail.setUserTranscript(detail.getUserTranscript());
//            dtoDetail.setScore(detail.getScore() != null ? detail.getScore().toString() : null);
//            dtoDetail.setFeedback(detail.getFeedback());
//            dtoDetail.setPart(part);
//            dtoDetail.setQuestionContent(detail.getQuestionContent());
//            wrapper.setAnswer(dtoDetail);
//
//            if (!parts.containsKey(part)) {
//                parts.put(part, new ArrayList<>());
//            }
//            parts.get(part).add(wrapper);
//        }
//
//        dto.setParts(parts);
//        return dto;
//    }

    @Override
    public SpeakUserAnswerDTO getUserAnswerByRecordId(Long recordId) {
        if (recordId == null) {
            throw new IllegalArgumentException("记录ID不能为空");
        }

        SpeakUserAnswerRecord record = recordMapper.findById(recordId);
        if (record == null) {
            throw new RuntimeException("未找到对应的答题记录");
        }

        List<SpeakUserAnswerDetail> details = detailMapper.findByRecordId(recordId);

        SpeakUserAnswerDTO dto = new SpeakUserAnswerDTO();
        SpeakUserAnswerDTO.SpeakUserAnswerRecord dtoRecord = new SpeakUserAnswerDTO.SpeakUserAnswerRecord();
        dtoRecord.setRecordId(record.getRecordId());
        dtoRecord.setUserId(record.getUserId());
        dtoRecord.setSpeakingId(record.getSpeakingId());
        dtoRecord.setScore(record.getScore() != null ? record.getScore().toString() : null);
        dtoRecord.setAnswerEvaluation(record.getAnswerEvaluation());
        dtoRecord.setDurationSeconds(record.getDurationSeconds());
        dtoRecord.setDeviceType(record.getDeviceType());
        dtoRecord.setPart(record.getPart());
        dto.setSpeakUserAnswerRecord(dtoRecord);

        Map<String, List<SpeakUserAnswerDTO.AnswerWrapper>> parts = new HashMap<>();

        for (SpeakUserAnswerDetail detail : details) {
            String part = detail.getPart();
            SpeakUserAnswerDTO.AnswerWrapper wrapper = new SpeakUserAnswerDTO.AnswerWrapper();
            SpeakUserAnswerDTO.SpeakUserAnswerDetail dtoDetail = new SpeakUserAnswerDTO.SpeakUserAnswerDetail();

            dtoDetail.setDetailId(detail.getDetailId());
            dtoDetail.setRecordId(detail.getRecordId());
            dtoDetail.setUserId(detail.getUserId());
            dtoDetail.setQuestionId(detail.getQuestionId());
            dtoDetail.setUserAudioUrl(detail.getUserAudioUrl());
            dtoDetail.setUserTranscript(detail.getUserTranscript());
            dtoDetail.setScore(detail.getScore() != null ? detail.getScore().toString() : null);
            dtoDetail.setFeedback(detail.getFeedback());
            dtoDetail.setPart(part);
            dtoDetail.setQuestionContent(detail.getQuestionContent());
            wrapper.setAnswer(dtoDetail);

            if (!parts.containsKey(part)) {
                parts.put(part, new ArrayList<>());
            }
            parts.get(part).add(wrapper);
        }

        dto.setParts(parts);
        // 异步调用设置用户答题评分，否则会很卡
        setUserAnswerScoreByRecordId(recordId);
        return dto;
    }


    @Transactional(readOnly = true)
    public SpeakUserAnswerDTO getUserAnswerDetailsByRecordIdAndUserId(Long userId, Long recordId) {
        if (recordId == null || userId == null) {
            throw new IllegalArgumentException("记录ID和用户ID不能为空");
        }

        // 查询答题明细
        List<SpeakUserAnswerDetail> details = detailMapper.findByRecordIdAndUserId(recordId, userId);
        if (details == null || details.isEmpty()) {
            throw new RuntimeException("未找到对应的答题明细");
        }

        // 构建返回的DTO
        SpeakUserAnswerDTO dto = new SpeakUserAnswerDTO();

        Map<String, List<SpeakUserAnswerDTO.AnswerWrapper>> parts = new HashMap<>();

        for (SpeakUserAnswerDetail detail : details) {
            String part = detail.getPart();
            SpeakUserAnswerDTO.AnswerWrapper wrapper = new SpeakUserAnswerDTO.AnswerWrapper();
            SpeakUserAnswerDTO.SpeakUserAnswerDetail dtoDetail = new SpeakUserAnswerDTO.SpeakUserAnswerDetail();

            dtoDetail.setDetailId(detail.getDetailId());
            dtoDetail.setRecordId(detail.getRecordId());
            dtoDetail.setUserId(detail.getUserId());
            dtoDetail.setQuestionId(detail.getQuestionId());
            dtoDetail.setUserAudioUrl(detail.getUserAudioUrl());
            dtoDetail.setUserTranscript(detail.getUserTranscript());
            dtoDetail.setScore(detail.getScore() != null ? detail.getScore().toString() : null);
            dtoDetail.setFeedback(detail.getFeedback());
            dtoDetail.setPart(part);
            dtoDetail.setQuestionContent(detail.getQuestionContent());
            wrapper.setAnswer(dtoDetail);

            if (!parts.containsKey(part)) {
                parts.put(part, new ArrayList<>());
            }
            parts.get(part).add(wrapper);
        }

        dto.setParts(parts);
        return dto;
    }
    @Async
    @Override
    public void setUserAnswerScoreByRecordId(Long recordId) {
        if (recordId == null) {
            throw new IllegalArgumentException("Record ID cannot be null");
        }

        SpeakUserAnswerRecord record = recordMapper.findById(recordId);
        if (record == null) {
            throw new RuntimeException("No corresponding answer record found");
        }

        List<SpeakUserAnswerDetail> details = detailMapper.findByRecordId(recordId);

        double totalScore = 0.0; // Variable to store the total score

        for (SpeakUserAnswerDetail detail : details) {
            String audioFilePath = detail.getUserAudioUrltoAi();
            String questionChoice = detail.getQuestionContent();
            String questionKeywords = detail.getQuestionContent();

            try {
                // 调用AI评测接口
                double resultScore = new SpeakToAiUtil().evaluateAudio(audioFilePath, questionChoice, questionKeywords);
                //ai测评结果太严格了，0到10分保底
                if(resultScore < 10) {
                    Random random = new Random();
                    double randomNumber = random.nextDouble()* 10; // 生成0到10的随机数
                    totalScore = randomNumber;
                }
                totalScore += resultScore; // 增加总分数

                // 更新明细表中的分数
                detail.setScore(new java.math.BigDecimal(resultScore));
                detailMapper.updateScoreById(detail.getDetailId(), resultScore);
            } catch (Exception e) {
                throw new RuntimeException("Error during AI evaluation", e);
            }
        }

        // 更新主记录表中的分数
        record.setScore(new java.math.BigDecimal(totalScore));
        recordMapper.updateScoreById(recordId, totalScore);
    }


    /**
     * 查询用户ID对应的口语答题主记录
     * @param userId 用户ID
     */
    @Transactional(readOnly = true)
    public List<SpeakingAnswerRecordDTO> getUserSpeakingAnswerRecords(Long userId) {
        return recordMapper.selectUserSpeakingAnswerRecordsByUserId(userId);
    }
}