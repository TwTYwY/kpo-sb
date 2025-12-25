package com.gozon.payments.messaging;

import com.gozon.payments.domain.OutboxEvent;
import com.gozon.payments.domain.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${payments.kafka.payment-status-topic}")
    private String paymentStatusTopic;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void publishOutbox() {
        List<OutboxEvent> events = outboxEventRepository.findTop50BySentFalseOrderByCreatedAtAsc();
        for (OutboxEvent event : events) {
            ProducerRecord<String, String> record = new ProducerRecord<>(paymentStatusTopic, event.getEventKey(), event.getPayload());
            kafkaTemplate.send(record);
            event.setSent(true);
        }
    }
}