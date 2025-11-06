package hse.bank.patterns.observer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BalanceNotifier {
    private final List<BalanceObserver> observers = new ArrayList<>();

    public void addObserver(BalanceObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BalanceObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String accountName, double oldBalance, double newBalance) {
        for (BalanceObserver observer : observers) {
            observer.update(accountName, oldBalance, newBalance);
        }
    }
}