package ru.yandex.manager;

import ru.yandex.manager.file.FileBackedTasksManager;
import ru.yandex.manager.history.HistoryManager;
import ru.yandex.manager.history.InMemoryHistoryManager;

import java.nio.file.Path;

public class Managers {
    public static TaskManager getDefault(String s) {
        //return new InMemoryTaskManager();
        return new FileBackedTasksManager(Path.of(s));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
