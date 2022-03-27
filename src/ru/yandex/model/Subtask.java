import ru.yandex.model.Task;

public class Subtask extends Task {
    private int epicID;

    public Subtask(String taskName, int id, String descriptionTask, String status, int epicID) {
        super(taskName, id, descriptionTask, status);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }
}
