package com.gozon.payments.messaging;

import com.gozon.payments.service.PaymentProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InboxProcessorScheduler {
    private final PaymentProcessingService paymentProcessingService;

    @Scheduled(fixedDelay = 1000)
    public void processInbox() {
        paymentProcessingService.processPendingTasks();
    }
}