package ru.yandex.manager;

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
