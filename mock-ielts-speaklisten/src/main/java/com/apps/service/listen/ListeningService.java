package com.apps.service.listen;

import com.apps.dto.ListeningWithQuestionsAndAnswersDTO;
import com.apps.dto.ListeningWithQuestionsDTO;
import com.apps.mapper.listen.ListeningMapper;
import com.apps.mapper.listen.ListeningQuestionMapper;
import com.apps.mapper.listen.ListeningAnswerMapper;
import com.apps.model.listen.Listening;
import com.apps.model.listen.ListeningQuestion;
import com.apps.model.listen.ListeningAnswer;
import com.apps.common.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ListeningService {

    @Autowired
    private ListeningMapper listeningMapper;

    @Autowired
    private ListeningQuestionMapper listeningQuestionMapper;

    @Autowired
    private ListeningAnswerMapper listeningAnswerMapper;

    /**
     * 新增听力题及其题目和答案
     *
     * @param dto 听力题及其题目和答案的DTO对象
     * @return ResponseResult<String> 响应结果
     */
    public ResponseResult<Object> addListeningWithQuestionsAndAnswers(ListeningWithQuestionsAndAnswersDTO dto) {
        try {
            Listening listening = dto.getListening();
            Map<String, List<ListeningWithQuestionsAndAnswersDTO.QuestionWithAnswers>> parts = dto.getParts();
            Random random = new Random();
            long randomId = random.nextLong(); // 生成一个随机的 long 值
            listening.setId(randomId); // 设置听力材料ID
            // 插入听力材料
            listeningMapper.insertListening(listening);

            // 插入题目和答案
            for (Map.Entry<String, List<ListeningWithQuestionsAndAnswersDTO.QuestionWithAnswers>> entry : parts.entrySet()) {
                String part = entry.getKey();
                    List<ListeningWithQuestionsAndAnswersDTO.QuestionWithAnswers> questionsWithAnswers = entry.getValue();

                for (ListeningWithQuestionsAndAnswersDTO.QuestionWithAnswers questionWithAnswers : questionsWithAnswers) {
                    ListeningQuestion question = questionWithAnswers.getQuestion();

                    // 校验题目类型是否为SINGLE_CHOICE, FILL_IN_THE_BLANK, MATCHING之一
                    if (!"SINGLE_CHOICE".equals(question.getType()) && !"FILL_IN_THE_BLANK".equals(question.getType()) && !"MATCHING".equals(question.getType())) {
                        return ResponseResult.fail("题目类型不为：SINGLE_CHOICE, FILL_IN_THE_BLANK, 或 MATCHING");
                    }

                    question.setListeningId(listening.getId()); // 设置听力材料ID
                    question.setPart(part); // 设置题目部分
                    Random randomQuestion = new Random();
                    long randomIdQuestion = random.nextLong(); // 生成一个随机的 long 值
                    question.setId(randomIdQuestion); // 设置题目ID
                    listeningQuestionMapper.insertQuestion(question);

                    // 插入答案
                    for (ListeningAnswer answer : questionWithAnswers.getAnswers()) {
                        answer.setQuestionId(question.getId()); // 设置题目ID
                        Random randomAnswer = new Random();
                        long randomAnswerId = random.nextLong(); // 生成一个随机的 long 值
                        answer.setId(randomAnswerId); // 设置答案ID
                        //校验插入的值是否有对应正确答案
                        ResponseResult<Object> objectResponseResult = validateAnswer(question, answer);
                        if (objectResponseResult != null) {
                            return objectResponseResult;
                        }
                        listeningAnswerMapper.insertAnswer(answer);
                    }
                }
            }
            return ResponseResult.success("听力题新增成功");
        } catch (Exception e) {
            // 捕获所有异常，防止空指针异常和其他可能的异常
            return ResponseResult.fail("新增听力题失败: " + e.getMessage());
        }
    }

    /**
     * 校验答案是否有对应正确答案
     * @param question
     * @param answer
     * @return
     */
    private ResponseResult<Object> validateAnswer(ListeningQuestion question, ListeningAnswer answer) {
        switch (question.getType()) {
            case "FILL_IN_THE_BLANK":
                if (answer.getBlankNumber() == null) {
                    return  ResponseResult.fail("填空题顺序不能为空");
                 }
                if (answer.getMatchingKey() == null) {
                    return  ResponseResult.fail("填空题目答案不能为空");
                }
                break;
            case "SINGLE_CHOICE":
            case "MATCHING":
                if (answer.getMatchingKey() == null) {
                    return  ResponseResult.fail("选择，配对题目答案不能为空");
                }
                break;
            default:
                throw new IllegalArgumentException("题目类型有误: " + question.getType());
        }
        return null;
    }

    /**
     * 管理员获取听力题目及答案
     * @param listeningId
     * @return
     */
    public ResponseResult<Object> getListeningWithQuestionsAndAnswers(Long listeningId) {
        Listening listening = listeningMapper.findListeningById(listeningId);
        if (listening == null) {
            return ResponseResult.fail("Listening not found");
        }

        List<ListeningQuestion> questions = listeningQuestionMapper.findQuestionsByListeningId(listeningId);
        Map<String, List<ListeningWithQuestionsAndAnswersDTO.QuestionWithAnswers>> parts = questions.stream()
                .collect(Collectors.groupingBy(
                        ListeningQuestion::getPart,
                        Collectors.mapping(question -> {
                            List<ListeningAnswer> answers = listeningAnswerMapper.findAnswersByQuestionId(question.getId());
                            ListeningWithQuestionsAndAnswersDTO.QuestionWithAnswers qwa = new ListeningWithQuestionsAndAnswersDTO.QuestionWithAnswers();
                            qwa.setQuestion(question);
                            qwa.setAnswers(answers);
                            return qwa;
                        }, Collectors.toList())
                ));

        ListeningWithQuestionsAndAnswersDTO dto = new ListeningWithQuestionsAndAnswersDTO();
        dto.setListening(listening);
        dto.setParts(parts);

        return ResponseResult.success(dto);
    }
    /**
     * 更新听力题目
     * @param dto 听力题目对象
     * @return ResponseResult<String> 响应结果
     */
    public ResponseResult<Object> updateListeningWithQuestionsAndAnswers(ListeningWithQuestionsAndAnswersDTO dto) {
        Listening listening = dto.getListening();
        Listening existingListening = listeningMapper.findListeningById(listening.getId());

        if (existingListening == null) {
            return addListeningWithQuestionsAndAnswers(dto);
        } else {
            // Update listening
            listeningMapper.updateListening(listening);

            // Update questions and answers
            Map<String, List<ListeningWithQuestionsAndAnswersDTO.QuestionWithAnswers>> parts = dto.getParts();
            for (Map.Entry<String, List<ListeningWithQuestionsAndAnswersDTO.QuestionWithAnswers>> entry : parts.entrySet()) {
                for (ListeningWithQuestionsAndAnswersDTO.QuestionWithAnswers questionWithAnswers : entry.getValue()) {
                    ListeningQuestion question = questionWithAnswers.getQuestion();
                    question.setListeningId(listening.getId());
                    listeningQuestionMapper.updateQuestion(question);

                    for (ListeningAnswer answer : questionWithAnswers.getAnswers()) {
                        answer.setQuestionId(question.getId());
                        listeningAnswerMapper.updateAnswer(answer);
                    }
                }
            }
            return ResponseResult.success("更新听力题成功");
        }
    }


    /**
     * 查询听力题目部分
     * @param listeningId 听力材料ID
     * @return ResponseResult<ListeningWithQuestionsDTO> 响应结果
     */
    public ResponseResult<ListeningWithQuestionsDTO> getQuestionsByListeningIdAndPart(Long listeningId) {
        try {
            Listening listening = listeningMapper.findListeningById(listeningId);
            if (listening == null) {
                return ResponseResult.fail("听力材料不存在");
            }

            List<ListeningQuestion> questions = listeningQuestionMapper.findQuestionsByListeningId(listeningId);
            Map<String, List<ListeningQuestion>> parts = questions.stream()
                    .collect(Collectors.groupingBy(ListeningQuestion::getPart));

            ListeningWithQuestionsDTO dto = new ListeningWithQuestionsDTO();
            dto.setListening(listening);
            dto.setParts(parts);

            return ResponseResult.success(dto);
        } catch (Exception e) {
            return ResponseResult.fail("查询题目出错: " + e.getMessage());
        }
    }
}