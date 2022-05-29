package ru.yandex.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private String taskName;    //название задач
    private int id; //идентификатор
    private String descriptionTask; //описание
    private Status status;// статус задачи
    private TypeTask type; // тип задачи
    private int duration; // продолжительность задачи
    private LocalDateTime startTime; // дата начала задачи
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm");

    public Task(String taskName, int id, String descriptionTask, Status status, TypeTask type,
                int duration, LocalDateTime startTime) {
        this.taskName = taskName;
        this.id = id;
        this.descriptionTask = descriptionTask;
        this.status = status;
        this.type = type;
        this.duration = duration;
        this.startTime = startTime;
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

    public TypeTask getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
            return startTime.plusMinutes(duration);
    }



    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", id=" + id +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", duration=" + duration +
                ", startTime=" + startTime +
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
