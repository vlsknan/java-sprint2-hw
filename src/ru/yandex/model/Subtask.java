package ru.yandex.model;

public class Subtask extends Task {
    private int epicID;

    public Subtask(String taskName, int id, String descriptionTask, Status status, int epicID) {
        super(taskName, id, descriptionTask, status);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "subtaskName='" + getTaskName() + '\'' +
                ", id=" + getID() +
                ", descriptionTask='" + getDescriptionTask() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
