package hse.bank.commands;

import hse.bank.domain.Operation;
import hse.bank.facade.OperationFacade;

import java.util.UUID;

public class CreateOperationCommand implements Command {
    private final OperationFacade operationFacade;
    private final Operation.Type type;
    private final UUID accountId;
    private final double amount;
    private final String description;
    private final UUID categoryId;
    private Operation createdOperation;

    public CreateOperationCommand(OperationFacade operationFacade, Operation.Type type,
                                  UUID accountId, double amount, String description, UUID categoryId) {
        this.operationFacade = operationFacade;
        this.type = type;
        this.accountId = accountId;
        this.amount = amount;
        this.description = description;
        this.categoryId = categoryId;
    }

    @Override
    public void execute() {
        createdOperation = operationFacade.createOperation(type, accountId, amount, description, categoryId);
        System.out.println("Created " + type + " operation: " + description + " amount: " + amount);
    }

    @Override
    public String getName() {
        return "Create " + type + " Operation";
    }

    public Operation getCreatedOperation() {
        return createdOperation;
    }
}