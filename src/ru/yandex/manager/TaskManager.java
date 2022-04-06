package ru.yandex.manager;

import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    static final int MAX_SIZE_HISTORY = 10;

    ArrayList<Task> getAllTasks();
    ArrayList<Epic> getAllEpic();
    ArrayList<Subtask> getAllSubtasks();

    void deleteAllTasks();
    void deleteAllEpic();
    void deleteAllSubtasks();

    Task getTaskByID(int id);
    Epic getEpicByID(int id);
    Subtask getSubtaskByID(int id);

    Task createTask(Task task);
    Epic createEpic(Epic epic);
    Subtask createSubtask(Subtask subtask);

    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);

    void deleteTaskByID(int id);
    void deleteEpicByID(String nameEpic);
    void deleteSubtaskByID(int id);

    ArrayList<Subtask> getListAllSubtaskEpic (Epic epic);
    List<Task> getHistory();
}
