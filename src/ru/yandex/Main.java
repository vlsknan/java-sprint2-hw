package ru.yandex;

import ru.yandex.manager.Managers;
import ru.yandex.manager.TaskManager;
import ru.yandex.model.*;

public class Main {
        public static void main(String[] args) {

                TaskManager manager = Managers.getDefault();

                Task task1 = new Task("Задача 1", 0, "Собрать вещи для переезда", Status.NEW);
                manager.createTask(task1);
                Task task2 = new Task("Задача 2", 0, "-", Status.IN_PROGRESS);
                manager.createTask(task2);
                System.out.println("History: " + manager.getHistory());

                Epic epic1 = new Epic("Эпик 1", 0, "Выучить все экзамены", Status.NO);
                manager.createEpic(epic1);

                Subtask subtaskForEpic1_1 = new Subtask("Мат анализ", 0, "-", Status.NEW,
                        epic1.getID());
                Subtask subtaskForEpic1_2 = new Subtask("Философия", 0, "Осталось выучить 3 билета",
                        Status.IN_PROGRESS, epic1.getID());
                Subtask subtaskForEpic1_3 = new Subtask("Помыть полы", 0, "пусть будет чисто",
                        Status.DONE, epic1.getID());
                manager.createSubtask(subtaskForEpic1_1);
                manager.createSubtask(subtaskForEpic1_2);
                manager.createSubtask(subtaskForEpic1_3);

                Epic epic2 = new Epic("Эпик 2", 0, "-", Status.NO);
                manager.createEpic(epic2);

                manager.getTaskByID(task2.getID());
                System.out.println("HISTORY: " + manager.getHistory());
                manager.getEpicByID(epic2.getID());
                System.out.println("HISTORY: " + manager.getHistory());
                manager.getTaskByID(task1.getID());
                System.out.println("HISTORY: " + manager.getHistory());
                manager.getTaskByID(task2.getID());
                System.out.println("HISTORY: " + manager.getHistory());
                manager.getEpicByID(epic1.getID());
                System.out.println("HISTORY: " + manager.getHistory());
                manager.getEpicByID(epic2.getID());
                System.out.println("HISTORY: " + manager.getHistory());

                manager.deleteTaskByID(task2.getID());
                System.out.println("HISTORY: " + manager.getHistory());
                manager.deleteEpicByID(epic1.getID());
                System.out.println("HISTORY: " + manager.getHistory());

                System.out.println(manager.getAllEpic());
                System.out.println(manager.getAllTasks());
        }
}
