import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int id;
    private String NEW = "NEW";
    private String IN_PROGRESS = "IN_PROGRESS";
    private String DONE = "DONE";

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

    Epic createEpic (Epic epic) {
        epic.setId(id++);
        tasksByID.put(epic.getID(), epic);
        return epic;
    }

    Subtask createSubtask (Subtask subtask) {
        subtask.setId(id++);
        subtasksByID.put(subtask.getID(), subtask);
        return subtask;
    }

    //обновление
    void update(Task task) {
        if (tasksByID.containsKey(task.getID())) {
            tasksByID.put(task.getID(), task);
        } else {
            return;
        }
    }

    /void update(Epic epic) {
        for (String subtaskStatus : epic.getStatus()) {
            if (subtaskStatus == null || subtaskStatus == NEW) {
                epic.setStatus("NEW");
            } else if (subtaskStatus == IN_PROGRESS) {
                epic.setStatus("IN_PROGRESS");
            } else {
                epic.setStatus("DONE");
            }
        }
        if (epicByID.containsKey(epic.getID())) {
            epicByID.put(epic.getID(), epic);
        } else {
            return;
        }
    }
*/
    void update(Subtask subtask) {

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
        ArrayList<Subtask> allSubtask = new ArrayList<>();
        allSubtask.add(subtasksByID.get(epic));
        return allSubtask;
    }
}
