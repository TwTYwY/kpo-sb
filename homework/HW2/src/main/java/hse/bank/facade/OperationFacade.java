package hse.bank.facade;

import hse.bank.domain.BankAccount;
import hse.bank.domain.Operation;
import hse.bank.factories.DomainFactory;

import java.time.LocalDateTime;
import java.util.*;

public class OperationFacade {
    private final Map<UUID, Operation> operations = new HashMap<>();
    private final DomainFactory factory;
    private final BankAccountFacade accountFacade;

    public OperationFacade(DomainFactory factory, BankAccountFacade accountFacade) {
        this.factory = factory;
        this.accountFacade = accountFacade;
    }

    public Operation createOperation(Operation.Type type, UUID bankAccountId,
                                     double amount, String description, UUID categoryId) {
        Operation operation = factory.createOperation(type, bankAccountId, amount, description, categoryId);
        operations.put(operation.getId(), operation);

        updateAccountBalance(bankAccountId, type, amount);

        return operation;
    }

    private void updateAccountBalance(UUID accountId, Operation.Type type, double amount) {
        BankAccount account = accountFacade.getAccount(accountId);
        if (account != null) {
            double currentBalance = account.getBalance();
            if (type == Operation.Type.INCOME) {
                account.setBalance(currentBalance + amount);
            } else {
                account.setBalance(currentBalance - amount);
            }
        }
    }

    public void deleteOperation(UUID id) {
        Operation operation = operations.remove(id);
        if (operation != null) {
            BankAccount account = accountFacade.getAccount(operation.getBankAccountId());
            if (account != null) {
                double currentBalance = account.getBalance();
                if (operation.getType() == Operation.Type.INCOME) {
                    account.setBalance(currentBalance - operation.getAmount());
                } else {
                    account.setBalance(currentBalance + operation.getAmount());
                }
            }
        }
    }

    public Operation getOperation(UUID id) {
        return operations.get(id);
    }

    public List<Operation> getAllOperations() {
        return new ArrayList<>(operations.values());
    }

    public List<Operation> getOperationsByAccount(UUID accountId) {
        return operations.values().stream()
                .filter(op -> op.getBankAccountId().equals(accountId))
                .toList();
    }

    public List<Operation> getOperationsByCategory(UUID categoryId) {
        return operations.values().stream()
                .filter(op -> op.getCategoryId().equals(categoryId))
                .toList();
    }
}