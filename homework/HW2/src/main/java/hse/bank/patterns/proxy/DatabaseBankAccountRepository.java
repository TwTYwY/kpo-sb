package hse.bank.patterns.proxy;

import hse.bank.domain.BankAccount;

import java.util.*;

public class DatabaseBankAccountRepository implements BankAccountRepository {
    private final Map<UUID, BankAccount> database = new HashMap<>();

    @Override
    public BankAccount findById(UUID id) {
        System.out.println("Reading from DATABASE: account " + id);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return database.get(id);
    }

    @Override
    public List<BankAccount> findAll() {
        System.out.println("Reading ALL accounts from DATABASE");
        return new ArrayList<>(database.values());
    }

    @Override
    public void save(BankAccount account) {
        System.out.println("Saving to DATABASE: account " + account.getId() + " - " + account.getName());
        database.put(account.getId(), account);
    }

    @Override
    public void delete(UUID id) {
        System.out.println("Deleting from DATABASE: account " + id);
        database.remove(id);
    }
}