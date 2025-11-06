package hse.bank;

import hse.bank.commands.Command;
import hse.bank.commands.CreateAccountCommand;
import hse.bank.commands.CreateOperationCommand;
import hse.bank.domain.BankAccount;
import hse.bank.domain.Category;
import hse.bank.domain.Operation;
import hse.bank.facade.*;
import hse.bank.factories.DomainFactory;
import hse.bank.importers.JsonDataImporter;
import hse.bank.patterns.TimingDecorator;
import hse.bank.patterns.proxy.BankAccountRepositoryProxy;
import hse.bank.visitors.JsonExportVisitor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("hse.bank.config");

        BankAccountFacade accountFacade = context.getBean(BankAccountFacade.class);
        CategoryFacade categoryFacade = context.getBean(CategoryFacade.class);
        OperationFacade operationFacade = context.getBean(OperationFacade.class);
        AnalyticsFacade analyticsFacade = context.getBean(AnalyticsFacade.class);
        BankAccountRepositoryProxy repositoryProxy = context.getBean(BankAccountRepositoryProxy.class);
        DomainFactory factory = context.getBean(DomainFactory.class);

        System.out.println("=== HSE Bank Financial Tracker ===\n");

        demonstrateAllPatterns(accountFacade, categoryFacade, operationFacade,
                analyticsFacade, repositoryProxy, factory);

        context.close();
    }

    private static void demonstrateAllPatterns(BankAccountFacade accountFacade,
                                               CategoryFacade categoryFacade,
                                               OperationFacade operationFacade,
                                               AnalyticsFacade analyticsFacade,
                                               BankAccountRepositoryProxy repositoryProxy,
                                               DomainFactory factory) {
        System.out.println("=== Демонстрация ПРОКСИ паттерна ===");

        BankAccount testAccount1 = accountFacade.createAccount("Proxy Test Account 1", 1000.0);
        BankAccount testAccount2 = accountFacade.createAccount("Proxy Test Account 2", 2000.0);

        repositoryProxy.save(testAccount1);
        repositoryProxy.save(testAccount2);

        System.out.println("\n--- Первый запрос (должен идти в БД) ---");
        BankAccount account1 = repositoryProxy.findById(testAccount1.getId());

        System.out.println("\n--- Второй запрос (должен быть из кэша) ---");
        BankAccount account1Cached = repositoryProxy.findById(testAccount1.getId());

        System.out.println("\n--- Запрос всех данных (из кэша) ---");
        repositoryProxy.findAll();

        repositoryProxy.printCacheStats();

        System.out.println("\n=== Демонстрация ШАБЛОННОГО МЕТОДА ===");
        JsonDataImporter jsonImporter = new JsonDataImporter();
        try {
            System.out.println("Импортер JSON готов к работе (файл не указан для демонстрации)");
        } catch (Exception e) {
            System.out.println("Ошибка импорта: " + e.getMessage());
        }

        System.out.println("\n=== Создание категорий ===");
        Category salaryCategory = categoryFacade.createCategory(Category.Type.INCOME, "Salary");
        Category foodCategory = categoryFacade.createCategory(Category.Type.EXPENSE, "Food");
        Category entertainmentCategory = categoryFacade.createCategory(Category.Type.EXPENSE, "Entertainment");

        System.out.println("\n=== Создание счетов (с измерением времени) ===");
        Command createAccount1 = new TimingDecorator(
                new CreateAccountCommand(accountFacade, "Main Account", 1000.0)
        );
        createAccount1.execute();

        Command createAccount2 = new TimingDecorator(
                new CreateAccountCommand(accountFacade, "Savings Account", 5000.0)
        );
        createAccount2.execute();

        UUID mainAccountId = accountFacade.getAllAccounts().get(0).getId();

        System.out.println("\n=== Создание операций (с измерением времени) ===");
        Command incomeOperation = new TimingDecorator(
                new CreateOperationCommand(operationFacade, Operation.Type.INCOME,
                        mainAccountId, 2000.0, "Monthly salary", salaryCategory.getId())
        );
        incomeOperation.execute();

        Command expenseOperation1 = new TimingDecorator(
                new CreateOperationCommand(operationFacade, Operation.Type.EXPENSE,
                        mainAccountId, 150.0, "Lunch at cafe", foodCategory.getId())
        );
        expenseOperation1.execute();

        Command expenseOperation2 = new TimingDecorator(
                new CreateOperationCommand(operationFacade, Operation.Type.EXPENSE,
                        mainAccountId, 50.0, "Cinema", entertainmentCategory.getId())
        );
        expenseOperation2.execute();

        System.out.println("\n=== Аналитика финансов ===");
        analyticsFacade.printFinancialSummary();

        System.out.println("\n=== Доходы по категориям ===");
        analyticsFacade.groupOperationsByCategory(Operation.Type.INCOME)
                .forEach((category, amount) ->
                        System.out.println(category + ": " + amount));

        System.out.println("\n=== Расходы по категориям ===");
        analyticsFacade.groupOperationsByCategory(Operation.Type.EXPENSE)
                .forEach((category, amount) ->
                        System.out.println(category + ": " + amount));

        System.out.println("\n=== Экспорт данных в JSON (ПОСЕТИТЕЛЯ) ===");
        JsonExportVisitor jsonExporter = new JsonExportVisitor();

        accountFacade.getAllAccounts().forEach(jsonExporter::visit);
        categoryFacade.getAllCategories().forEach(jsonExporter::visit);
        operationFacade.getAllOperations().forEach(jsonExporter::visit);

        System.out.println(jsonExporter.getResult());

        System.out.println("\n=== Демонстрация ФАБРИКИ с валидацией ===");
        try {
            factory.createOperation(Operation.Type.INCOME, mainAccountId, -100.0, "Invalid", salaryCategory.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Фабрика предотвратила создание невалидной операции: " + e.getMessage());
        }

        System.out.println("\n=== Финальное состояние счетов ===");
        accountFacade.getAllAccounts().forEach(account ->
                System.out.println(account.getName() + ": " + account.getBalance()));

        System.out.println("\n=== Все паттерны успешно продемонстрированы! ===");
        System.out.println("=== Spring DI контейнер активен! ===");
    }
}