package hse.bank.patterns.proxy;

import hse.bank.domain.BankAccount;

import java.util.*;

public class BankAccountRepositoryProxy implements BankAccountRepository {
    private final BankAccountRepository realRepository;
    private final Map<UUID, BankAccount> cache = new HashMap<>();
    private boolean cacheLoaded = false;

    public BankAccountRepositoryProxy(BankAccountRepository realRepository) {
        this.realRepository = realRepository;
    }

    private void ensureCacheLoaded() {
        if (!cacheLoaded) {
            loadAllToCache();
            cacheLoaded = true;
        }
    }

    private void loadAllToCache() {
        System.out.println("Loading all accounts to CACHE...");
        List<BankAccount> allAccounts = realRepository.findAll();
        allAccounts.forEach(account -> cache.put(account.getId(), account));
        System.out.println("Cache loaded with " + cache.size() + " accounts");
    }

    @Override
    public BankAccount findById(UUID id) {
        ensureCacheLoaded();

        if (cache.containsKey(id)) {
            System.out.println("Reading from CACHE: account " + id);
            return cache.get(id);
        }

        System.out.println("Account not in cache, reading from DATABASE: " + id);
        BankAccount account = realRepository.findById(id);
        if (account != null) {
            cache.put(id, account);
            System.out.println("Added account to CACHE: " + id);
        }
        return account;
    }

    @Override
    public List<BankAccount> findAll() {
        ensureCacheLoaded();
        System.out.println("Reading ALL accounts from CACHE");
        return new ArrayList<>(cache.values());
    }

    @Override
    public void save(BankAccount account) {
        realRepository.save(account);
        cache.put(account.getId(), account);
        System.out.println("Saved to both DATABASE and CACHE: account " + account.getId());
    }

    @Override
    public void delete(UUID id) {
        realRepository.delete(id);
        cache.remove(id);
        System.out.println("Deleted from both DATABASE and CACHE: account " + id);
    }

    public void clearCache() {
        cache.clear();
        cacheLoaded = false;
        System.out.println("Cache cleared");
    }

    public void printCacheStats() {
        System.out.println("Cache statistics: " + cache.size() + " accounts in cache");
    }
}