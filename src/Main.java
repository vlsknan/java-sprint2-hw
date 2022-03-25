public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

        Task task1 = new Task("Переезд", 0, "Собрать вещи для переезда", "NEW");
        manager.createTask(task1);
        Task task2 = new Task("Задача 2", 0, "-", "IN_PROGRESS");
        manager.createTask(task2);
        manager.getTaskByID(task1.getID());
        manager.getTaskByID(task2.getID());

        Epic epic1 = new Epic("Сдать сессию", 0, "Выучить все экзамены", "-");
        manager.createEpic(epic1);
        manager.getEpicByID(epic1.getID());

        Subtask subtaskForEpic1_1 = new Subtask("Мат анализ", 0, "-", "NEW", epic1.getID());
        Subtask subtaskForEpic1_2 = new Subtask("Философия", 0, "Осталось выучить 3 билета",
                "IN_PROGRESS", epic1.getID());
        manager.createSubtask(subtaskForEpic1_1);
        manager.createSubtask(subtaskForEpic1_2);
        manager.getEpicByID(epic1.getID()).putSubtask(subtaskForEpic1_1);
        manager.getEpicByID(epic1.getID()).putSubtask(subtaskForEpic1_2);


        Epic epic2 = new Epic("Убраться дома", 0, "-", "-");
        manager.createEpic(epic2);
        manager.getEpicByID(epic2.getID());

        Subtask subtaskForEpic2_1 = new Subtask("Помыть полы", 0, "пусть будет чисто", "DONE", epic2.getID());
        manager.createSubtask(subtaskForEpic2_1);
        manager.getEpicByID(epic2.getID()).putSubtask(subtaskForEpic2_1);
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubtasks() + "\n");

        manager.updateTask(task1);
        manager.updateTask(task2);
        manager.updateSubtask(subtaskForEpic1_1);
        manager.updateSubtask(subtaskForEpic1_2);
        manager.updateSubtask(subtaskForEpic2_1);
        manager.updateEpic(epic1);
        manager.updateEpic(epic2);
        System.out.println(manager.getEpicByID(epic1.getID()));
        System.out.println(manager.getEpicByID(epic2.getID()) + "\n");

        manager.deleteTaskByID(task2.getID());
        manager.deleteEpicByID(epic1.getTaskName());
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpic());
    }
}
