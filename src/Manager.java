package ru.yandex.manager;

import ru.yandex.model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int id;

    private HashMap<Integer, Task> tasksByID;
    private HashMap<Integer, Epic> epicByID;
    private HashMap<Integer, Subtask> subtasksByID;


    public Manager() {
        id = 1;
        this.tasksByID = new HashMap<>();
        this.subtasksByID = new HashMap<>();
        this.epicByID = new HashMap<>();
    }

    //получить все задачи
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasksByID.values());
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicByID.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasksByID.values());
    }

    //удалить все задачи
    public void deleteAllTasks() {
        tasksByID.clear();
    }

    public void deleteAllEpic() {
            epicByID.clear();
    }

    public void deleteAllSubtasks() {
        subtasksByID.clear();
    }

    //получить задачу по номеру
    public Task getTaskByID(int id) {
        return tasksByID.get(id);
    }

    public Epic getEpicByID(int id) {
        return epicByID.get(id);
    }

    public Subtask getSubtaskByID(int id) {
        return subtasksByID.get(id);
    }

    //создание
    public Task createTask(Task task) {
        task.setId(id++);
        tasksByID.put(task.getID(), task);
        return task;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(id++);
        epicByID.put(epic.getID(), epic);
        return epic;
    }

    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(id++);
        subtasksByID.put(subtask.getID(), subtask);
        return subtask;
    }

    //обновление
    public void updateTask(Task task) {
        if (tasksByID.containsKey(task.getID())) {
            tasksByID.put(task.getID(), task);
        } else {
            return;
        }
    }

    public void updateEpic(Epic epic) {
        if (epicByID.containsKey(epic.getID())) {
            int countNew = 0;
            int countDone = 0;
            for (Subtask subtaskStatus : epic.getSubtasks()) {
                if (subtaskStatus == null || subtaskStatus.getStatus() == Status.NEW) {
                countNew++;
                } else {
                countDone++;
                }
            }
            if (countDone == epic.getSubtasks().size()) {
                epic.setStatus(Status.DONE);
            } else if (countNew == epic.getSubtasks().size()) {
                epic.setStatus(Status.NEW);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
            epicByID.put(epic.getID(), epic);
        } else {
            return;
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (tasksByID.containsKey(subtask.getID())) {
            tasksByID.put(subtask.getID(), subtask);
        } else {
            return;
        }
    }

    //удалить по номеру
    public void deleteTaskByID(int id) {
        tasksByID.remove(id);
    }

    public void deleteEpicByID(String nameEpic) {
        epicByID.remove(nameEpic);
    }

    public void deleteSubtaskByID(int id) {
        subtasksByID.remove(id);
    }

    public ArrayList<Subtask> getListAllSubtaskEpic (Epic epic) {
        return new ArrayList<>(epic.getSubtasks());
    }
}
