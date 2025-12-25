package com.gozon.payments.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentInboxTaskRepository extends JpaRepository<PaymentInboxTask, UUID> {
    List<PaymentInboxTask> findTop50ByStatusOrderByCreatedAtAsc(PaymentTaskStatus status);
    Optional<PaymentInboxTask> findByMessageKey(String messageKey);
}