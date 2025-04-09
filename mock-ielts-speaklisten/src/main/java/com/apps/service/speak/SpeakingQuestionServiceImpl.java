package com.apps.service.speak;

import com.apps.dto.speak.SpeakingQuestionDTO;
import com.apps.model.speak.SpeakingQuestion;
import com.apps.mapper.speak.SpeakingQuestionMapper;
import com.apps.service.speak.impl.SpeakingQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 口语题目服务实现类
 */
@Service
public class SpeakingQuestionServiceImpl implements SpeakingQuestionService {

    @Autowired
    private SpeakingQuestionMapper speakingQuestionMapper;

    @Override
    @Transactional
    public SpeakingQuestionDTO createSpeakingQuestion(SpeakingQuestionDTO speakingQuestionDTO) {
        SpeakingQuestion speakingQuestion = new SpeakingQuestion();
        // 将DTO转换为实体类
        speakingQuestion.setSpeakingId(speakingQuestionDTO.getSpeakingId());
        speakingQuestion.setType(speakingQuestionDTO.getType());
        speakingQuestion.setContent(speakingQuestionDTO.getContent());
        speakingQuestion.setImageUrl(speakingQuestionDTO.getImageUrl());
        speakingQuestion.setPart(speakingQuestionDTO.getPart());
        speakingQuestion.setSerial(speakingQuestionDTO.getSerial());
        speakingQuestionMapper.insertSpeakingQuestion(speakingQuestion);
        speakingQuestionDTO.setId(speakingQuestion.getId());
        return speakingQuestionDTO;
    }

    @Override
    @Transactional
    public SpeakingQuestionDTO updateSpeakingQuestion(Long id, SpeakingQuestionDTO speakingQuestionDTO) {
        SpeakingQuestion speakingQuestion = speakingQuestionMapper.findSpeakingQuestionById(id);
        if (speakingQuestion == null) {
            throw new RuntimeException("SpeakingQuestion not found");
        }
        // 更新实体类
        speakingQuestion.setSpeakingId(speakingQuestionDTO.getSpeakingId());
        speakingQuestion.setType(speakingQuestionDTO.getType());
        speakingQuestion.setContent(speakingQuestionDTO.getContent());
        speakingQuestion.setImageUrl(speakingQuestionDTO.getImageUrl());
        speakingQuestion.setPart(speakingQuestionDTO.getPart());
        speakingQuestion.setSerial(speakingQuestionDTO.getSerial());
        speakingQuestionMapper.updateSpeakingQuestion(speakingQuestion);
        return speakingQuestionDTO;
    }

    @Override
    @Transactional
    public void deleteSpeakingQuestion(Long id) {
        speakingQuestionMapper.deleteSpeakingQuestion(id);
    }

    @Override
    public SpeakingQuestionDTO getSpeakingQuestionById(Long id) {
        SpeakingQuestion speakingQuestion = speakingQuestionMapper.findSpeakingQuestionById(id);
        if (speakingQuestion == null) {
            throw new RuntimeException("SpeakingQuestion not found");
        }
        // 将实体类转换为DTO
        SpeakingQuestionDTO speakingQuestionDTO = new SpeakingQuestionDTO();
        speakingQuestionDTO.setId(speakingQuestion.getId());
        speakingQuestionDTO.setSpeakingId(speakingQuestion.getSpeakingId());
        speakingQuestionDTO.setType(speakingQuestion.getType());
        speakingQuestionDTO.setContent(speakingQuestion.getContent());
        speakingQuestionDTO.setImageUrl(speakingQuestion.getImageUrl());
        speakingQuestionDTO.setCreatedAt(speakingQuestion.getCreatedAt());
        speakingQuestionDTO.setUpdatedAt(speakingQuestion.getUpdatedAt());
        speakingQuestionDTO.setPart(speakingQuestion.getPart());
        speakingQuestionDTO.setSerial(speakingQuestion.getSerial());
        return speakingQuestionDTO;
    }

    @Override
    public List<SpeakingQuestionDTO> getAllSpeakingQuestions() {
        List<SpeakingQuestion> speakingQuestions = speakingQuestionMapper.findAllSpeakingQuestions();
        return speakingQuestions.stream().map(speakingQuestion -> {
            SpeakingQuestionDTO speakingQuestionDTO = new SpeakingQuestionDTO();
            speakingQuestionDTO.setId(speakingQuestion.getId());
            speakingQuestionDTO.setSpeakingId(speakingQuestion.getSpeakingId());
            speakingQuestionDTO.setType(speakingQuestion.getType());
            speakingQuestionDTO.setContent(speakingQuestion.getContent());
            speakingQuestionDTO.setImageUrl(speakingQuestion.getImageUrl());
            speakingQuestionDTO.setCreatedAt(speakingQuestion.getCreatedAt());
            speakingQuestionDTO.setUpdatedAt(speakingQuestion.getUpdatedAt());
            speakingQuestionDTO.setPart(speakingQuestion.getPart());
            speakingQuestionDTO.setSerial(speakingQuestion.getSerial());
            return speakingQuestionDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SpeakingQuestionDTO> getSpeakingQuestionsBySpeakingId(Long speakingId) {
        List<SpeakingQuestion> speakingQuestions = speakingQuestionMapper.findAllSpeakingQuestions().stream()
                .filter(q -> q.getSpeakingId().equals(speakingId))
                .collect(Collectors.toList());
        return speakingQuestions.stream().map(speakingQuestion -> {
            SpeakingQuestionDTO speakingQuestionDTO = new SpeakingQuestionDTO();
            speakingQuestionDTO.setId(speakingQuestion.getId());
            speakingQuestionDTO.setSpeakingId(speakingQuestion.getSpeakingId());
            speakingQuestionDTO.setType(speakingQuestion.getType());
            speakingQuestionDTO.setContent(speakingQuestion.getContent());
            speakingQuestionDTO.setImageUrl(speakingQuestion.getImageUrl());
            speakingQuestionDTO.setCreatedAt(speakingQuestion.getCreatedAt());
            speakingQuestionDTO.setUpdatedAt(speakingQuestion.getUpdatedAt());
            speakingQuestionDTO.setPart(speakingQuestion.getPart());
            speakingQuestionDTO.setSerial(speakingQuestion.getSerial());
            return speakingQuestionDTO;
        }).collect(Collectors.toList());
    }
}