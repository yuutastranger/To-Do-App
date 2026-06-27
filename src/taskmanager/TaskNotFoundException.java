package taskmanager;


public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(int id) {
        super("Задача с id=" + id + " не найдена");
    }
}
