package com.dang.commonlib.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("UPDATE Transaction t SET t.replyMessage=?2 WHERE t.messageId=?1")
    @Modifying
    Optional<Transaction> updateResponseMessageByEventId(UUID messageId, String replyMessage);
}
