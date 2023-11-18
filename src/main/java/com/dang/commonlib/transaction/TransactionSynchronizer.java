package com.dang.commonlib.transaction;

import com.dang.commonlib.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionSynchronizer {
    private final TransactionRepository transactionRepository;
    private final StringUtils stringUtils;
    private static final long TIMEOUT_MILLIS = 60000;

    public long registerTransaction(UUID messageId, Object message) {
        long threadId = Thread.currentThread().getId();
        Transaction transaction = new Transaction();
        transaction.setMessageId(messageId);
        transaction.setThreadId(threadId);
        transaction.setRequestMessage(stringUtils.toString(message));
        transaction.setRequestClassName(message.getClass().getTypeName());
        transactionRepository.save(transaction);
        log.info("Transaction registered, threadId: " + threadId);
        return threadId;
    }

    public void waitForSync(long threadId) {
        try {
            log.info("Wait for event to arrive.");
            Thread eventThread = getThread(threadId);
            synchronized (eventThread) {
                eventThread.wait(TIMEOUT_MILLIS);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveEventAndContinue(UUID eventId, Object event) {
        Transaction transaction = transactionRepository
                .findByMessageId(eventId)
                .orElseThrow(
                        () -> new RuntimeException("Transaction record with eventId " + eventId + " was not found. ")
                );
        transaction.setReplyMessage(stringUtils.toString(event));
        transaction.setReplyClassName(event.getClass().getTypeName());
        transactionRepository.save(transaction);
        long threadId = transaction.getThreadId();
        Thread eventThread = getThread(threadId);
        synchronized (eventThread) {
            eventThread.notify();
        }
        log.info("Continuing transaction");
    }

    public SpecificRecord getReplyMessage(UUID eventId) {
        Transaction transaction = transactionRepository
                .findByMessageId(eventId)
                .orElseThrow(
                        () -> new RuntimeException("transaction was not found, eventId: " + eventId)
                );
        return stringUtils.toObject(
                transaction.getReplyMessage(),
                transaction.getReplyClassName()
        );
    }

    public SpecificRecord getRequestMessage(UUID eventId) {
        Transaction transaction = transactionRepository
                .findByMessageId(eventId)
                .orElseThrow(
                        () -> new RuntimeException("transaction was not found, eventId: " + eventId)
                );
        return stringUtils.toObject(
                transaction.getRequestMessage(),
                transaction.getRequestClassName()
        );
    }

    private Thread getThread(long threadId) {
        return Thread.getAllStackTraces()
                .keySet()
                .stream()
                .filter(thread -> thread.getId() == threadId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Thread with id " + threadId + " not found"));
    }
}
