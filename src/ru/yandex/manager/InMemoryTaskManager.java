package ru.yandex.manager;

import ru.yandex.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int id;

    private HashMap<Integer, Task> tasksByID;
    private HashMap<Integer, Epic> epicByID;
    private HashMap<Integer, Subtask> subtasksByID;
    //private  List<Task> history;
    private TaskManager historyManager;

    public InMemoryTaskManager() {
        id = 1;
        this.tasksByID = new HashMap<>();
        this.subtasksByID = new HashMap<>();
        this.epicByID = new HashMap<>();
        //this.history = new ArrayList<>();
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
        return tasksByID.get(id);
    }

    @Override
    public Epic getEpicByID(int id) {
        if (MAX_SIZE_HISTORY < historyManager.getHistory()) {
            history.remove(0);
            history.add();
        } else {
            history.add(getTaskByID(id));
        }
        return epicByID.get(id);
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        if (history.size() > MAX_SIZE_HISTORY) {
            history.remove(0);
            history.add(getSubtaskByID(id));
        } else {
            history.add(getTaskByID(id));
        }
        return subtasksByID.get(id);
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
        } else {
            return;
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
        } else {
            return;
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (tasksByID.containsKey(subtask.getID())) {
            tasksByID.put(subtask.getID(), subtask);
        } else {
            return;
        }
    }

    //удалить по номеру
    @Override
    public void deleteTaskByID(int id) {
        tasksByID.remove(id);
    }

    @Override
    public void deleteEpicByID(String nameEpic) {
        epicByID.remove(nameEpic);
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
