package ru.yandex.manager.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.manager.server.kv.KVServer;
import ru.yandex.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

    class HttpTaskManagerTest {
        KVServer kvServer;
        HttpTaskManager manager;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm");

        @BeforeEach
        void beforeEach() throws IOException {
            kvServer = new KVServer();
            kvServer.start();
            manager = new HttpTaskManager("http://localhost:8078");
        }

        @AfterEach
        void afterEach() {
            kvServer.stop();
        }

        @Test
        @DisplayName("сохранить на сервер")
        void saveTest() {
            Task task = new Task("Задача 1", 0, "Описание задачи 1",
                    Status.NEW, TypeTask.TASK, 30, LocalDateTime.parse("25.05.22, 12:30", format));
            manager.createTask(task);

            assertEquals(task, manager.getTaskByID(task.getID()));
        }

        @Test
        @DisplayName("загрузить с сервера")
        void loadTest() {
            Task task = new Task("Задача 1", 1, "Описание задачи 1",
                    Status.NEW, TypeTask.TASK, 30, LocalDateTime.parse("25.05.22, 12:30", format));
            task = manager.createTask(task);

            HttpTaskManager manager2 = new HttpTaskManager("http://localhost:8078");
            manager2.load();

            var t = manager2.getTaskByID(task.getID());
            assertEquals(task, manager2.getTaskByID(task.getID()));
        }
}