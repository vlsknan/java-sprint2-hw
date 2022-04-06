package ru.yandex.model;

import java.util.Objects;

public class Task {
        private String taskName;    //название задач
        private int id; //идентификатор
        private String descriptionTask; //описание
        private Status status;// статус задачи

        public Task(String taskName, int id, String descriptionTask, Status status) {
                this.taskName = taskName;
                this.id = id;
                this.descriptionTask = descriptionTask;
                this.status = status;
        }

        public Status getStatus() {
                return status;
        }

        public void setStatus(Status status) {
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

        @Override
        public String toString() {
                return "Task{" +
                        "taskName='" + taskName + '\'' +
                        ", id=" + id +
                        ", descriptionTask='" + descriptionTask + '\'' +
                        ", status=" + status +
                        '}';
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Task task = (Task) o;
                return id == task.id && Objects.equals(taskName, task.taskName)
                        && Objects.equals(descriptionTask, task.descriptionTask)
                        && Objects.equals(status, task.status);
        }

        @Override
        public int hashCode() {
                return Objects.hash(taskName, id, descriptionTask, status);
        }
}
