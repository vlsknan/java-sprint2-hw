import java.util.HashMap;

public class Manager {
    public int id;

    HashMap<Integer, Task> tasksByID;
    HashMap<Integer, Subtask> subtasksByID;
    HashMap<Integer, Epic> epicByID;

    public Manager() {
        id = 1;
        this.tasksByID = new HashMap<>();
        this.subtasksByID = new HashMap<>();
        this.epicByID = new HashMap<>();
    }

    void generateID(Task task) {
        task.setId(id++);
    }

    void getAllTasks() {
        for (Task tasks : tasksByID.values()) {
            System.out.println(tasks);
        }
    }

    void deleteAllTasks() {
        tasksByID.clear();
    }

    Task getTaskByID(int id) {
        for (Integer taskID : tasksByID.keySet()) {
            if (taskID == id) {
                System.out.println(tasksByID.get(taskID));
            } else {
                System.out.println("Такой задачи нет");
            }
        }
        return tasksByID.get(id);
    }

    void deleteByID(int id) {
        tasksByID.remove(id);
    }

    void updateStatus(Task task) {
        tasksByID.put(task.getID(), task);
    }
}
