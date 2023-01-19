package ru.yandex.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    transient private ArrayList<Subtask> subtasks;

    public Epic(String taskName, int id, String descriptionTask, Status status,
                TypeTask type, int duration, LocalDateTime startTime) {
        super(taskName, id, descriptionTask, status, type, duration, startTime);
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
