package ru.yandex.manager;

import ru.yandex.manager.history.HistoryManager;
import ru.yandex.manager.history.InMemoryHistoryManager;
import ru.yandex.manager.server.HttpTaskManager;

import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() throws IOException {
        //return new InMemoryTaskManager();
        //return new FileBackedTasksManager("resources/task.csv");
        return new HttpTaskManager("http://localhost:8078");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
