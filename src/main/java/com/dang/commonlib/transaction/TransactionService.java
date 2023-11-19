package com.dang.commonlib.transaction;

import com.dang.commonlib.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final StringUtils stringUtils;

    @Transactional
    public void updateReplyMessage(UUID messageId, Object message) {
        transactionRepository
                .updateReplyMessage(
                        messageId,
                        stringUtils.toString(message),
                        message.getClass().getTypeName()
                );
    }

    public void saveTransaction(UUID messageId, Object message, long threadId) {
        Transaction transaction = new Transaction();
        transaction.setMessageId(messageId);
        transaction.setThreadId(threadId);
        transaction.setRequestMessage(stringUtils.toString(message));
        transaction.setRequestClassName(message.getClass().getTypeName());
        transactionRepository.save(transaction);
    }

    public SpecificRecord getReplyMessage(UUID messageId) {
        MessageDto message = transactionRepository.getReplyMessage(messageId)
                .orElseThrow(
                        () -> new RuntimeException("Reply message not found:" + messageId)
                );
        return stringUtils.toObject(
                message.getValue(),
                message.getClassName()
        );
    }

    public SpecificRecord getRequestMessage(UUID messageId) {
        MessageDto message = transactionRepository.getRequestMessage(messageId)
                .orElseThrow(
                        () -> new RuntimeException("Request message not found:" + messageId)
                );
        return stringUtils.toObject(
                message.getValue(),
                message.getClassName()
        );
    }

    public long getThreadId(UUID messageId) {
        return transactionRepository.getThreadIdByMessageId(messageId)
                .orElseThrow(
                        () -> new RuntimeException("ThreadId was not found, eventId: " + messageId)
                );
    }
}
