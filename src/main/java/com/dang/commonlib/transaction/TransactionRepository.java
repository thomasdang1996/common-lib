package com.dang.commonlib.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query("SELECT t.threadId FROM Transaction t WHERE t.eventId =?1")
    Optional<Long> getThreadIdByEventId(UUID eventId);
}
