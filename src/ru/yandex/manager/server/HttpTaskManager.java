package ru.yandex.manager.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.manager.file.FileBackedTasksManager;
import ru.yandex.manager.history.HistoryManager;
import ru.yandex.manager.server.kv.KVTaskClient;
import ru.yandex.model.Epic;
import ru.yandex.model.Subtask;
import ru.yandex.model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    private final Gson gson = HttpTaskServer.getGson();
    public HistoryManager history;

    public HttpTaskManager(String kvServerUrl) {
        super();
        client = new KVTaskClient(kvServerUrl);
    }

    public HttpTaskManager() {
        super();
        client = new KVTaskClient("http://localhost:8078");
        load();
    }

    @Override
    protected void save() {
        client.put("tasks", gson.toJson(tasksByID));
        client.put("epics", gson.toJson(epicByID));
        client.put("subtasks", gson.toJson(subtasksByID));
        client.put("history", gson.toJson(historyManager.getHistory()));
    }

    public void load() {
        String value;
        // для задач
        value = client.load("tasks");
        HashMap<Integer, Task> jsonTasks = gson.fromJson(cleanJSON(value), new TypeToken<HashMap<Integer, Task>>() {
        }.getType());
        tasksByID = jsonTasks;

        // для эпиков
        value = client.load("epics");
        Map<Integer, Epic> jsonEpics = gson.fromJson(cleanJSON(value), new TypeToken<HashMap<Integer, Epic>>() {
        }.getType());
        epicByID = jsonEpics;

        // для подзадач
        value = client.load("subtasks");
        Map<Integer, Subtask> jsonSubtasks = gson.fromJson(cleanJSON(value), new TypeToken<HashMap<Integer, Subtask>>() {
        }.getType());
        subtasksByID = jsonSubtasks;

        // для истории
        value = client.load("history");
        List<Task> jsonHistory = gson.fromJson(cleanJSON(value), new TypeToken<List<Task>>() {
        }.getType());
        jsonHistory.forEach(historyManager::add);
    }

    private String cleanJSON(String json) {
        json = json.substring(1, json.length() - 1);
        json = json.replaceAll("\\\\\"", "\"");
        return json;
    }
}

