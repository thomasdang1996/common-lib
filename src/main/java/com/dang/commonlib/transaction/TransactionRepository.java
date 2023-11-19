package com.dang.commonlib.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("SELECT new com.dang.commonlib.transaction.MessageDto (t.messageId,t.replyClassName,t.replyMessage) FROM Transaction t WHERE t.messageId=?1")
    Optional<MessageDto> getReplyMessage(UUID messageId);

    @Query("SELECT new com.dang.commonlib.transaction.MessageDto (t.messageId,t.requestClassName,t.requestMessage) FROM Transaction t WHERE t.messageId=?1")
    Optional<MessageDto> getRequestMessage(UUID messageId);

    @Query("SELECT t.threadId FROM Transaction t WHERE t.messageId=?1")
    Optional<Long> getThreadIdByMessageId(UUID messageId);
    @Query("UPDATE Transaction t SET t.replyMessage=?2, t.replyClassName=?3 WHERE t.messageId=?1")
    @Modifying
    void updateReplyMessage(UUID messageId, String replyMessage, String replyClassName);
}
