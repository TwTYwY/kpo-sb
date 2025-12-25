package com.gozon.payments.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payment_inbox_tasks", uniqueConstraints = {
        @UniqueConstraint(columnNames = "messageKey")
})
@Getter
@Setter
@NoArgsConstructor
public class PaymentInboxTask {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String messageKey;

    @Lob
    @Column(nullable = false)
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentTaskStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    @Column
    private Instant processedAt;

    @Column
    private String resultStatus;

    @Column
    private String resultReason;
}