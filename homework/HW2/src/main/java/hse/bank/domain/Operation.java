package hse.bank.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Operation {
    public enum Type { INCOME, EXPENSE }

    private final UUID id;
    private Type type;
    private UUID bankAccountId;
    private double amount;
    private LocalDateTime date;
    private String description;
    private UUID categoryId;

    public Operation(UUID id, Type type, UUID bankAccountId, double amount,
                     LocalDateTime date, String description, UUID categoryId) {
        this.id = id;
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.categoryId = categoryId;
    }

    public UUID getId() { return id; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public UUID getBankAccountId() { return bankAccountId; }
    public void setBankAccountId(UUID bankAccountId) { this.bankAccountId = bankAccountId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public UUID getCategoryId() { return categoryId; }
    public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }
}