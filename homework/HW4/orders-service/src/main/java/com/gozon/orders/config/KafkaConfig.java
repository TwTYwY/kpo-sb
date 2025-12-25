package com.gozon.orders.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Value("${orders.kafka.payment-request-topic}")
    private String paymentRequestTopic;

    @Value("${orders.kafka.payment-status-topic}")
    private String paymentStatusTopic;

    @Bean
    public NewTopic paymentRequestTopic() {
        return new NewTopic(paymentRequestTopic, 3, (short) 1);
    }

    @Bean
    public NewTopic paymentStatusTopic() {
        return new NewTopic(paymentStatusTopic, 3, (short) 1);
    }
}