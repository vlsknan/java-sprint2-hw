package ru.yandex.manager.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.*;
import ru.yandex.manager.Managers;
import ru.yandex.manager.TaskManager;
import ru.yandex.manager.server.kv.KVServer;
import ru.yandex.model.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    HttpTaskServer taskServer;
    KVServer kvServer;
    TaskManager manager;
    HttpClient client;
    Task task;
    Epic epic;
    Subtask subtask;
    Gson gson;

    HttpTaskServerTest() throws IOException {
        client = HttpClient.newHttpClient();
        manager = Managers.getDefault();
        gson = HttpTaskServer.getGson();
    }

    @BeforeEach
    void first() throws IOException {
        task = new Task("Задача 1", 0, "Описание задачи 1", Status.NEW,
                TypeTask.TASK, 30, LocalDateTime.of(2022, 05, 25, 12, 30));
        subtask = new Subtask("Подзадача 2", 0,
                "Описание подзадачи 1 эпика 2", Status.IN_PROGRESS, 0,
                TypeTask.SUBTASK, 30, LocalDateTime.parse("30.05.22, 10:30",
                DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm")));
        epic = new Epic("Эпик 1", 0, "Описание эпика 1",
                Status.NO, TypeTask.EPIC, 0, subtask.getStartTime());
        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(subtask);

        kvServer = new KVServer();
        kvServer.start();
        taskServer = new HttpTaskServer();
        taskServer.start();
    }

    @AfterEach
    void afterEach() {
        taskServer.stop();
        kvServer.stop();
    }

    @Test
    @DisplayName("получить приоритетный список задач")
    void getPrioritizedTasksTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("получить все задачи")
    void getAllTasksTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("получить все эпики")
    void getAllEpicTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("получить все подзадачи")
    void getAllSubtasksTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("получить задачу по id")
    void getTaskByIdTest() throws IOException, InterruptedException {
            URI url = URI.create("http://localhost:8080/tasks/task/?id=" + task.getID());
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("получить эпик по id")
    void getEpicByIdTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=" + epic.getID());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("получить подзадачу по id")
    void getSubtaskByIdTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=" + subtask.getID());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("получить историю")
    void getHistoryTest() throws IOException, InterruptedException {
        manager.getTaskByID(task.getID());
        manager.getEpicByID(epic.getID());

        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        assertEquals(2, response.body().length());
    }

    @Test
    @DisplayName("удалить все задачи")
    void deleteAllTaskTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("удалить все эпики")
    void deleteAllEpicTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("удалить все подзадачи")
    void deleteAllSubtaskTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("удалить задачу по id")
    void deleteTaskByIdTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task?id=" + task.getID());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("удалить эпик по id")
    void deleteEpicByIdTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic?id=" + epic.getID());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("удалить подзадачу по id")
    void deleteSubtaskByIdTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask?id=" + subtask.getID());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

}