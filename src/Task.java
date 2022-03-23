public class Task {
        private String taskName;    //название задач
        private int id; //идентификатор
        private String status; //статус задачи

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

        public String getStatus() {
                return status;
        }

        public void setStatus(String status) {
                this.status = status;
        }
}
