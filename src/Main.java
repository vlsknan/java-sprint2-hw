import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();
        Task task1 = new Task("Переезд", 0, "Собрать вещи для переезда", "NEW");
        manager.createTask(task1);

        Task task2 = new Task("Задача 2", 0, "-", "IN_PROGRESS");
        manager.createTask(task2);

        Subtask subtaskForEpic1 = new Subtask("Мат анализ", 0, "-", "IN_PROGRESS", 0);

        Epic epic1 = new Epic("Сдать сессию", 0, "Выучить все экзамены", "0", subtaskForEpic1);
    }
}
