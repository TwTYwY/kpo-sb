package hse.bank.facade;

import hse.bank.domain.Operation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class AnalyticsFacade {
    private final OperationFacade operationFacade;
    private final CategoryFacade categoryFacade;

    public AnalyticsFacade(OperationFacade operationFacade, CategoryFacade categoryFacade) {
        this.operationFacade = operationFacade;
        this.categoryFacade = categoryFacade;
    }

    public double calculateBalanceDifference(LocalDateTime start, LocalDateTime end) {
        List<Operation> operationsInPeriod = operationFacade.getAllOperations().stream()
                .filter(op -> !op.getDate().isBefore(start) && !op.getDate().isAfter(end))
                .toList();

        double totalIncome = operationsInPeriod.stream()
                .filter(op -> op.getType() == Operation.Type.INCOME)
                .mapToDouble(Operation::getAmount)
                .sum();

        double totalExpense = operationsInPeriod.stream()
                .filter(op -> op.getType() == Operation.Type.EXPENSE)
                .mapToDouble(Operation::getAmount)
                .sum();

        return totalIncome - totalExpense;
    }

    public Map<String, Double> groupOperationsByCategory(Operation.Type type) {
        return operationFacade.getAllOperations().stream()
                .filter(op -> op.getType() == type)
                .collect(Collectors.groupingBy(
                        op -> {
                            var category = categoryFacade.getCategory(op.getCategoryId());
                            return category != null ? category.getName() : "Unknown";
                        },
                        Collectors.summingDouble(Operation::getAmount)
                ));
    }

    public void printFinancialSummary() {
        double totalIncome = operationFacade.getAllOperations().stream()
                .filter(op -> op.getType() == Operation.Type.INCOME)
                .mapToDouble(Operation::getAmount)
                .sum();

        double totalExpense = operationFacade.getAllOperations().stream()
                .filter(op -> op.getType() == Operation.Type.EXPENSE)
                .mapToDouble(Operation::getAmount)
                .sum();

        System.out.println("=== Financial Summary ===");
        System.out.println("Total Income: " + totalIncome);
        System.out.println("Total Expense: " + totalExpense);
        System.out.println("Net Balance: " + (totalIncome - totalExpense));
    }
}