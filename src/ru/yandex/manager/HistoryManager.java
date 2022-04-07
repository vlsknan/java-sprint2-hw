package ru.yandex.manager;

import ru.yandex.model.Task;

import java.util.List;

public interface HistoryManager {
    static final int MAX_SIZE_HISTORY = 10;

    void add(Task task);
    List<Task> getHistory();
}
