public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();
        Task task = new Task("Переезд", 0, "Описание", "NEW");
        manager.createTask(task);
        manager.getTaskByID(task.getID());
        System.out.println(manager.getAllTasks());
        manager.deleteTaskByID(task.getID());


    }
}
