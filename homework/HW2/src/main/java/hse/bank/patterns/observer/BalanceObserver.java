package hse.bank.patterns.observer;

public interface BalanceObserver {
    void update(String accountName, double oldBalance, double newBalance);
}