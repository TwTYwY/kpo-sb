package com.gozon.payments.api;

import com.gozon.payments.api.dto.BalanceResponse;
import com.gozon.payments.api.dto.CreateAccountRequest;
import com.gozon.payments.api.dto.DepositRequest;
import com.gozon.payments.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class PaymentController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Void> createAccount(@RequestParam("userId") String userId) {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setUserId(userId);
        accountService.createAccount(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(
            @RequestParam("userId") String userId,
            @RequestParam("amount") Long amount
    ) {
        DepositRequest request = new DepositRequest();
        request.setUserId(userId);
        request.setAmountCents(amount);
        accountService.deposit(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> getBalance(@RequestParam("userId") String userId) {
        BalanceResponse balanceResponse = accountService.getBalance(userId);
        return ResponseEntity.ok(balanceResponse);
    }
}