package com.apps.model.write;

    import java.io.Serializable;
    import java.sql.Timestamp;

    public class WritingQuestion  implements Serializable {
        private Long taskId; // 写作题目ID
        private String taskTitle; // 写作题目标题
        private String taskDescription; // 写作题目描述
        private String taskType; // 写作题目类型
        private String taskRequirements; // 写作要求
        private Integer wordLimit; // 字数限制
        private Timestamp createdAt; // 创建时间
        private Timestamp updatedAt; // 更新时间

        // Getters and Setters
        public Long getTaskId() {
            return taskId;
        }

        public void setTaskId(Long taskId) {
            this.taskId = taskId;
        }

        public String getTaskTitle() {
            return taskTitle;
        }

        public void setTaskTitle(String taskTitle) {
            this.taskTitle = taskTitle;
        }

        public String getTaskDescription() {
            return taskDescription;
        }

        public void setTaskDescription(String taskDescription) {
            this.taskDescription = taskDescription;
        }

        public String getTaskType() {
            return taskType;
        }

        public void setTaskType(String taskType) {
            this.taskType = taskType;
        }

        public String getTaskRequirements() {
            return taskRequirements;
        }

        public void setTaskRequirements(String taskRequirements) {
            this.taskRequirements = taskRequirements;
        }

        public Integer getWordLimit() {
            return wordLimit;
        }

        public void setWordLimit(Integer wordLimit) {
            this.wordLimit = wordLimit;
        }

        public Timestamp getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
        }

        public Timestamp getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Timestamp updatedAt) {
            this.updatedAt = updatedAt;
        }
    }