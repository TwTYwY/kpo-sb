package hse.bank.visitors;

import hse.bank.domain.BankAccount;
import hse.bank.domain.Category;
import hse.bank.domain.Operation;

public class JsonExportVisitor implements DataExportVisitor {
    private final StringBuilder jsonBuilder = new StringBuilder();
    private boolean firstElement = true;

    public JsonExportVisitor() {
        jsonBuilder.append("{\n  \"data\": [\n");
    }

    @Override
    public void visit(BankAccount account) {
        if (!firstElement) {
            jsonBuilder.append(",\n");
        }
        jsonBuilder.append("    {\n")
                .append("      \"type\": \"BankAccount\",\n")
                .append("      \"id\": \"").append(account.getId()).append("\",\n")
                .append("      \"name\": \"").append(account.getName()).append("\",\n")
                .append("      \"balance\": ").append(account.getBalance()).append("\n")
                .append("    }");
        firstElement = false;
    }

    @Override
    public void visit(Category category) {
        if (!firstElement) {
            jsonBuilder.append(",\n");
        }
        jsonBuilder.append("    {\n")
                .append("      \"type\": \"Category\",\n")
                .append("      \"id\": \"").append(category.getId()).append("\",\n")
                .append("      \"type\": \"").append(category.getType()).append("\",\n")
                .append("      \"name\": \"").append(category.getName()).append("\"\n")
                .append("    }");
        firstElement = false;
    }

    @Override
    public void visit(Operation operation) {
        if (!firstElement) {
            jsonBuilder.append(",\n");
        }
        jsonBuilder.append("    {\n")
                .append("      \"type\": \"Operation\",\n")
                .append("      \"id\": \"").append(operation.getId()).append("\",\n")
                .append("      \"operationType\": \"").append(operation.getType()).append("\",\n")
                .append("      \"bankAccountId\": \"").append(operation.getBankAccountId()).append("\",\n")
                .append("      \"amount\": ").append(operation.getAmount()).append(",\n")
                .append("      \"date\": \"").append(operation.getDate()).append("\",\n")
                .append("      \"description\": \"").append(operation.getDescription()).append("\",\n")
                .append("      \"categoryId\": \"").append(operation.getCategoryId()).append("\"\n")
                .append("    }");
        firstElement = false;
    }

    public String getResult() {
        jsonBuilder.append("\n  ]\n}");
        return jsonBuilder.toString();
    }
}