package ru.yandex.manager.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.manager.ManagerTest;
import ru.yandex.manager.TaskManager;
import ru.yandex.model.Task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends ManagerTest<FileBackedTasksManager> {

    @BeforeEach
    @Override
    protected void initialData() {
        manager = new FileBackedTasksManager(Path.of("resources/task.csv"));
        super.initialData();
    }

    @Test
    @DisplayName("Пустой список задач")
    void emptyListTaskTest() {
        TaskManager managerEmpty = new FileBackedTasksManager(Path.of("resources/task.csv"));

        assertEquals(0, managerEmpty.getAllTasks().size());
        assertEquals(0, managerEmpty.getAllEpic().size());
        assertEquals(0, managerEmpty.getAllSubtasks().size());
    }

    @Test
    @DisplayName("Загразка из файла")
    void loadFromFileTest() {
        TaskManager manager2 = FileBackedTasksManager.loadFromFile("resources/task.csv");
        Task task1 = manager.getTaskByID(task.getID());
        Task task2 = manager2.getTaskByID(task.getID());
        assertEquals(task1, task2);
    }

    @Test
    @DisplayName("Преобразование из строки в задачу")
    void fromStringToTaskTest() {
        try (BufferedReader br = new BufferedReader(new FileReader("resources/task.csv"))) {
            br.readLine();
            Task taskFromFile = FileBackedTasksManager.fromStringToTask(br.readLine());
            assertEquals(manager.getTaskByID(task.getID()), taskFromFile);
        } catch (IOException exp) {
            throw new ManagerSaveException("Произошла ошибка");
        }
    }
}