package com.apps.service.read2;

import com.apps.common.CreateId;
import com.apps.dto.read2.*;
import com.apps.mapper.read.AnswerMapper;
import com.apps.mapper.read.QuestionMapper;
import com.apps.mapper.read.ReadingMapper;
import com.apps.model.read.Answer;
import com.apps.model.read.Question;
import com.apps.model.read.Reading;
import com.apps.model.read.ReadingSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReadingService2 {

    @Autowired
    private ReadingMapper readingMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AnswerMapper answerMapper;

    /**
     * 插入阅读汇总信息、阅读材料及其题目和答案
     * @param readingInsertDTO 包含阅读汇总信息、阅读材料及其题目和答案的 DTO
     */
    @Transactional
    public void insertReadingWithQuestions(ReadingInsertDTO readingInsertDTO) {
        if (readingInsertDTO == null || readingInsertDTO.getReadingSummary() == null) {
            throw new IllegalArgumentException("阅读汇总信息不能为空");
        }

        // 插入阅读汇总信息
        ReadingSummaryDTO readingSummaryDTO = readingInsertDTO.getReadingSummary();
        ReadingSummary readingSummary = new ReadingSummary();
        readingSummary.setTitle(readingSummaryDTO.getTitle());
        Long summaryId = new CreateId().generateId();
        readingSummary.setId(summaryId);
        LocalDateTime now = LocalDateTime.now();
        // 转换为 Timestamp
        Timestamp timestamp = Timestamp.valueOf(now);
        readingSummary.setCreatedAt(timestamp);//现在的时间
         readingMapper.insertReadingSummary(readingSummary);

        // 遍历每个 Part，插入对应的阅读材料和题目
        List<PartDTO> parts = readingInsertDTO.getParts();
        if (parts != null && !parts.isEmpty()) {
            for (PartDTO part : parts) {
                ReadingDTO readingDTO = part.getReading();
                if (readingDTO == null) {
                    throw new IllegalArgumentException("阅读材料数据不能为空");
                }

                // 插入阅读材料
                Reading reading = new Reading();
                reading.setTitle(readingDTO.getTitle());
                reading.setContent(readingDTO.getContent());
                reading.setImageBase64(readingDTO.getImageBase64());
                reading.setReadSummaryId(readingSummary.getId()); // 关联阅读汇总 ID
                Long readingId = new CreateId().generateId();
                reading.setId(readingId);
                readingMapper.insertReading(reading);

                // 插入题目和答案
                List<QuestionWrapperDTO> questions = part.getQuestions();
                if (questions != null) {
                    for (QuestionWrapperDTO wrapper : questions) {
                        QuestionDTO questionDTO = wrapper.getQuestion();
                        if (questionDTO == null) continue;

                        // 插入题目
                        Question question = new Question();
                        question.setReadingId(reading.getId());
                        question.setType(questionDTO.getType());
                        question.setContent(questionDTO.getContent());
                        question.setPlaceholderFormat(questionDTO.getPlaceholderFormat());
                        question.setSerial(questionDTO.getSerial());
                        Long questionId = new CreateId().generateId();
                        question.setId(questionId);
                        questionMapper.insertQuestion(question);

                        // 插入答案
                        List<AnswerDTO> answers = questionDTO.getAnswers();
                        if (answers != null) {
                            for (AnswerDTO answerDTO : answers) {
                                Answer answer = new Answer();
                                answer.setQuestionId(question.getId());
                                answer.setContent(answerDTO.getContent());
                                answer.setCorrect(answerDTO.getCorrect());
                                answer.setBlankNumber(answerDTO.getBlankNumber());
                                answer.setMatchingKey(answerDTO.getMatchingKey());
                                Long answerId = new CreateId().generateId();
                                answer.setId(answerId);
                                try {
                                    answerMapper.insertAnswer(answer);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据阅读汇总 ID 删除阅读汇总、阅读材料及其题目和答案
     * @param readingSummaryId 阅读汇总 ID
     */
    @Transactional
    public void deleteReadingSummaryById(Long readingSummaryId) {
        if (readingSummaryId == null) {
            throw new IllegalArgumentException("阅读汇总 ID 不能为空");
        }

        // 删除关联的阅读材料及其题目和答案
        List<Reading> readings = readingMapper.selectReadingsBySummaryId(readingSummaryId);
        if (readings != null) {
            for (Reading reading : readings) {
                // 删除题目和答案
                List<Question> questions = questionMapper.selectQuestionsByReadingId(reading.getId());
                if (questions != null) {
                    for (Question question : questions) {
                        answerMapper.deleteAnswersByQuestionId(question.getId());
                    }
                }
                questionMapper.deleteQuestionsByReadingId(reading.getId());

                // 删除阅读材料
                readingMapper.deleteReadingById(reading.getId());
            }
        }

        // 删除阅读汇总
        readingMapper.deleteReadingSummaryById(readingSummaryId);
    }

    /**
     * 根据阅读汇总 ID 查询阅读汇总、阅读材料及其题目和答案
     * @param readingSummaryId 阅读汇总 ID
     * @return 包含阅读汇总、阅读材料及其题目和答案的 DTO
     */
    public ReadingInsertDTO getReadingSummaryById(Long readingSummaryId) {
        if (readingSummaryId == null) {
            throw new IllegalArgumentException("阅读汇总 ID 不能为空");
        }

        ReadingSummary readingSummary = readingMapper.selectReadingSummaryById(readingSummaryId);
        if (readingSummary == null) {
            throw new NoSuchElementException("未找到对应的阅读汇总");
        }

        // 构建 ReadingInsertDTO
        ReadingInsertDTO readingInsertDTO = new ReadingInsertDTO();
        ReadingSummaryDTO readingSummaryDTO = new ReadingSummaryDTO();
        readingSummaryDTO.setId(readingSummary.getId());
        readingSummaryDTO.setTitle(readingSummary.getTitle());
        readingSummaryDTO.setCreatedAt(readingSummary.getCreatedAt());
        readingSummaryDTO.setUpdatedAt(readingSummary.getUpdatedAt());
        readingInsertDTO.setReadingSummary(readingSummaryDTO);

        // 查询关联的阅读材料及其题目和答案
        List<Reading> readings = readingMapper.selectReadingsBySummaryId(readingSummaryId);
        List<PartDTO> parts = new ArrayList<>();
        if (readings != null) {
            for (Reading reading : readings) {
                PartDTO partDTO = new PartDTO();
                ReadingDTO readingDTO = new ReadingDTO();
                readingDTO.setId(reading.getId());
                readingDTO.setTitle(reading.getTitle());
                readingDTO.setContent(reading.getContent());
                readingDTO.setImageBase64(reading.getImageBase64());
                partDTO.setReading(readingDTO);

                // 查询题目和答案
                List<Question> questions = questionMapper.selectQuestionsByReadingId(reading.getId());
                List<QuestionWrapperDTO> questionWrappers = new ArrayList<>();
                if (questions != null) {
                    for (Question question : questions) {
                        QuestionDTO questionDTO = new QuestionDTO();
                        questionDTO.setId(question.getId());
                        questionDTO.setType(question.getType());
                        questionDTO.setContent(question.getContent());
                        questionDTO.setPlaceholderFormat(question.getPlaceholderFormat());
                        questionDTO.setSerial(question.getSerial());

                        // 查询答案
                        List<Answer> answers = answerMapper.selectAnswersByQuestionId(question.getId());
                        List<AnswerDTO> answerDTOs = new ArrayList<>();
                        if (answers != null) {
                            for (Answer answer : answers) {
                                AnswerDTO answerDTO = new AnswerDTO();
                                answerDTO.setId(answer.getId());
                                answerDTO.setContent(answer.getContent());
                                answerDTO.setCorrect(answer.getCorrect());
                                answerDTO.setBlankNumber(answer.getBlankNumber());
                                answerDTO.setMatchingKey(answer.getMatchingKey());
                                answerDTOs.add(answerDTO);
                            }
                        }
                        questionDTO.setAnswers(answerDTOs);

                        QuestionWrapperDTO wrapperDTO = new QuestionWrapperDTO();
                        wrapperDTO.setQuestion(questionDTO);
                        questionWrappers.add(wrapperDTO);
                    }
                }
                partDTO.setQuestions(questionWrappers);
                parts.add(partDTO);
            }
        }
        readingInsertDTO.setParts(parts);

        return readingInsertDTO;
    }


    /**
     * 查询所有阅读汇总，包括阅读汇总ID 和标题
     * @return 所有阅读汇总及其 ID 和标题
     */
    public List<ReadingSummary> getAllReadingSummary() {
        // 查询所有阅读汇总
        List<ReadingSummary> readingSummaries = readingMapper.selectAllReadingSummary();
        return readingSummaries;
    }
}