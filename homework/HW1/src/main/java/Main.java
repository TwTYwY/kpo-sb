import entities.*;
import services.ZooService;
import config.AppConfig;
import java.util.*;
import java.util.Scanner;
import interfaces.IInventory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        ZooService zooService = context.getBean(ZooService.class);
        Scanner scanner = new Scanner(System.in);

        initializeTestData(zooService);

        while (true) {
            System.out.println("\n=== Московский зоопарк ===");
            System.out.println("1. Добавить животное");
            System.out.println("2. Показать всех животных");
            System.out.println("3. Общее количество еды");
            System.out.println("4. Животные для контактного зоопарка");
            System.out.println("5. Весь инвентарь");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        addAnimal(scanner, zooService);
                        break;
                    case 2:
                        showAnimals(zooService);
                        break;
                    case 3:
                        showTotalFood(zooService);
                        break;
                    case 4:
                        showContactZooAnimals(zooService);
                        break;
                    case 5:
                        showAllInventory(zooService);
                        break;
                    case 0:
                        System.out.println("До свидания!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Неверный выбор!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите число!");
                scanner.nextLine();
            } catch (NoSuchElementException e) {
                System.out.println("Ошибка ввода. Завершение программы.");
                break;
            }
        }
        scanner.close();
        context.close();
    }

    private static void addAnimal(Scanner scanner, ZooService zooService) {
        System.out.println("\n--- Добавление животного ---");
        System.out.println("Выберите категорию:");
        System.out.println("1. Хищники");
        System.out.println("2. Травоядные");
        System.out.print("Ваш выбор: ");

        int category = scanner.nextInt();
        scanner.nextLine();

        Animal animal = null;

        switch (category) {
            case 1:
                animal = addPredator(scanner);
                break;
            case 2:
                animal = addHerbo(scanner);
                break;
            default:
                System.out.println("Неверная категория!");
                return;
        }

        if (animal != null) {
            boolean added = zooService.addAnimal(animal);
            if (added) {
                System.out.println("Животное добавлено!");
            } else {
                System.out.println("Животное не добавлено!");
                System.out.println("Причина: животное должно быть здорово для добавления в зоопарк.");
            }
        }
    }

    private static Animal addPredator(Scanner scanner) {
        System.out.println("\n--- Выберите хищника ---");
        System.out.println("1. Тигр");
        System.out.println("2. Волк");
        System.out.print("Ваш выбор: ");

        int predatorType = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Инвентарный номер: ");
        int number = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Имя: ");
        String name = scanner.nextLine();

        System.out.print("Еда (кг/день): ");
        int food = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Здоров (true/false): ");
        boolean isHealthy = scanner.nextBoolean();
        scanner.nextLine();

        switch (predatorType) {
            case 1:
                System.out.print("Количество полос: ");
                int stripeCount = scanner.nextInt();
                scanner.nextLine();
                return new Tiger(number, name, food, isHealthy, stripeCount);
            case 2:
                System.out.print("Название стаи: ");
                String packName = scanner.nextLine();
                return new Wolf(number, name, food, isHealthy, packName);
            default:
                System.out.println("Неверный тип хищника!");
                return null;
        }
    }

    private static Animal addHerbo(Scanner scanner) {
        System.out.println("\n--- Выберите травоядное ---");
        System.out.println("1. Кролик");
        System.out.println("2. Обезьяна");
        System.out.print("Ваш выбор: ");

        int herboType = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Инвентарный номер: ");
        int number = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Имя: ");
        String name = scanner.nextLine();

        System.out.print("Еда (кг/день): ");
        int food = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Здоров (true/false): ");
        boolean isHealthy = scanner.nextBoolean();
        scanner.nextLine();

        System.out.print("Уровень доброты (1-10): ");
        int kindness = scanner.nextInt();
        scanner.nextLine();

        switch (herboType) {
            case 1:
                System.out.print("Цвет меха: ");
                String furColor = scanner.nextLine();
                return new Rabbit(number, name, food, isHealthy, kindness, furColor);
            case 2:
                System.out.print("Уровень интеллекта (1-10): ");
                int intelligence = scanner.nextInt();
                scanner.nextLine();
                return new Monkey(number, name, food, isHealthy, kindness, intelligence);
            default:
                System.out.println("Неверный тип травоядного!");
                return null;
        }
    }

    private static void showAnimals(ZooService zooService) {
        System.out.println("\n--- Все животные ---");
        for (Animal animal : zooService.getAnimals()) {
            System.out.printf("№%d: %s, Еда: %d кг, Здоров: %s, Контактный: %s%n",
                    animal.getInventoryNumber(),
                    animal.getName(),
                    animal.getFoodAmount(),
                    animal.isHealthy() ? "Да" : "Нет",
                    animal.canBeInContactZoo() ? "Да" : "Нет");
        }
    }

    private static void showTotalFood(ZooService zooService) {
        System.out.printf("\nОбщая потребность в еде: %d кг/день%n",
                zooService.getTotalFoodAmount());
    }

    private static void showContactZooAnimals(ZooService zooService) {
        System.out.println("\n--- Животные для контактного зоопарка ---");
        for (Animal animal : zooService.getAnimalsForContactZoo()) {
            System.out.printf("№%d: %s%n", animal.getInventoryNumber(), animal.getName());
        }
    }

    private static void showAllInventory(ZooService zooService) {
        System.out.println("\n--- Весь инвентарь ---");
        for (IInventory item : zooService.getAllInventory()) {
            System.out.printf("№%d: %s (%s)%n",
                    item.getInventoryNumber(),
                    item.getName(),
                    item.getClass().getSimpleName());
        }
    }

    private static void initializeTestData(ZooService zooService) {
        zooService.addAnimal(new Rabbit(1, "Крош", 2, true, 8, "Синий"));
        zooService.addAnimal(new Tiger(2, "Тигра", 10, true, 10));
        zooService.addAnimal(new Wolf(3, "Джейкоб", 10, true, "Блэк"));
        zooService.addAnimal(new Monkey(4, "Чичичи", 1, true, 10, 0));
    }
}