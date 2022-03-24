public class Task {
        private String taskName;    //название задач
        private int id; //идентификатор
        private String descriptionTask; //описание
        private String status; //статус задачи

        public Task(String taskName, int id, String descriptionTask, String status) {
                this.taskName = taskName;
                this.id = id;
                this.descriptionTask = descriptionTask;
                this.status = status;
        }

        public String getTaskName() {
                return taskName;
        }

        public void setTaskName(String taskName) {
                this.taskName = taskName;
        }

        public int getID() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getDescriptionTask() {
                return descriptionTask;
        }

        public void setDescriptionTask(String descriptionTask) {
                this.descriptionTask = descriptionTask;
        }

        public String getStatus() {
                return status;
        }

        public void setStatus(String status) {
                this.status = status;
        }

        @Override
        public String toString() {
                return "Task{" +
                        "taskName='" + taskName + '\'' +
                        ", id=" + id +
                        ", descriptionTask='" + descriptionTask + '\'' +
                        ", status='" + status + '\'' +
                        '}';
        }
}
