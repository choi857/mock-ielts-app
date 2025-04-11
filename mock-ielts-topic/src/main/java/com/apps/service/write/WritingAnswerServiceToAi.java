package com.apps.service.write;

import com.apps.dto.write.IELTSAssessmentResult;
import com.apps.mapper.write.WritingAnswerDetailMapper;
import com.apps.mapper.write.WritingAnswerRecordMapper;
import com.apps.mapper.write.WritingTaskMapper;
import com.apps.model.write.WritingAnswerDetail;
import com.apps.model.write.WritingAnswerRecord;
import com.apps.model.write.WritingTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.apps.service.write.WritingAnswerService.assessIELTS;

@Service
public class WritingAnswerServiceToAi {

    @Autowired
    private WritingAnswerRecordMapper writingAnswerRecordMapper;

    @Autowired
    private WritingAnswerDetailMapper writingAnswerDetailMapper;

    @Autowired
    private WritingTaskMapper writingTaskMapper;

    /**
     * 查询 total_score 为空或者为0的记录，并更新 score 和 answerEvaluation
     */
    public void updateScores() {
        // 查询 total_score 为空或者为0的记录
        List<WritingAnswerRecord> records = writingAnswerRecordMapper.selectByTotalScoreIsNullOrZero();

        // 遍历每一条记录
        for (WritingAnswerRecord record : records) {

            // 处理 唯一答题记录id对应的两个题的分数和评价
            double totalScore = processTask(record.getTask1Id(), record.getRecordId());
            // 计算总的 score 更新到 col_write_user_answer_record
             record.setTotalScore(totalScore);
            writingAnswerRecordMapper.updateByPrimaryKey(record);
        }
    }

    /**
     * 处理单个任务的评分和评价，并返回该任务的总分
     *
     * @param taskId   任务ID
     * @param recordId 记录ID
     * @return 该任务的总分
     */
    private double processTask(Long taskId, Long recordId) {
        // 根据任务ID查询任务信息
        WritingTask task = writingTaskMapper.selectByTaskId(taskId);
        if (task == null) {
            return 0;
        }

        // 构建任务标题和描述
        String topic = task.getTaskTitle() + task.getTaskDescription();
        // 根据记录ID查询答案详情
        List<WritingAnswerDetail> details = writingAnswerDetailMapper.selectByRecordId(recordId);

        double taskScore = 0;

        // 遍历每个答案详情
        for (WritingAnswerDetail detail : details) {
            // 检查任务类型是否为 TASK1 或 TASK2
            if (detail.getTaskType().equals("TASK1") || detail.getTaskType().equals("TASK2")) {
                // 调用 assessIELTS 方法进行评分
                Map<String, String> result = assessIELTS(detail.getAnswerContent(), topic);

                // 更新 score 和 answerEvaluation
                double score = Double.valueOf(result.get("score"));
                detail.setScore(score);
                detail.setAnswerEvaluation(String.valueOf(result.get("answerEvaluation")));
                writingAnswerDetailMapper.updateByPrimaryKey(detail);

                // 累加该任务的分数
                taskScore += score;
            }
        }

        return taskScore;
    }


    /**
     * 计算总分
     *
     * @param recordId 记录ID
     * @return 总分
     */
    private double calculateTotalScore(Long recordId) {
        // 根据记录ID查询答案详情
        List<WritingAnswerDetail> details = writingAnswerDetailMapper.selectByRecordId(recordId);
        double totalScore = 0;

        // 遍历每个答案详情
        for (WritingAnswerDetail detail : details) {
            // 如果 score 不为空，则累加到总分
            if (detail.getScore() != null) {
                totalScore += detail.getScore();
            }
        }

        return totalScore;
    }

    /**
     * 新增的公共接口方法，用于更新分数
     */
    public void updateScoresInterface() {
        updateScores();
    }
}
