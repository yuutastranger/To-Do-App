package taskmanager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class TaskManager {

    private final List<Task> tasks = new ArrayList<>();
    private int nextId = 1; // счётчик для генерации уникальных id

    public Task addTask(String title, String description, Priority priority, LocalDate deadline) {
        Task task = new Task(nextId++, title, description, priority, deadline);
        tasks.add(task);
        return task;
    }

    public List<Task> getSortedTasks() {
        List<Task> copy = new ArrayList<>(tasks);
        copy.sort(
            Comparator.comparing(Task::isDone)
                .thenComparing(task -> task.getPriority().ordinal())
                .thenComparing(Task::getDeadline,
                        Comparator.nullsLast(Comparator.naturalOrder()))
        );
        return copy;
    }

    public Task findById(int id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public void markDone(int id) {
        findById(id).markDone();
    }

    public void deleteTask(int id) {
        Task task = findById(id);
        tasks.remove(task);
    }

    public List<Task> getAll() {
        return tasks;
    }


    public void loadFrom(List<Task> loaded) {
        tasks.clear();
        tasks.addAll(loaded);
        nextId = loaded.stream().mapToInt(Task::getId).max().orElse(0) + 1;
    }
}
