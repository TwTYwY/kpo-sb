package hse.bank.patterns.proxy;

import hse.bank.domain.BankAccount;

import java.util.List;
import java.util.UUID;

public interface BankAccountRepository {
    BankAccount findById(UUID id);
    List<BankAccount> findAll();
    void save(BankAccount account);
    void delete(UUID id);
}