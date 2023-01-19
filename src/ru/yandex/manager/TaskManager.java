package ru.yandex.manager;

import ru.yandex.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface TaskManager {
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
    void deleteEpicByID(int id);
    void deleteSubtaskByID(int id);

    ArrayList<Subtask> getListAllSubtaskEpic (Epic epic);
    List<Task> getHistory();

    Map<LocalDateTime, Task> getPrioritizedTasks();

}