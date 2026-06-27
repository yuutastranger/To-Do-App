package taskmanager;

import java.time.LocalDate;

public class Task {

    private final int id;
    private String title;
    private String description;
    private Priority priority;
    private LocalDate deadline; // может быть null, если срок не задан
    private boolean done;

    public Task(int id, String title, String description, Priority priority, LocalDate deadline) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.done = false;
    }

    // --- геттеры ---
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public boolean isDone() {
        return done;
    }

    // --- изменение состояния ---
    public void markDone() {
        this.done = true;
    }

    @Override
    public String toString() {
        String status = done ? "[x]" : "[ ]";
        String deadlineText = (deadline == null) ? "без срока" : "до " + deadline;
        StringBuilder sb = new StringBuilder();
        sb.append(status)
          .append(" #").append(id)
          .append(" [").append(priority.label()).append("] ")
          .append(title)
          .append(" (").append(deadlineText).append(")");
        if (description != null && !description.isEmpty()) {
            sb.append(" — ").append(description);
        }
        return sb.toString();
    }
}
