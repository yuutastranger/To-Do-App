package taskmanager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;


public class Main{

    private static final String DATA_FILE = "tasks.txt";

    private final TaskManager manager = new TaskManager();
    private final TaskStorage storage = new TaskStorage(DATA_FILE);
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args){
        new Main().run();
    }

    private void run(){
        try{
            manager.loadFrom(storage.load());
        }
        catch (Exception e){
            System.out.println("Не удалось загрузить сохранённые задачи: " + e.getMessage());
        }

        System.out.println(" Менеджер задач ");
        boolean running = true;
        while (running){
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice){
                case "1" -> listTasks();
                case "2" -> addTask();
                case "3" -> markDone();
                case "4" -> deleteTask();
                case "5" -> running = false;
                default -> System.out.println("Неизвестная команда. Введите число от 1 до 5.");
            }
        }

        try{
            storage.save(manager.getAll());
            System.out.println("Задачи сохранены. До встречи!");
        }
        catch (Exception e){
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    private void printMenu(){
        System.out.println();
        System.out.println("1 — показать задачи");
        System.out.println("2 — добавить задачу");
        System.out.println("3 — отметить выполненной");
        System.out.println("4 — удалить задачу");
        System.out.println("5 — выход");
        System.out.print("> ");
    }

    private void listTasks(){
        List<Task> tasks = manager.getSortedTasks();
        if (tasks.isEmpty()){
            System.out.println("Список задач пуст.");
            return;
        }
        System.out.println();
        System.out.println("Ваши задачи:");
        for (Task task : tasks){
            System.out.println("  " + task);
        }
    }

    private void addTask(){
        System.out.print("Название: ");
        String title = readSanitizedLine();
        if (title.isEmpty()){
            System.out.println("Название не может быть пустым, задача не создана.");
            return;
        }
        System.out.print("Описание (Enter — пропустить): ");
        String description = readSanitizedLine();

        Priority priority = readPriority();
        LocalDate deadline = readDeadline();

        Task created = manager.addTask(title, description, priority, deadline);
        System.out.println("Добавлена задача #" + created.getId());
    }

    private void markDone(){
        Integer id = readId("Введите id задачи для отметки: ");
        if (id == null){
            return;
        }
        try{
            manager.markDone(id);
            System.out.println("Задача #" + id + " отмечена выполненной.");
        }
        catch (TaskNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    private void deleteTask(){
        Integer id = readId("Введите id задачи для удаления: ");
        if (id == null){
            return;
        }
        try{
            manager.deleteTask(id);
            System.out.println("Задача #" + id + " удалена.");
        }
        catch (TaskNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    private String readSanitizedLine(){
        return scanner.nextLine().replace("\t", " ").trim();
    }

    private Priority readPriority(){
        System.out.print("Приоритет (1 — высокий, 2 — средний, 3 — низкий) [по умолчанию 2]: ");
        String input = scanner.nextLine().trim();
        return switch (input){
            case "1" -> Priority.HIGH;
            case "3" -> Priority.LOW;
            default -> Priority.MEDIUM;
        };
    }

    private LocalDate readDeadline(){
        System.out.print("Дедлайн в формате ГГГГ-ММ-ДД (Enter — без срока): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()){
            return null;
        }
        try{
            return LocalDate.parse(input);
        }
        catch (DateTimeParseException e){
            System.out.println("Дата введена неверно, задача будет без срока.");
            return null;
        }
    }

    private Integer readId(String prompt){
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        try{
            return Integer.parseInt(input);
        }
        catch (NumberFormatException e){
            System.out.println("Нужно ввести число.");
            return null;
        }
    }
}
