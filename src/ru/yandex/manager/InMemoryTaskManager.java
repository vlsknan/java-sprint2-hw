package ru.yandex.manager;

import ru.yandex.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int id;

    protected final HashMap<Integer, Task> tasksByID;
    protected final HashMap<Integer, Epic> epicByID;
    protected final HashMap<Integer, Subtask> subtasksByID;
    protected final HistoryManager historyManager;

    public InMemoryTaskManager() {
        id = 1;
        this.tasksByID = new HashMap<>();
        this.subtasksByID = new HashMap<>();
        this.epicByID = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    //получить все задачи
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasksByID.values());
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicByID.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasksByID.values());
    }

    //удалить все задачи
    @Override
    public void deleteAllTasks() {
        tasksByID.clear();
    }

    @Override
    public void deleteAllEpic() {
        epicByID.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasksByID.clear();
    }

    //получить задачу по номеру
    @Override
    public Task getTaskByID(int id) {
        Task task = tasksByID.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic epic = epicByID.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        Subtask subtask = subtasksByID.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    //создание
    @Override
    public Task createTask(Task task) {
        task.setId(id++);
        tasksByID.put(task.getID(), task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(id++);
        epicByID.put(epic.getID(), epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(id++);
        subtasksByID.put(subtask.getID(), subtask);
        return subtask;
    }

    //обновление
    @Override
    public void updateTask(Task task) {
        if (tasksByID.containsKey(task.getID())) {
            tasksByID.put(task.getID(), task);
        }
    }

    @Override
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
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (tasksByID.containsKey(subtask.getID())) {
            tasksByID.put(subtask.getID(), subtask);
        }
    }

    //удалить по номеру
    @Override
    public void deleteTaskByID(int id) {
        tasksByID.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicByID(int id) {
        epicByID.remove(id);
        subtasksByID.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskByID(int id) {
        subtasksByID.remove(id);
    }

    @Override
    public ArrayList<Subtask> getListAllSubtaskEpic (Epic epic) {
        return new ArrayList<>(epic.getSubtasks());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}