package ru.yandex.manager.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.manager.file.FileBackedTasksManager;
import ru.yandex.manager.history.HistoryManager;
import ru.yandex.manager.server.kv.KVTaskClient;
import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;

import java.util.*;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    private final Gson gson =  HttpTaskServer.getGson();
    public HistoryManager history;

    public HttpTaskManager(String kvServerUrl) {
        client = new KVTaskClient(kvServerUrl);
    }

    public HttpTaskManager() {
        client = new KVTaskClient("http://localhost:8078");
        load();
    }

    @Override
    protected void save() {
        client.put("tasks", gson.toJson(new ArrayList<>(tasksByID.values())));
       client.put("epics", gson.toJson(new ArrayList<>(epicByID.values())));
       client.put("subtasks", gson.toJson(new ArrayList<>(subtasksByID.values())));

       client.put("history", gson.toJson(historyManager.getHistory()));
    }

    public void load() {
        String value;
            // для задач
            value = client.load("tasks");
            Map<Integer, Task> jsonTasks = gson.fromJson(value, new TypeToken<ArrayList<Task>>() {
            }.getType());
            tasksByID = jsonTasks;

            // для эпиков
            value = client.load("epics");
            Map<Integer, Epic> jsonEpics = gson.fromJson(value, new TypeToken<ArrayList<Epic>>() {
            }.getType());
            epicByID = jsonEpics;

            // для подзадач
            value = client.load("subtasks");
            Map<Integer, Subtask> jsonSubtasks = gson.fromJson(value, new TypeToken<ArrayList<Subtask>>() {
            }.getType());
            subtasksByID = jsonSubtasks;

            // для истории
            value = client.load("history");
            HistoryManager jsonHistory = gson.fromJson(value, new TypeToken<HistoryManager>() {
            }.getType());
            historyManager = jsonHistory;
    }
}

