package ru.yandex.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;
    private int duration;

    public Epic(String taskName, int id, String descriptionTask, Status status,
                TypeTask type, int duration, LocalDateTime startTime) {
        super(taskName, id, descriptionTask, status, type, duration, startTime);
        this.duration = duration;
    }

    public void putSubtask(Subtask subtask) {
        if (subtasks == null) {
            subtasks = new ArrayList<>();
        }
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public void setDurationEpic(int duration) {
        this.duration = duration;
    }

    public int getDurationEpic() {
        return duration;
    }

    public LocalDateTime getEndTimeEpic() {
        int sum = 0;
        for (Subtask sub : getSubtasks()) {
            sum += sub.getDuration();
        }
        return getStartTime().plusMinutes(sum);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "epicName='" + getTaskName() + '\'' +
                ", id=" + getID() +
                ", descriptionTask='" + getDescriptionTask() + '\'' +
                ", status=" + getStatus() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                '}';
    }
}
