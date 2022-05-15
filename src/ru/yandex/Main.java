package ru.yandex;

import ru.yandex.manager.*;
import ru.yandex.model.*;

public class Main {
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

            TaskManager manager2 = FileBackedTasksManager.loadFromFile("resources/task.csv");
            System.out.println(manager2.getAllTasks());
            System.out.println(manager2.getAllEpic());
            System.out.println(manager2.getAllSubtasks());
            System.out.println(manager2.getHistory());
        }
}
