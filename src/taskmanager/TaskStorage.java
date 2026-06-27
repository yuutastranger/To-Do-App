package taskmanager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


public class TaskStorage {

    private static final String SEP = "\t";
    private final Path file;

    public TaskStorage(String fileName) {
        this.file = Path.of(fileName);
    }

    public void save(List<Task> tasks) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(String.join(SEP,
                String.valueOf(task.getId()),
                String.valueOf(task.isDone()),
                task.getPriority().name(),
                task.getDeadline() == null ? "-" : task.getDeadline().toString(),
                task.getTitle(),
                (task.getDescription() == null || task.getDescription().isEmpty())
                        ? "-" : task.getDescription()
            ));
        }
        Files.write(file, lines);
    }

    public List<Task> load() throws IOException {
        List<Task> result = new ArrayList<>();
        if (!Files.exists(file)) {
            return result;
        }
        for (String line : Files.readAllLines(file)) {
            if (line.isBlank()) {
                continue;
            }
            Task task = parseLine(line);
            if (task != null) {
                result.add(task);
            }
        }
        return result;
    }


    private Task parseLine(String line) {
        String[] parts = line.split(SEP, -1); // -1 сохраняет пустые поля в конце
        if (parts.length < 6) {
            return null;
        }
        try {
            int id = Integer.parseInt(parts[0]);
            boolean done = Boolean.parseBoolean(parts[1]);
            Priority priority = Priority.valueOf(parts[2]);
            LocalDate deadline = parts[3].equals("-") ? null : LocalDate.parse(parts[3]);
            String title = parts[4];
            String description = parts[5].equals("-") ? "" : parts[5];

            Task task = new Task(id, title, description, priority, deadline);
            if (done) {
                task.markDone();
            }
            return task;
        } catch (IllegalArgumentException | DateTimeParseException e) {

            return null;
        }
    }
}
