package ru.yandex.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    private InMemoryHistoryManager historyManager;
    private TaskManager taskManager;
    private Task task;
    private Epic epic;
    private Subtask subtask;
    private DateTimeFormatter format;
    private Epic epic1;

    @BeforeEach
    void createFirst() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager();
        format = DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm");
        task = new Task("Задача 1", 0, "Описание задачи 1",
                Status.NEW, TypeTask.TASK, 30, LocalDateTime.parse("25.05.22, 12:30", format));
        taskManager.createTask(task);
        epic = new Epic("Эпик 1", 0, "Описание эпика 1",
                Status.NO, TypeTask.EPIC, 180, LocalDateTime.parse("27.05.22, 17:00", format));
        taskManager.createEpic(epic);
        subtask = new Subtask("Подзадача 1", 0,
                "Описание подзадачи 1 эпика 1", Status.NEW, epic.getID(),
                TypeTask.SUBTASK, 70, LocalDateTime.parse("17.06.22, 13:40", format));
        taskManager.createSubtask(subtask);
        epic1 = new Epic("Эпик 1", 0, "Описание эпика 1",
                Status.NO, TypeTask.EPIC, 180, LocalDateTime.parse("27.05.22, 17:00", format));
        taskManager.createEpic(epic1);
    }

    @Test
    @DisplayName("Получить пустую историю")
    void getEmptyHistoryTest() {
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    @DisplayName("Получить историю без дубликатов")
    void getDuplicateHistoryTest() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
    }

    @Test
    @DisplayName("Удалить первую в истории")
    void removeFirstTest() {
        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(subtask);
        historyManager.remove(epic.getID());

        assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    @DisplayName("Удалить середину в истории")
    void removeMiddleTest() {
        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(subtask);
        historyManager.remove(task.getID());

        assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    @DisplayName("Удалить конец в истории")
    void removeEndTest() {
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.add(task);
        historyManager.remove(task.getID());

        assertEquals(2, historyManager.getHistory().size());
    }
}