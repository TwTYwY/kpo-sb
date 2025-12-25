package com.gozon.payments.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gozon.payments.domain.*;
import com.gozon.payments.messaging.PaymentRequestEvent;
import com.gozon.payments.messaging.PaymentStatusEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentProcessingService {
    private final PaymentInboxTaskRepository inboxRepository;
    private final AccountRepository accountRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void ensureInboxTask(String messageKey, String payload) {
        if (inboxRepository.findByMessageKey(messageKey).isPresent()) {
            return;
        }
        PaymentInboxTask task = new PaymentInboxTask();
        task.setMessageKey(messageKey);
        task.setPayload(payload);
        task.setStatus(PaymentTaskStatus.NEW);
        task.setCreatedAt(Instant.now());
        inboxRepository.save(task);
    }

    @Transactional
    public void processPendingTasks() {
        List<PaymentInboxTask> tasks = inboxRepository.findTop50ByStatusOrderByCreatedAtAsc(PaymentTaskStatus.NEW);
        for (PaymentInboxTask task : tasks) {
            try {
                task.setStatus(PaymentTaskStatus.PROCESSING);
                PaymentRequestEvent event = objectMapper.readValue(task.getPayload(), PaymentRequestEvent.class);
                PaymentStatusEvent statusEvent = handlePayment(event);
                String payload = objectMapper.writeValueAsString(statusEvent);
                OutboxEvent outbox = new OutboxEvent();
                outbox.setEventKey(statusEvent.getEventId());
                outbox.setType("PAYMENT_STATUS");
                outbox.setPayload(payload);
                outbox.setAggregateId(statusEvent.getOrderId());
                outbox.setCreatedAt(Instant.now());
                outbox.setSent(false);
                outboxEventRepository.save(outbox);
                task.setStatus(PaymentTaskStatus.DONE);
                task.setProcessedAt(Instant.now());
                task.setResultStatus(statusEvent.getStatus());
                task.setResultReason(statusEvent.getReason());
            } catch (Exception e) {
                task.setStatus(PaymentTaskStatus.FAILED);
                task.setProcessedAt(Instant.now());
                task.setResultStatus("FAIL");
                task.setResultReason("INTERNAL_ERROR");
            }
        }
    }

    private PaymentStatusEvent handlePayment(PaymentRequestEvent event) {
        PaymentStatusEvent statusEvent = new PaymentStatusEvent();
        statusEvent.setEventId(UUID.randomUUID().toString());
        statusEvent.setOrderId(event.getOrderId());
        statusEvent.setUserId(event.getUserId());
        statusEvent.setAmount(event.getAmount());
        Account account = accountRepository.findByUserId(event.getUserId()).orElse(null);
        if (account == null) {
            statusEvent.setStatus("FAIL");
            statusEvent.setReason("NO_ACCOUNT");
            return statusEvent;
        }
        boolean debited = debitAccount(account, event.getAmount());
        if (!debited) {
            statusEvent.setStatus("FAIL");
            statusEvent.setReason("INSUFFICIENT_FUNDS");
            return statusEvent;
        }
        statusEvent.setStatus("SUCCESS");
        statusEvent.setReason("OK");
        return statusEvent;
    }

    private boolean debitAccount(Account account, BigDecimal amount) {
        boolean updated = false;
        int attempts = 0;
        while (!updated && attempts < 5) {
            attempts++;
            if (account.getBalance().compareTo(amount) < 0) {
                return false;
            }
            BigDecimal newBalance = account.getBalance().subtract(amount);
            account.setBalance(newBalance);
            try {
                accountRepository.saveAndFlush(account);
                updated = true;
            } catch (Exception e) {
                account = accountRepository.findById(account.getId()).orElse(null);
                if (account == null) {
                    return false;
                }
            }
        }
        return updated;
    }
}