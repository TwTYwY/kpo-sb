package hse.bank.facade;

import hse.bank.domain.BankAccount;
import hse.bank.factories.DomainFactory;
import hse.bank.patterns.observer.BalanceNotifier;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BankAccountFacade {
    private final Map<UUID, BankAccount> accounts = new HashMap<>();
    private final DomainFactory factory;
    private final BalanceNotifier balanceNotifier;

    public BankAccountFacade(DomainFactory factory, BalanceNotifier balanceNotifier) {
        this.factory = factory;
        this.balanceNotifier = balanceNotifier;
    }

    public BankAccount createAccount(String name, double initialBalance) {
        BankAccount account = factory.createBankAccount(name, initialBalance);
        accounts.put(account.getId(), account);
        return account;
    }

    public void updateAccount(UUID id, String name) {
        BankAccount account = accounts.get(id);
        if (account != null) {
            account.setName(name);
        }
    }

    public void deleteAccount(UUID id) {
        accounts.remove(id);
    }

    public BankAccount getAccount(UUID id) {
        return accounts.get(id);
    }

    public List<BankAccount> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public void updateBalance(UUID accountId, double newBalance) {
        BankAccount account = accounts.get(accountId);
        if (account != null) {
            double oldBalance = account.getBalance();
            account.setBalance(newBalance);
            balanceNotifier.notifyObservers(account.getName(), oldBalance, newBalance);
        }
    }
}