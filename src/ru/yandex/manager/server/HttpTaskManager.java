package ru.yandex.manager.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.manager.file.FileBackedTasksManager;
import ru.yandex.manager.server.kv.KVTaskClient;
import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    private final Gson gson;
    public Map<Integer, Task> tasks;
    public Map<Integer, Epic> epics;
    public Map<Integer, Subtask> subtasks;
    public ArrayList<Integer> history;

    public HttpTaskManager(String kvServerUrl) {
        client = new KVTaskClient(kvServerUrl);
        gson = HttpTaskServer.getGson();
    }

    @Override
    protected void save() {
       client.put("tasks", gson.toJson(tasksByID));
       client.put("epics", gson.toJson(epicByID));
       client.put("subtasks", gson.toJson(subtasksByID));

       client.put("history", gson.toJson(historyManager.getHistory()));
    }

    protected void load() {
        String value;
            // для задач
            value = client.load("tasks");
            Map<Integer, Task> jsonTasks = gson.fromJson(value, new TypeToken<HashMap<Integer, Task>>() {
            }.getType());
            tasks = jsonTasks;

            // для эпиков
            value = client.load("epics");
            Map<Integer, Epic> jsonEpics = gson.fromJson(value, new TypeToken<HashMap<Integer, Epic>>() {
            }.getType());
            epics = jsonEpics;

            // для подзадач
            value = client.load("subtasks");
            Map<Integer, Subtask> jsonSubtasks = gson.fromJson(value, new TypeToken<HashMap<Integer, Subtask>>() {
            }.getType());
            subtasks = jsonSubtasks;

            // для истории
            value = client.load("history");
            ArrayList<Integer> jsonHistory = gson.fromJson(value, new TypeToken<ArrayList<Integer>>() {
            }.getType());
            history = jsonHistory;
    }
}

