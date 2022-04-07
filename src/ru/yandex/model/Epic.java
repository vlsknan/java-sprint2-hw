package ru.yandex.model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String taskName, int id, String descriptionTask, Status status) {
        super(taskName, id, descriptionTask, status);
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

    @Override
    public String toString() {
        return "Epic{" +
                "epicName='" + getTaskName() + '\'' +
                ", id=" + getID() +
                ", descriptionTask='" + getDescriptionTask() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
