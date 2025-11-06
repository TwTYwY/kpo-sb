package hse.bank.factories;

import hse.bank.domain.BankAccount;
import hse.bank.domain.Category;
import hse.bank.domain.Operation;

import java.time.LocalDateTime;
import java.util.UUID;

public class DomainFactory {

    public BankAccount createBankAccount(String name, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        return new BankAccount(UUID.randomUUID(), name, initialBalance);
    }

    public Category createCategory(Category.Type type, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        return new Category(UUID.randomUUID(), type, name);
    }

    public Operation createOperation(Operation.Type type, UUID bankAccountId,
                                     double amount, String description, UUID categoryId) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Operation amount must be positive");
        }
        if (bankAccountId == null || categoryId == null) {
            throw new IllegalArgumentException("Bank account and category must be specified");
        }

        return new Operation(UUID.randomUUID(), type, bankAccountId, amount,
                LocalDateTime.now(), description, categoryId);
    }
}