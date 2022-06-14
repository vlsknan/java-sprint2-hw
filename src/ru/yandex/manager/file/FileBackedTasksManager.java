package ru.yandex.manager.file;

import ru.yandex.manager.history.HistoryManager;
import ru.yandex.manager.memory.InMemoryTaskManager;
import ru.yandex.model.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.String.valueOf;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private Path file;

    public FileBackedTasksManager(Path path) {
        this.file = Paths.get(valueOf(path));
    }

    public FileBackedTasksManager() {
        this.file = Paths.get("task.csv");
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task getTaskByID(int id) {
        Task task = super.getTaskByID(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic epic = super.getEpicByID(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        Subtask subtask = super.getSubtaskByID(id);
        save();
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskByID(int id) {
        super.deleteTaskByID(id);
        save();
    }

    @Override
    public void deleteEpicByID(int id) {
        super.deleteEpicByID(id);
        save();
    }

    @Override
    public void deleteSubtaskByID(int id) {
        super.deleteSubtaskByID(id);
        save();
    }

    @Override
    public ArrayList<Subtask> getListAllSubtaskEpic(Epic epic) {
        ArrayList<Subtask> subtask = super.getListAllSubtaskEpic(epic);
        save();
        return subtask;
    }

    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(valueOf(file)))) {
            writer.append("id,time,type,name,status,description,epic,duration");
            writer.newLine();

            for (Map.Entry<Integer, Task> entryTask : tasksByID.entrySet()) {
                writer.append(taskToString(entryTask.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, Epic> entryEpic : epicByID.entrySet()) {
                writer.append(taskToString(entryEpic.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, Subtask> entrySubtask : subtasksByID.entrySet()) {
                writer.append(taskToString(entrySubtask.getValue()));
                writer.newLine();
            }
            writer.append("\n").append(historyManagerToString(historyManager));
        } catch (IOException exp) {
            throw new ManagerSaveException("Произошла ошибка работы");
        }
    }

    private void load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(valueOf(file)))) {
            String line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
            }
        } catch (IOException exp) {
            throw new ManagerSaveException("Произошла ошибка работы");
        }
    }

    public static FileBackedTasksManager loadFromFile(String file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(Path.of(file));
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            while (true) {
                String str = br.readLine();
                if (!str.isEmpty()) {
                    Task task = fromStringToTask(str);
                    switch (task.getType()) {
                        case TASK:
                            manager.tasksByID.put(task.getID(), task);
                            break;
                        case EPIC:
                            manager.epicByID.put(task.getID(), (Epic) task);
                            break;
                        case SUBTASK:
                            manager.subtasksByID.put(task.getID(), (Subtask) task);
                            break;
                    }
                } else {
                    break;
                }
            }
            String line = br.readLine();
            if (line != null) {
                List<Integer> history = historyFromString(line);
                for (Integer id : history) {
                    for (Integer idT : manager.tasksByID.keySet()) {
                        if (id == idT)
                            manager.historyManager.add(manager.tasksByID.get(id));
                    }
                    for (Integer idE : manager.epicByID.keySet()){
                        if (id == idE)
                            manager.historyManager.add(manager.epicByID.get(id));
                    }
                        for (Integer idS : manager.subtasksByID.keySet()){
                            if (id == idS)
                                manager.historyManager.add(manager.subtasksByID.get(id));
                        }
                }
            }
        } catch (IOException exp) {
            throw new ManagerSaveException("Произошла ошибка");
        }
        return manager;
    }

    //из задачи строку
    private String taskToString(Task task) {
        StringBuilder sb = new StringBuilder();
        if (task.getType().equals(TypeTask.SUBTASK)) {
            Subtask subtaskId = (Subtask) task;
            sb.append(task.getID()).append(",").append(subtaskId.getStartTime()).append(",").append(task.getType()).append(",").append(task.getTaskName()).
                    append(",").append(task.getStatus()).append(",").append(task.getDescriptionTask()).
                    append(",").append(subtaskId.getEpicID()).append(",").append(subtaskId.getDuration());
        } else {
            sb.append(task.getID()).append(",").append(task.getStartTime()).append(",").append(task.getType()).append(",").append(task.getTaskName()).
                    append(",").append(task.getStatus()).append(",").append(task.getDescriptionTask()).
                    append(",").append(task.getDuration());
        }
        return sb.toString();
    }

    //историю в строку
    private static String historyManagerToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getID()).append(",");
        }
        return sb.toString();
    }

    //из строки в задачу
    public static Task fromStringToTask(String str) {
        String[] dataStr = str.split(",");

        int id = Integer.parseInt(dataStr[0]);
        TypeTask taskType = TypeTask.valueOf(dataStr[2]);
        String name = dataStr[3];
        Status status = Status.valueOf(dataStr[4]);
        String descriptionTask = dataStr[5];
        int duration = Integer.parseInt(dataStr[6]);
        LocalDateTime startTime = LocalDateTime.parse(dataStr[1]);

        if (taskType.equals(TypeTask.TASK)) {
            return new Task(name, id, descriptionTask, status, taskType, duration, startTime);
        } else if (taskType.equals(TypeTask.EPIC)) {
            return new Epic(name, id, descriptionTask, status, taskType, duration, startTime);
        } else {
            int epicId = Integer.parseInt(dataStr[6]);
            startTime = LocalDateTime.parse(dataStr[1]);
            return new Subtask(name, id, descriptionTask, status, epicId, taskType, duration, startTime);
        }
    }

    // строку в историю
    private static List<Integer> historyFromString(String str) {
        String[] historyId = str.split(",");
        List<Integer> history = new ArrayList<>();
        for (String s : historyId) {
            history.add(Integer.parseInt(s));
        }
        return history;
    }
}
