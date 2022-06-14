package ru.yandex.manager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

 public abstract class ManagerTest<T extends TaskManager> {
    protected T manager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    protected DateTimeFormatter format;

    protected void initialData() {
        format = DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm");
        task = new Task("Задача 1", 0, "Описание задачи 1",
                Status.NEW, TypeTask.TASK, 30, LocalDateTime.parse("25.05.22, 12:30", format));
        manager.createTask(task);
        epic = new Epic("Эпик 1", 0, "Описание эпика 1",
                Status.NO, TypeTask.EPIC, 180, LocalDateTime.parse("27.05.22, 17:00", format));
        manager.createEpic(epic);
        subtask = new Subtask("Подзадача 1", 0,
                "Описание подзадачи 1 эпика 1", Status.NEW, epic.getID(),
                TypeTask.SUBTASK, 70, LocalDateTime.parse("17.06.22, 13:40", format));
        manager.createSubtask(subtask);
    }

    @Test
    @DisplayName("Получить все таски")
    void getAllTasksTest() {
        List<Task> tasks = manager.getAllTasks();
        assertNotNull(tasks);
        assertEquals(task, tasks.get(0));
    }

    @Test
    @DisplayName("Получить все эпики")
    void getAllEpicTest() {
        List<Epic> epics = manager.getAllEpic();
        assertNotNull(epics);
        assertEquals(epic, epics.get(0));
    }

    @Test
    @DisplayName("Получить все сабтаски")
    void getAllSubtasksTest() {
        List<Subtask> subtasks = manager.getAllSubtasks();
        assertNotNull(subtasks);
        assertEquals(subtask, subtasks.get(0));
    }

    @Test
    @DisplayName("Удалить все таски")
    void deleteAllTasksTest() {
        manager.deleteAllTasks();
        List<Task> tasks = manager.getAllTasks();
        assertEquals(0, tasks.size());
    }

    @Test
    @DisplayName("Удалить все эпики")
    void deleteAllEpicTest() {
        manager.deleteAllEpic();
        List<Epic> epics = manager.getAllEpic();
        assertEquals(0, epics.size());
    }

    @Test
    @DisplayName("Удалить все сабтаски")
    void deleteAllSubtasksTest() {
        manager.deleteAllSubtasks();
        List<Subtask> subtasks = manager.getAllSubtasks();
        assertEquals(0, subtasks.size());
    }

    @Test
    @DisplayName("Получить задачу по id")
    void getTaskByIDTest() {
        Task tasks = manager.getTaskByID(task.getID());
        assertNotNull(tasks);
        assertEquals(task, tasks);
    }

     @Test
     @DisplayName("id не существует")
     void getTaskByIDNullTest() {
         Task tasks = manager.getTaskByID(5);
         assertNull(tasks);
     }

    @Test
    @DisplayName("Получить эпик по id")
    void getEpicByIDTest() {
        Epic epics = manager.getEpicByID(epic.getID());
        assertNotNull(epics);
        assertEquals(epic, epics);
    }

     @Test
     @DisplayName("id не существует")
     void getEpicByIDNullTest() {
         Epic epics = manager.getEpicByID(5);
         assertNull(epics);
     }

    @Test
    @DisplayName("Полуить сабтаск по id")
    void getSubtaskByIDTest() {
        Subtask subtasks = manager.getSubtaskByID(subtask.getID());
        assertNotNull(subtasks);
        assertEquals(subtask, subtasks);
    }

     @Test
     @DisplayName("id не существует")
     void getSubtaskByIDNullTest() {
         Subtask subtasks = manager.getSubtaskByID(5);
         assertNull(subtasks);
     }

    @Test
    @DisplayName("Обновить задачи")
    void updateTaskTest() {
        Task newTask = new Task("Задача 2", task.getID(), "Описание задачи 2",
                Status.IN_PROGRESS, TypeTask.TASK, 20, LocalDateTime.parse("01.06.22, 21:20", format));
                manager.updateTask(newTask);
        assertEquals(newTask, manager.getTaskByID(task.getID()));
    }

    @Test
    @DisplayName("Обновить сабтаск")
    void updateSubtaskTest() {
        Subtask newSubtask = new Subtask("Подзадача 2", subtask.getID(),
                "Описание подзадачи 1 эпика 1", Status.IN_PROGRESS, epic.getID(),
                TypeTask.SUBTASK, 30, LocalDateTime.parse("30.05.22, 10:45", format));
        manager.updateSubtask(newSubtask);
        assertEquals(newSubtask, manager.getSubtaskByID(subtask.getID()));
    }

    @Test
    @DisplayName("Удалить задачу по id")
    void deleteTaskByIDTest() {
        manager.deleteTaskByID(task.getID());
       List<Task> tasks = manager.getAllTasks();
       assertEquals(0, tasks.size());
    }

    @Test
    @DisplayName("Удалить эпик п id")
    void deleteEpicByIDTest() {
        manager.deleteEpicByID(epic.getID());
        List<Epic> epics = manager.getAllEpic();
        assertEquals(0, epics.size());
    }

    @Test
    @DisplayName("Удалить сабтаск по id")
    void deleteSubtaskByIDTest() {
        manager.deleteSubtaskByID(subtask.getID());
        List<Subtask> subtasks = manager.getAllSubtasks();
        assertEquals(0, subtasks.size());
    }

    @Test
    @DisplayName("Получить историю")
    void getHistoryTest() {
        manager.getTaskByID(task.getID());
        manager.getEpicByID(epic.getID());
        List<Task> history = manager.getHistory();
        assertEquals(2, history.size());
    }

     @Test
     @DisplayName("Эпик без статуса (подзадач нет)")
     void epicWithEmptySubtaskTest() {
        assertEquals(Status.NO, manager.getEpicByID(epic.getID()).getStatus());
     }

     @Test
     @DisplayName("Эпик должен быть New (подзадача New)")
     void epicWithNewSubtaskTest() {
         epic.putSubtask(subtask);
         manager.updateEpic(epic);

         assertEquals(Status.NEW, epic.getStatus());
     }

     @Test
     @DisplayName("Эпик должен быть Done (подзадача Done)")
     void epicWithDoneSubtaskTest() {
         Subtask subtask1 = new Subtask("Подзадача 1", 0,
                 "Описание подзадачи 1 эпика 1", Status.DONE, epic.getID(),
                 TypeTask.SUBTASK, 70, LocalDateTime.parse("17.06.22, 13:40", format));
         manager.createSubtask(subtask1);

         epic.putSubtask(subtask1);
         manager.updateEpic(epic);

         assertEquals(Status.DONE, epic.getStatus());
     }

     @Test
     @DisplayName("Эпик должен быть In progress (подзадачи New и Done)")
     void epicWithNewAndDoneSubtaskTest() {
         Subtask subtask1 = new Subtask("Подзадача 1", 0,
                 "Описание подзадачи 1 эпика 1", Status.NEW, epic.getID(),
                 TypeTask.SUBTASK, 70, LocalDateTime.parse("17.06.22, 13:40", format));
         manager.createSubtask(subtask1);
         Subtask subtask2 = new Subtask("Подзадача 2", 0,
                 "Описание подзадачи 2 эпика 1", Status.DONE, epic.getID(),
                 TypeTask.SUBTASK, 70, LocalDateTime.parse("17.06.22, 13:40", format));
         manager.createSubtask(subtask2);

         epic.putSubtask(subtask1);
         epic.putSubtask(subtask2);

         manager.updateEpic(epic);

         assertEquals(Status.IN_PROGRESS, epic.getStatus());
     }

     @Test
     @DisplayName("Эпик должен быть In progress (подзадача In progress)")
     void epicWithInProgressSubtaskTest() {
         Subtask subtask1 = new Subtask("Подзадача 1", 0,
                 "Описание подзадачи 1 эпика 1", Status.IN_PROGRESS, epic.getID(),
                 TypeTask.SUBTASK, 70, LocalDateTime.parse("17.06.22, 13:40", format));
         manager.createSubtask(subtask1);

         epic.putSubtask(subtask1);
         manager.updateEpic(epic);

         assertEquals(Status.IN_PROGRESS, epic.getStatus());
     }

     @Test
     @DisplayName("Наличие у эпика сабтаска")
     void availabilityEpicForSubtask() {
        epic.putSubtask(subtask);

        assertEquals(subtask.getEpicID(), epic.getID());
     }

     @Test
     @DisplayName("Получить приоритетный список задач")
     void getPrioritizedTasksTest() {
         Map<LocalDateTime, Task> list = manager.getPrioritizedTasks();
         Map<LocalDateTime, Task> list2 = new TreeMap<>();
         list2.put(task.getStartTime(), task);
         list2.put(subtask.getStartTime(), subtask);

         assertEquals(list2.keySet(), list.keySet());
     }
}