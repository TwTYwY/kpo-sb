package com.gozon.orders.messaging;

import com.gozon.orders.domain.OutboxEvent;
import com.gozon.orders.domain.OutboxEventRepository;
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

    @Value("${orders.kafka.payment-request-topic}")
    private String paymentRequestTopic;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void publishOutbox() {
        List<OutboxEvent> events = outboxEventRepository.findTop50BySentFalseOrderByCreatedAtAsc();
        for (OutboxEvent event : events) {
            ProducerRecord<String, String> record = new ProducerRecord<>(paymentRequestTopic, event.getEventKey(), event.getPayload());
            kafkaTemplate.send(record);
            event.setSent(true);
        }
    }
}