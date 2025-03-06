package com.apps.service;

import com.apps.DTO.AnswerDTO;
import com.apps.DTO.CreateQuestionRequestDTO;
import com.apps.DTO.QuestionDTO;
import com.apps.DTO.ReadingWithQuestionsDTO;
import com.apps.common.ResponseResult;
import com.apps.mapper.AnswerMapper;
import com.apps.mapper.QuestionMapper;
import com.apps.mapper.ReadingMapper;
import com.apps.model.Answer;
import com.apps.model.Question;
import com.apps.model.Reading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReadingService {

    @Autowired
    private ReadingMapper readingMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AnswerMapper answerMapper;

    public ReadingWithQuestionsDTO getReadingWithQuestions(Long readingId) {
        // 查询阅读材料
        Reading reading = readingMapper.selectReadingById(readingId);
        if (reading == null) {
            throw new RuntimeException("阅读材料未找到");
        }

        // 查询题目
        List<Question> questions = questionMapper.selectQuestionsByReadingId(readingId);
        List<QuestionDTO> questionDTOs = new ArrayList<>();

        // 遍历题目列表
        for (Question question : questions) {
            // 查询答案
            List<Answer> answers = answerMapper.selectAnswersByQuestionId(question.getId());
            List<AnswerDTO> answerDTOs = new ArrayList<>();

            // 遍历答案列表
            for (Answer answer : answers) {
                AnswerDTO answerDTO = new AnswerDTO(
                        answer.getId(),
                        answer.getContent(),
                        answer.getIsCorrect(),
                        answer.getBlankNumber(),
                        answer.getMatchingKey()
                );
                answerDTOs.add(answerDTO);
            }

            // 创建 QuestionDTO
            QuestionDTO questionDTO = new QuestionDTO(
                    question.getId(),
                    question.getType(),
                    question.getContent(),
                    question.getPlaceholderFormat(),
                    answerDTOs
            );
            questionDTOs.add(questionDTO);
        }

        // 创建并返回 ReadingWithQuestionsDTO
        return new ReadingWithQuestionsDTO(
                reading.getId(),
                reading.getTitle(),
                reading.getContent(),
                reading.getImageBase64(),
                questionDTOs
        );
    }

    /**
     *  新增阅读材料及其题目和答案
     * @param reading
     */
     public ResponseResult<String> addReadingWithQuestionsAndAnswers(Reading reading) {
        // 插入阅读材料
        readingMapper.insertReading(reading);

        // 插入题目
        for (Question question : reading.getQuestions()) {
            //校验Question.type是否为SINGLE_CHOICE', 'FILL_IN_THE_BLANK', 'MATCHING'之一
            if(!"SINGLE_CHOICE".equals(question.getType()) && !"FILL_IN_THE_BLANK".equals(question.getType()) && !"MATCHING".equals(question.getType())){
              return  ResponseResult.fail("阅读题类型不为：SINGLE_CHOICE, FILL_IN_THE_BLANK, 或 MATCHING");
//                throw new RuntimeException("阅读题类型不为：SINGLE_CHOICE, FILL_IN_THE_BLANK, 或 MATCHING");
            }
            question.setReadingId(reading.getId()); // 设置阅读材料ID
            questionMapper.insertQuestion(question);

            // 插入答案
            for (Answer answer : question.getAnswers()) {
                answer.setQuestionId(question.getId()); // 设置题目ID
                answerMapper.insertAnswer(answer);
            }
        }
        return ResponseResult.success("阅读题新增成功");
    }

    /**
     * 根据ID获取阅读材料及其题目和答案
     * @param readingId
     * @return
     */
    public Reading getReadingWithQuestionsAndAnswers(Long readingId) {
        // 查询阅读材料
        Reading reading = readingMapper.selectReadingById(readingId);
        if (reading == null) {
            throw new RuntimeException("阅读材料未找到");
        }

        // 查询阅读材料对应的题目
        List<Question> questions = questionMapper.selectQuestionsByReadingId(readingId);
        for (Question question : questions) {
            // 查询题目对应的答案
            List<Answer> answers = answerMapper.selectAnswersByQuestionId(question.getId());
            question.setAnswers(answers); // 将答案设置到题目中
        }

        reading.setQuestions(questions); // 将题目设置到阅读材料中
        return reading;
    }

    /**
     * 删除阅读材料及其关联数据
     * @param readingId
     * @return
     */
    public ResponseResult<String> deleteReadingWithQuestionsAndAnswers(Long readingId) {

            // 查询与阅读材料关联的题目
            List<Question> questions = questionMapper.selectQuestionsByReadingId(readingId);
            for (Question question : questions) {
                // 删除与题目关联的答案
                answerMapper.deleteAnswerById(question.getId());
            }

            // 删除与阅读材料关联的题目
            questionMapper.deleteQuestionById(readingId);

            // 删除阅读材料
            readingMapper.deleteReadingById(readingId);

        return ResponseResult.success("阅读材料及其关联数据删除成功");
    }
}