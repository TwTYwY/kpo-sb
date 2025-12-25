package com.gozon.payments.service;

import com.gozon.payments.api.dto.BalanceResponse;
import com.gozon.payments.api.dto.CreateAccountRequest;
import com.gozon.payments.api.dto.DepositRequest;
import com.gozon.payments.domain.Account;
import com.gozon.payments.domain.AccountRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional
    public void createAccount(CreateAccountRequest request) {
        if (accountRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new EntityExistsException();
        }
        Account account = new Account();
        account.setUserId(request.getUserId());
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);
    }

    @Transactional
    public void deposit(DepositRequest request) {
        Account account = accountRepository.findByUserId(request.getUserId()).orElseThrow();
        boolean updated = false;
        int attempts = 0;
        while (!updated && attempts < 5) {
            attempts++;
            try {
                BigDecimal newBalance = account.getBalance().add(BigDecimal.valueOf(request.getAmountCents(), 2));
                account.setBalance(newBalance);
                accountRepository.saveAndFlush(account);
                updated = true;
            } catch (OptimisticLockException e) {
                account = accountRepository.findById(account.getId()).orElseThrow();
            }
        }
        if (!updated) {
            throw new IllegalStateException();
        }
    }

    @Transactional(readOnly = true)
    public BalanceResponse getBalance(String userId) {
        Account account = accountRepository.findByUserId(userId).orElseThrow();
        return BalanceResponse.builder()
                .userId(account.getUserId())
                .balance(account.getBalance())
                .build();
    }
}