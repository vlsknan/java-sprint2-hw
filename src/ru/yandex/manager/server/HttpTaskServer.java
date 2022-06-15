package ru.yandex.manager.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.manager.Managers;
import ru.yandex.manager.TaskManager;
import ru.yandex.model.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpServer server;
    private final TaskManager manager;
    private final Gson gson;

    public HttpTaskServer() throws IOException {
        server = HttpServer.create();
        manager = Managers.getDefault();
        gson = getGson();

        server.bind(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", this::handle);
        server.createContext("/tasks/history", this::handleHistory);

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void handle(HttpExchange exchange) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            String path = exchange.getRequestURI().getPath();
            String[] split = path.split("/");
            String method = exchange.getRequestMethod();
            String id = exchange.getRequestURI().getQuery();
            String response = "";
            switch (method) {
                case "GET":
                    if (path.endsWith("/tasks")) {
                        response = gson.toJson(manager.getPrioritizedTasks());
                        exchange.sendResponseHeaders(200, 0);
                    }
                    if (split.length == 3 && id == null) {
                        if (path.endsWith("/task")) {
                            response = gson.toJson(manager.getAllTasks());
                            exchange.sendResponseHeaders(200, 0);
                        } else if (path.endsWith("/epic")) {
                            response = gson.toJson(manager.getAllEpic());
                            exchange.sendResponseHeaders(200, 0);
                        } else if (path.endsWith("/subtask")) {
                            response = gson.toJson(manager.getAllSubtasks());
                            exchange.sendResponseHeaders(200, 0);
                        }
                    } else {
                        int intId = Integer.parseInt(id.split("=")[1]);
                        if (path.contains("/task")) {
                            response = gson.toJson(manager.getTaskByID(intId));
                            exchange.sendResponseHeaders(200, 0);
                        } else if (path.contains("/epic")) {
                            response = gson.toJson(manager.getEpicByID(intId));
                            exchange.sendResponseHeaders(200, 0);
                        } else if (path.contains("/subtask")) {
                            response = gson.toJson(manager.getSubtaskByID(intId));
                            exchange.sendResponseHeaders(200, 0);
                        }
                    }
                    break;
                case "POST":
                    String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                    if (split.length == 3 && id == null) {
                        if (path.endsWith("/task")) {
                            manager.createTask(gson.fromJson(body, Task.class));
                            response = gson.toJson("Задача создана");
                            exchange.sendResponseHeaders(200, 0);
                        } else if (path.endsWith("/epic")) {
                            manager.createEpic(gson.fromJson(body, Epic.class));
                            response = gson.toJson("Эпик создан");
                            exchange.sendResponseHeaders(200, 0);
                        } else if (path.endsWith("/subtask")) {
                            manager.createSubtask(gson.fromJson(body, Subtask.class));
                            response = gson.toJson("Подзадача создана");
                            exchange.sendResponseHeaders(200, 0);
                        }
                    } else {
                        int intId = Integer.parseInt(id.split("=")[1]);
                        if (path.contains("/task")) {
                            for (Task task : manager.getAllTasks()) {
                                if (task.getID() == intId) {
                                    manager.updateTask(gson.fromJson(body, Task.class));
                                    response = gson.toJson("Задача обновлена");
                                    exchange.sendResponseHeaders(200, 0);
                                } else {
                                    manager.createTask(gson.fromJson(body, Task.class));
                                    response = gson.toJson("Задача создана");
                                    exchange.sendResponseHeaders(200, 0);
                                }
                            }
                        } else if (path.contains("/epic")) {
                            for (Epic epic : manager.getAllEpic()) {
                                if (epic.getID() == intId) {
                                    manager.updateEpic(gson.fromJson(body, Epic.class));
                                    response = gson.toJson("Эпик обновлен");
                                    exchange.sendResponseHeaders(200, 0);
                                } else {
                                    manager.createEpic(gson.fromJson(body, Epic.class));
                                    response = gson.toJson("Эпик создан");
                                    exchange.sendResponseHeaders(200, 0);
                                }
                            }
                        } else if (path.contains("/subtask")) {
                            for (Subtask subtask : manager.getAllSubtasks()) {
                                if (subtask.getID() == intId) {
                                    manager.updateSubtask(gson.fromJson(body, Subtask.class));
                                    response = gson.toJson("Подзадача обновлена");
                                    exchange.sendResponseHeaders(200, 0);
                                } else {
                                    manager.createSubtask(gson.fromJson(body, Subtask.class));
                                    response = gson.toJson("Подзадача создана");
                                    exchange.sendResponseHeaders(200, 0);
                                }
                            }
                        }
                    }
                    break;
                case "DELETE":
                    if (split.length == 3 && id == null) {
                        if (path.endsWith("/task")) {
                            manager.deleteAllTasks();
                            response = gson.toJson("Все задачи удалены");
                            exchange.sendResponseHeaders(200, 0);
                        } else if (path.endsWith("/epic")) {
                            manager.deleteAllEpic();
                            response = gson.toJson("Все эпики удалены");
                            exchange.sendResponseHeaders(200, 0);
                        } else if (path.endsWith("/subtask")) {
                            manager.deleteAllSubtasks();
                            response = gson.toJson("Все подзадачи удалены");
                            exchange.sendResponseHeaders(200, 0);
                        }
                    } else {
                        int intId = Integer.parseInt(id.split("=")[1]);
                        if (path.contains("/task")) {
                            manager.deleteTaskByID(intId);
                            response = gson.toJson("Задача с id " + intId + " удалена");
                            exchange.sendResponseHeaders(200, 0);
                        } else if (path.contains("/epic")) {
                            manager.deleteEpicByID(intId);
                            response = gson.toJson("Эпик с id " + intId + " удален");
                            exchange.sendResponseHeaders(200, 0);
                        } else if (path.contains("/subtask")) {
                            manager.deleteSubtaskByID(intId);
                            response = gson.toJson("Подзадача с id " + intId + " удалена");
                            exchange.sendResponseHeaders(200, 0);
                        }
                    }
                    break;
                default:
                    response = gson.toJson("Недопустимый метод");
                    exchange.sendResponseHeaders(405, 0);
                    break;
            }
                    os.write(response.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleHistory(HttpExchange exchange) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            String response = gson.toJson(manager.getHistory());
            exchange.sendResponseHeaders(200, 0);
            os.write(response.getBytes());
        } catch (IOException e) {
            exchange.sendResponseHeaders(404, 0);
        }
    }

    public static Gson getGson() {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gb.serializeNulls();
        gb.enableComplexMapKeySerialization();
        return gb.create();
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}
