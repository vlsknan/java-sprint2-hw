package ru.yandex.manager.server;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.manager.server.kv.KVServer;
import ru.yandex.manager.server.kv.KVTaskClient;
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
            Gson gson = HttpTaskServer.getGson();
            Task task = new Task("Задача 1", 0, "Описание задачи 1",
                    Status.NEW, TypeTask.TASK, 30, LocalDateTime.parse("25.05.22, 12:30", format));
            manager.createTask(task);
            KVTaskClient client = new KVTaskClient("http://localhost:8078");
            client.put("tasks", gson.toJson(task));

            HttpTaskManager manager2 = new HttpTaskManager();

            assertEquals(task, manager2.getTaskByID(task.getID()));
        }
}