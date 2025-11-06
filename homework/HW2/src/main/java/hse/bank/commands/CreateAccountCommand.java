package hse.bank.commands;

import hse.bank.domain.BankAccount;
import hse.bank.facade.BankAccountFacade;

public class CreateAccountCommand implements Command {
    private final BankAccountFacade accountFacade;
    private final String name;
    private final double initialBalance;
    private BankAccount createdAccount;

    public CreateAccountCommand(BankAccountFacade accountFacade, String name, double initialBalance) {
        this.accountFacade = accountFacade;
        this.name = name;
        this.initialBalance = initialBalance;
    }

    @Override
    public void execute() {
        createdAccount = accountFacade.createAccount(name, initialBalance);
        System.out.println("Created account: " + createdAccount.getName() + " with balance: " + createdAccount.getBalance());
    }

    @Override
    public String getName() {
        return "Create Account: " + name;
    }

    public BankAccount getCreatedAccount() {
        return createdAccount;
    }
}