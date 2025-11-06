package hse.bank.patterns.observer;

import org.springframework.stereotype.Component;

@Component
public class BalanceLogger implements BalanceObserver {
    @Override
    public void update(String accountName, double oldBalance, double newBalance) {
        System.out.println("BALANCE CHANGE: " + accountName +
                " | Old: " + oldBalance + " → New: " + newBalance +
                " | Diff: " + (newBalance - oldBalance));
    }
}