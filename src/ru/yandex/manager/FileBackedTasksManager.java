package ru.yandex.manager;

import ru.yandex.model.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.String.valueOf;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private Path file;

    public FileBackedTasksManager(Path path) {
        this.file = Paths.get(valueOf(path));
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> task = super.getAllTasks();
        save();
        return task;
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        ArrayList<Epic> epic = super.getAllEpic();
        save();
        return epic;
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> subtask = super.getAllSubtasks();
        save();
        return subtask;
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

    @Override
    public List<Task> getHistory() {
        List<Task> history = super.getHistory();
        save();
        return history;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(valueOf(file)))) {
            writer.append("id,type,name,status,description,epic");
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
            br.readLine();
            String line = br.readLine();
            if (line != null) {
                List<Integer> history = historyFromString(line);
                for (Integer id : history) {
                manager.historyManager.add(manager.tasksByID.get(id));
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
            sb.append(task.getID()).append(",").append(task.getType()).append(",").append(task.getTaskName()).
                    append(",").append(task.getStatus()).append(",").append(task.getDescriptionTask()).
                    append(",").append(subtaskId.getEpicID());
        } else {
            sb.append(task.getID()).append(",").append(task.getType()).append(",").append(task.getTaskName()).
                    append(",").append(task.getStatus()).append(",").append(task.getDescriptionTask());
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
        TypeTask taskType = TypeTask.valueOf(dataStr[1]);
        String name = dataStr[2];
        Status status = Status.valueOf(dataStr[3]);
        String descriptionTask = dataStr[4];

        if (taskType.equals(TypeTask.TASK)) {
            return new Task(name, id, descriptionTask, status, taskType);
        } else if (taskType.equals(TypeTask.EPIC)) {
            return new Epic(name, id, descriptionTask, status, taskType);
        } else {
            int epicId = Integer.parseInt(dataStr[5]);
            return new Subtask(name, id, descriptionTask, status, epicId, taskType);
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
