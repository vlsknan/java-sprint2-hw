import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int id;

    HashMap<Integer, Task> tasksByID;
    HashMap<Integer, Epic> epicByID;
    HashMap<Integer, Subtask> subtasksByID;


    public Manager() {
        id = 1;
        this.tasksByID = new HashMap<>();
        this.subtasksByID = new HashMap<>();
        this.epicByID = new HashMap<>();
    }

    //получить все задачи
    ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasksByID.values());
    }

    ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicByID.values());
    }

    ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasksByID.values());
    }

    //удалить все задачи
    void deleteAllTasks() {
        tasksByID.clear();
    }

    void deleteAllEpic() {
            epicByID.clear();
    }

    void deleteAllSubtasks() {
        subtasksByID.clear();
    }

    //получить задачу по номеру
    Task getTaskByID(int id) {
        return tasksByID.get(id);
    }

    Epic getEpicByID(int id) {
        return epicByID.get(id);
    }

    Subtask getSubtaskByID(int id) {
        return subtasksByID.get(id);
    }

    //создание
    Task createTask (Task task) {
        task.setId(id++);
        tasksByID.put(task.getID(), task);
        return task;
    }

    Epic createEpic(Epic epic) {
        epic.setId(id++);
        epicByID.put(epic.getID(), epic);
        return epic;
    }

    Subtask createSubtask(Subtask subtask) {
        subtask.setId(id++);
        subtasksByID.put(subtask.getID(), subtask);
        return subtask;
    }

    //обновление
    void updateTask(Task task) {
        if (tasksByID.containsKey(task.getID())) {
            tasksByID.put(task.getID(), task);
        } else {
            return;
        }
    }

    void updateEpic(Epic epic) {
        if (epicByID.containsKey(epic.getID())) {
            int countNew = 0;
            int countProgress = 0;
            int countDone = 0;
            for (Subtask subtaskStatus : epic.getSubtasks()) {
                if (subtaskStatus == null || subtaskStatus.getStatus().equals("NEW")) {
                countNew++;
                } else if (subtaskStatus.getStatus().equals("IN_PROGRESS")) {
                countProgress++;
                } else {
                countDone++;
                }
            }
            if (countDone == epic.getSubtasks().size()) {
                epic.setStatus("DONE");
            } else if (countNew == epic.getSubtasks().size()) {
                epic.setStatus("NEW");
            } else {
                epic.setStatus("IN_PROGRESS");
            }
            epicByID.put(epic.getID(), epic);
        } else {
            return;
        }
    }

    void updateSubtask(Subtask subtask) {
        if (tasksByID.containsKey(subtask.getID())) {
            tasksByID.put(subtask.getID(), subtask);
        } else {
            return;
        }
    }

    //удалить по номеру
    void deleteTaskByID(int id) {
        tasksByID.remove(id);
    }

    void deleteEpicByID(String nameEpic) {
        epicByID.remove(nameEpic);
    }

    void deleteSubtaskByID(int id) {
        subtasksByID.remove(id);
    }

    ArrayList<Subtask> getListAllSubtaskEpic (Epic epic) {
        return new ArrayList<>(epic.getSubtasks());
    }
}
