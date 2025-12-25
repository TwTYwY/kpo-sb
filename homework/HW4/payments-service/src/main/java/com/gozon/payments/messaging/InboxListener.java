package com.gozon.payments.messaging;

import com.gozon.payments.service.PaymentProcessingService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InboxListener {
    private final PaymentProcessingService paymentProcessingService;

    @KafkaListener(topics = "${payments.kafka.payment-request-topic}", groupId = "payments-service-group")
    public void onPaymentRequest(ConsumerRecord<String, String> record) {
        paymentProcessingService.ensureInboxTask(record.key(), record.value());
    }
}