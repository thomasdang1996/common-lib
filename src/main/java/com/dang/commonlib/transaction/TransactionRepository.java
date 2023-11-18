package com.dang.commonlib.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("SELECT t FROM Transaction t WHERE t.messageId=?1")
    @Modifying
    Optional<Transaction> findByMessageId(UUID messageId);

    @Query("SELECT t.replyMessage FROM Transaction t WHERE t.messageId=?1")
    Optional<String> getReplyMessageByEventId(UUID messageId);

    @Query("SELECT t.requestMessage FROM Transaction t WHERE t.messageId=?1")
    Optional<String> getRequestMessageByEventId(UUID messageId);
}
