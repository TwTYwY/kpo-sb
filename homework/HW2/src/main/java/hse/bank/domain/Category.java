package hse.bank.domain;

import java.util.UUID;

public class Category {
    public enum Type { INCOME, EXPENSE }

    private final UUID id;
    private Type type;
    private String name;

    public Category(UUID id, Type type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public UUID getId() { return id; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}