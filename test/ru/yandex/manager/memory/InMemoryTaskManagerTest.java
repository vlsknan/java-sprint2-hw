package ru.yandex.manager.memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.manager.ManagerTest;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends ManagerTest<InMemoryTaskManager> {

    @BeforeEach
    @Override
   protected void initialData() {
        manager = new InMemoryTaskManager();
        super.initialData();
    }

    @Test
    @DisplayName("Менеджер должен быть пустым")
    void shouldBeEmpty() {
        manager = new InMemoryTaskManager();
        assertEquals(0, manager.getAllTasks().size());
        assertEquals(0, manager.getAllEpic().size());
        assertEquals(0, manager.getAllSubtasks().size());
        assertEquals(0, manager.getHistory().size());
    }
}