package ru.yandex;

import ru.yandex.manager.server.HttpTaskServer;
import ru.yandex.manager.server.kv.KVServer;

import java.io.IOException;

public class Main {
        public static void main(String[] args) throws IOException {

            KVServer kvserver = new KVServer();
            kvserver.start();

            HttpTaskServer httpTaskServer = new HttpTaskServer();
            httpTaskServer.start();

//            TaskManager manager = Managers.getDefault();
//            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm");
//
//            Task task1 = new Task("Задача 1", 0, "Описание задачи 1",
//                    Status.NEW, TypeTask.TASK, 30, LocalDateTime.parse("25.05.22, 12:30", format));
//            manager.createTask(task1);
//            Task task2 = new Task("Задача 2", 0, "Описание задачи 2",
//                    Status.IN_PROGRESS, TypeTask.TASK, 20, LocalDateTime.parse("01.06.22, 23:00", format));
//            manager.createTask(task2);
//
//            Epic epic1 = new Epic("Эпик 1", 0, "Описание эпика 1",
//                    Status.NO, TypeTask.EPIC, 0, LocalDateTime.parse("17.06.22, 16:00", format));
//            manager.createEpic(epic1);
//            Epic epic2 = new Epic("Эпик 2", 0, "Описание эпика 2",
//                    Status.NO, TypeTask.EPIC, 0, LocalDateTime.parse("30.05.22, 10:30", format));
//            manager.createEpic(epic2);
//
//            Subtask subtaskForEpic1_1 = new Subtask("Подзадача 1", 0,
//                    "Описание подзадачи 1 эпика 1", Status.NEW, epic1.getID(),
//                    TypeTask.SUBTASK, 70, LocalDateTime.parse("17.06.22, 16:00", format));
//
//            Subtask subtaskForEpic2_1 = new Subtask("Подзадача 2", 0,
//                    "Описание подзадачи 1 эпика 2", Status.IN_PROGRESS, epic2.getID(),
//                    TypeTask.SUBTASK, 30, LocalDateTime.parse("30.05.22, 10:30", format));
//            manager.createSubtask(subtaskForEpic1_1);
//            manager.createSubtask(subtaskForEpic2_1);
//
//            manager.getTaskByID(task1.getID());
//            manager.getEpicByID(epic1.getID());
//            manager.getTaskByID(task2.getID());
//            manager.getEpicByID(epic2.getID());

//            TaskManager manager2 = FileBackedTasksManager.loadFromFile("resources/task.csv");
//            System.out.println(manager2.getAllTasks());
//            System.out.println(manager2.getAllEpic());
//            System.out.println(manager2.getAllSubtasks());
//            System.out.println(manager2.getHistory());
        }
}
