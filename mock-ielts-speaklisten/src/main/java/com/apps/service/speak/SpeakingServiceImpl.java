package com.apps.service.speak;

import com.apps.common.CreateId;
import com.apps.dto.speak.SpeakingDTO;
import com.apps.dto.speak.SpeakingQuestionDTO;
import com.apps.mapper.speak.SpeakingMapper;
import com.apps.mapper.speak.SpeakingQuestionMapper;
import com.apps.model.speak.Speaking;
import com.apps.model.speak.SpeakingQuestion;
import com.apps.service.speak.impl.SpeakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 口语材料服务实现类
 */
@Service
public class SpeakingServiceImpl implements SpeakingService {

    @Autowired
    private SpeakingMapper speakingMapper;

    @Autowired
    private SpeakingQuestionMapper speakingQuestionMapper;

    @Override
    @Transactional
    public SpeakingDTO createSpeaking(SpeakingDTO speakingDTO) {
        // 创建一个新的Speaking实体
        Speaking speaking = new Speaking();
        speaking.setTitle(speakingDTO.getSpeaking().getTitle());
        speaking.setImageUrl(speakingDTO.getSpeaking().getImageUrl());
        speaking.setTranscript(speakingDTO.getSpeaking().getTranscript());


        long speakingId = new CreateId().generateId(); // 生成一个随机的 long 值
        speaking.setId(speakingId);
        // 插入新的口语材料记录到数据库
        speakingMapper.insertSpeaking(speaking);

        // 设置随机生成的long类型的ID为新插入记录的ID
        speakingDTO.getSpeaking().setId(String.valueOf(speaking.getId()));

        // 插入对应的口语题目
        if (speakingDTO.getParts() != null) {
            for (Map.Entry<String, List<SpeakingDTO.QuestionWrapper>> entry : speakingDTO.getParts().entrySet()) {
                for (SpeakingDTO.QuestionWrapper wrapper : entry.getValue()) {
                    SpeakingQuestionDTO questionDTO = wrapper.getQuestion();
                    SpeakingQuestion question = new SpeakingQuestion();
                    question.setSpeakingId(speaking.getId());
                    question.setType(questionDTO.getType());
                    question.setContent(questionDTO.getContent());
                    question.setImageUrl(questionDTO.getImageUrl());
                    question.setPart(entry.getKey());
                    question.setSerial(questionDTO.getSerial());
                    // 生成随机的问题id
                    long questionId = new CreateId().generateId(); // 生成一个随机的 long 值
                    question.setId(questionId);
                    speakingQuestionMapper.insertSpeakingQuestion(question);
                    questionDTO.setId(question.getId());
                }
            }
        }

        return speakingDTO;
    }

    @Override
    @Transactional
    public SpeakingDTO updateSpeaking(Long id, SpeakingDTO speakingDTO) {
        // 根据ID查找现有的口语材料记录
        Speaking speaking = speakingMapper.findSpeakingById(id);
        if (speaking == null) {
            throw new RuntimeException("Speaking not found");
        }

        // 更新口语材料的属性
        speaking.setTitle(speakingDTO.getSpeaking().getTitle());
        speaking.setImageUrl(speakingDTO.getSpeaking().getImageUrl());
        speaking.setTranscript(speakingDTO.getSpeaking().getTranscript());

        // 更新数据库中的口语材料记录
        speakingMapper.updateSpeaking(speaking);
        return speakingDTO;
    }

    @Override
    @Transactional
    public void deleteSpeaking(Long id) {
        // 删除数据库中的口语材料记录
        speakingMapper.deleteSpeaking(id);
    }

    @Override
    public SpeakingDTO getSpeakingById(Long id) {
        // 根据ID查找口语材料记录
        Speaking speaking = speakingMapper.findSpeakingById(id);
        if (speaking == null) {
            throw new RuntimeException("Speaking not found");
        }

        // 创建并设置口语材料DTO
        SpeakingDTO speakingDTO = new SpeakingDTO();
        SpeakingDTO.Speaking speakingData = new SpeakingDTO.Speaking();
        speakingData.setId(String.valueOf(speaking.getId()));
        speakingData.setTitle(speaking.getTitle());
        speakingData.setImageUrl(speaking.getImageUrl());
        speakingData.setTranscript(speaking.getTranscript());
        speakingDTO.setSpeaking(speakingData);

        // 获取所有相关的口语题目
        List<SpeakingQuestion> questions = speakingQuestionMapper.findAllSpeakingQuestions().stream()
                .filter(q -> q.getSpeakingId().equals(id))
                .collect(Collectors.toList());

        // 将口语题目按部分分组并转换为DTO
        Map<String, List<SpeakingDTO.QuestionWrapper>> parts = questions.stream()
                .collect(Collectors.groupingBy(SpeakingQuestion::getPart))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream().map(q -> {
                            SpeakingDTO.QuestionWrapper wrapper = new SpeakingDTO.QuestionWrapper();
                            SpeakingQuestionDTO questionDTO = new SpeakingQuestionDTO();
                            questionDTO.setId(q.getId());
                            questionDTO.setSpeakingId(q.getSpeakingId());
                            questionDTO.setType(q.getType());
                            questionDTO.setContent(q.getContent());
                            questionDTO.setImageUrl(q.getImageUrl());
                            questionDTO.setCreatedAt(q.getCreatedAt());
                            questionDTO.setUpdatedAt(q.getUpdatedAt());
                            questionDTO.setSerial(q.getSerial());
                            wrapper.setQuestion(questionDTO);
                            return wrapper;
                        }).collect(Collectors.toList())
                ));

        speakingDTO.setParts(parts);
        return speakingDTO;
    }

    @Override
    public List<SpeakingDTO> getAllSpeakings() {
        // 获取所有口语材料记录
        List<Speaking> speakings = speakingMapper.findAllSpeakings();

        // 将每个口语材料记录转换为DTO
        return speakings.stream().map(speaking -> getSpeakingById(speaking.getId())).collect(Collectors.toList());
    }
}