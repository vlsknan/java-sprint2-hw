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
                writer.append(toString(entryTask.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, Epic> entryEpic : epicByID.entrySet()) {
                writer.append(toString(entryEpic.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, Subtask> entrySubtask : subtasksByID.entrySet()) {
                writer.append(toString(entrySubtask.getValue()));
                writer.newLine();
            }
            writer.append("\n");
            writer.append(toString(historyManager));
            writer.newLine();
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
                    Task task = fromString(str);
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
                    manager.historyManager.add(task);
                }
                return manager;
            }
        } catch (IOException exp) {
            throw new ManagerSaveException("Произошла ошибка");
        }
    }

    // из задачи строку
    public String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        if (task.getType().equals(TypeTask.SUBTASK)) {
            Subtask subtaskId = (Subtask) task;
            sb.append(task.getID() + "," + task.getType() + "," + task.getTaskName() + "," + task.getStatus() +
                    "," + task.getDescriptionTask() + "," + subtaskId.getEpicID());
        } else {
            sb.append(task.getID() + "," + task.getType() + "," + task.getTaskName() + "," + task.getStatus() +
                    "," + task.getDescriptionTask());
        }
        return sb.toString();
    }

    //из строки в задачу
    public static Task fromString(String str) {
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

    // историю в строку
    static String toString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getID());
            sb.append(",");
        }
        return sb.toString();
    }

    // строку в историю
    static List<Integer> historyFromString(String str) {
        String[] historyId = str.split(",");
        List<Integer> history = new ArrayList<>();
        for (String s : historyId) {
            history.add(Integer.valueOf(s));
        }
        return history;
    }

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault("resources/task.csv");

        Task task1 = new Task("Задача 1", 0, "Описание задачи 1", Status.NEW, TypeTask.TASK);
        manager.createTask(task1);
        Task task2 = new Task("Задача 2", 0, "Описание задачи 2", Status.IN_PROGRESS, TypeTask.TASK);
        manager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", 0, "Описание эпика 1", Status.NO, TypeTask.EPIC);
        manager.createEpic(epic1);
        Epic epic2 = new Epic("Эпик 2", 0, "Описание эпика 2", Status.NO, TypeTask.EPIC);
        manager.createEpic(epic2);

        Subtask subtaskForEpic1_1 = new Subtask("Подзадача 1", 0,
                "Описание подзадачи 1 эпика 1", Status.NEW, epic1.getID(), TypeTask.SUBTASK);
        Subtask subtaskForEpic2_1 = new Subtask("Подзадача 2", 0,
                "Описание подзадачи 1 эпика 2", Status.IN_PROGRESS, epic2.getID(), TypeTask.SUBTASK);
        manager.createSubtask(subtaskForEpic1_1);
        manager.createSubtask(subtaskForEpic2_1);

        manager.getTaskByID(task1.getID());
        manager.getEpicByID(epic1.getID());
        manager.getTaskByID(task2.getID());
        manager.getEpicByID(epic2.getID());

    }
}
