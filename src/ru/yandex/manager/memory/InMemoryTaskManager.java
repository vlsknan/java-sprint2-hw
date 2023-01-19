package ru.yandex.manager.memory;

import ru.yandex.manager.Managers;
import ru.yandex.manager.TaskManager;
import ru.yandex.manager.history.HistoryManager;
import ru.yandex.model.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id;

    protected Map<Integer, Task> tasksByID;
    protected Map<Integer, Epic> epicByID;
    protected Map<Integer, Subtask> subtasksByID;
    protected HistoryManager historyManager;
    private final Map<LocalDateTime, Task> prioritizedTasks;

    public InMemoryTaskManager() {
        id = 1;
        this.tasksByID = new HashMap<>();
        this.subtasksByID = new HashMap<>();
        this.epicByID = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
        this.prioritizedTasks = new TreeMap<>();
    }

    //получить все задачи
    @Override
    public ArrayList<Task> getAllTasks() {
        if (tasksByID == null) {
            tasksByID = new HashMap<>();
        }
        return new ArrayList<>(tasksByID.values());
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        if (epicByID == null) {
            epicByID = new HashMap<>();
        }
        return new ArrayList<>(epicByID.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        if (subtasksByID == null) {
            subtasksByID = new HashMap<>();
        }
        return new ArrayList<>(subtasksByID.values());
    }

    //удалить все задачи
    @Override
    public void deleteAllTasks() {
        tasksByID.clear();
        prioritizedTasks.clear();
    }

    @Override
    public void deleteAllEpic() {
        epicByID.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasksByID.clear();
        prioritizedTasks.clear();
    }

    //получить задачу по номеру
    @Override
    public Task getTaskByID(int id) {
        if (tasksByID == null) {
            tasksByID = new HashMap<>();
        }
        if (tasksByID.get(id) != null) {
            Task task = tasksByID.get(id);
            if (historyManager == null) {
                historyManager = Managers.getDefaultHistory();
            }
            historyManager.add(task);
            return task;
        }
        return null;
    }

    @Override
    public Epic getEpicByID(int id) {
        if (epicByID == null) {
            epicByID = new HashMap<>();
        }
        if (epicByID.get(id) != null) {
            Epic epic = epicByID.get(id);
            historyManager.add(epic);
            return epic;
        }
        return null;
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        if (subtasksByID == null) {
            subtasksByID = new HashMap<>();
        }
        if (subtasksByID.get(id) != null) {
            Subtask subtask = subtasksByID.get(id);
            historyManager.add(subtask);
            return subtask;
        }
        return null;
    }

    //создание
    @Override
    public Task createTask(Task task) {
        if (tasksByID == null) {
            tasksByID = new HashMap<>();
        }
        task.setId(id++);
        tasksByID.put(task.getID(), task);
        prioritizedTasks.put(task.getStartTime(), task);
        if (tasksByID.size() > 1) {
            searchForTheIntersectionOfTasks(task);// поиск пересечения
        }
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (epicByID == null) {
            epicByID = new HashMap<>();
        }
        epic.setId(id++);
        epicByID.put(epic.getID(), epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (subtasksByID == null) {
            subtasksByID = new HashMap<>();
        }
        subtask.setId(id++);
        subtasksByID.put(subtask.getID(), subtask);
        prioritizedTasks.put(subtask.getStartTime(), subtask);
        if (subtasksByID.size() > 1) {
            searchForTheIntersectionOfTasks(subtask);// поиск пересечения
        }
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
                } else if (subtaskStatus.getStatus() == Status.DONE){
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
        if (subtasksByID.containsKey(subtask.getID())) {
            subtasksByID.put(subtask.getID(), subtask);
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
        historyManager.remove(id);
    }

    @Override
    public ArrayList<Subtask> getListAllSubtaskEpic (Epic epic) {
        return new ArrayList<>(epic.getSubtasks());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Map<LocalDateTime,Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    // поиск пересечения задач
    private void searchForTheIntersectionOfTasks(Task task) {
        try {
            Map<LocalDateTime, Task > allTasks = getPrioritizedTasks();
            for (LocalDateTime startTime : allTasks.keySet()) {
                if (startTime != task.getStartTime() && startTime != task.getEndTime())
                    if (!task.getStartTime().isBefore(startTime) && !task.getEndTime().isAfter(startTime)) {
                        return;
                    }
            }
        } catch (RuntimeException ex) {
            throw new IntersectionException("Найдено пересечение. Задача не может быть создана");
        }
    }
}