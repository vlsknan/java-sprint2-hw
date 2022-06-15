package ru.yandex;

import ru.yandex.manager.*;
import ru.yandex.manager.server.HttpTaskManager;
import ru.yandex.manager.server.HttpTaskServer;
import ru.yandex.manager.server.kv.KVServer;
import ru.yandex.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
        public static void main(String[] args) throws IOException {

            KVServer kvserver = new KVServer();
            kvserver.start();

            HttpTaskServer httpTaskServer = new HttpTaskServer();
            httpTaskServer.start();

            TaskManager manager = Managers.getDefault();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm");

            Task task1 = new Task("Задача 1", 0, "Описание задачи 1",
                    Status.NEW, TypeTask.TASK, 30, LocalDateTime.parse("25.05.22, 12:30", format));
            manager.createTask(task1);

            Epic epic1 = new Epic("Эпик 1", 0, "Описание эпика 1",
                    Status.NO, TypeTask.EPIC, 0, LocalDateTime.parse("17.06.22, 16:00", format));
            manager.createEpic(epic1);

            Subtask subtaskForEpic1_1 = new Subtask("Подзадача 1", 0,
                    "Описание подзадачи 1 эпика 1", Status.NEW, epic1.getID(),
                    TypeTask.SUBTASK, 70, LocalDateTime.parse("17.06.22, 16:00", format));
            manager.createSubtask(subtaskForEpic1_1);

            manager.getTaskByID(task1.getID());
            manager.getEpicByID(epic1.getID());


            HttpTaskManager manager2 = new HttpTaskManager();
            //manager2.load();
            System.out.println(manager2.getAllTasks());
            System.out.println(manager2.getAllEpic());
            System.out.println(manager2.getAllSubtasks());
            System.out.println(manager2.getHistory());
        }
}
