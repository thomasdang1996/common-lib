package com.dang.commonlib.transaction;

import com.dang.commonlib.utils.StringUtils;
import jakarta.transaction.Transactional;
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
@Transactional
    public void saveEventAndContinue(UUID eventId, Object event) {
        Transaction transaction = transactionRepository
                .findByMessageId(eventId)
                .orElseThrow(
                        () -> new RuntimeException("Transaction record with eventId " + eventId + " was not found. ")
                );
        transaction.setReplyMessage(stringUtils.toString(event));
        transactionRepository.save(transaction);
        long threadId = transaction.getThreadId();
        Thread eventThread = getThread(threadId);
        synchronized (eventThread) {
            eventThread.notify();
        }
        log.info("Continuing transaction");
    }

    public SpecificRecord getReplyMessage(UUID eventId) {
        return stringUtils.toObject(
                transactionRepository
                        .getReplyMessageByEventId(eventId)
                        .orElseThrow(
                                () -> new RuntimeException("ReplyMessage was not found, eventId: " + eventId)
                        ),
                SpecificRecord.class
        );
    }

    public Object getRequestMessage(UUID eventId) {
        return stringUtils.toObject(
                transactionRepository
                        .getRequestMessageByEventId(eventId)
                        .orElseThrow(
                                () -> new RuntimeException("RequestMessage was not found, eventId: " + eventId)
                        ),
                Object.class
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
