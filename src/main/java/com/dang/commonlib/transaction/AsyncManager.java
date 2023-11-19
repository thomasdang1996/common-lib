package com.dang.commonlib.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Class for managing asynchronous calls (transactions).
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AsyncManager {
    private final TransactionService transactionService;
    private static final long TIMEOUT_MILLIS = 60000;

    /**
     * Registers a transaction with the respective object
     * @param transactionId id of the transaction
     * @param message object sent during said transaction
     * @return id of the waiting thread
     */
    public long registerTransaction(UUID transactionId, Object message) {
        long threadId = Thread.currentThread().getId();
        transactionService.saveTransaction(transactionId, message, threadId);
        log.info("Transaction registered, threadId: " + threadId);
        return threadId;
    }

    /**
     * Will wait until {@link #saveEventAndContinue(UUID, Object)} is called
     * @param threadId id of the waiting thread
     */
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

    /**
     * Saves received message and resumes with the waiting thread at {@link #waitForSync(long)}.
     * @param messageId id of the received message
     * @param message received message
     */
    public void saveEventAndContinue(UUID messageId, Object message) {
        long threadId = transactionService.getThreadId(messageId);
        Thread eventThread = getThread(threadId);
        synchronized (eventThread) {
            transactionService.updateReplyMessage(messageId,message);
            eventThread.notify();
        }
        log.info("Continuing transaction");
    }

    /**
     * Will get received message previously stored with {@link #saveEventAndContinue(UUID, Object)}
     * @param messageId id of the message
     * @return message of type {@link SpecificRecord}
     */
    public SpecificRecord getReplyMessage(UUID messageId) {
        return transactionService.getReplyMessage(messageId);
    }

    /**
     * Will get sent message previously stored with {@link #registerTransaction(UUID, Object)}
     * @param messageId id of the message
     * @return message of type {@link SpecificRecord}
     */
    public SpecificRecord getRequestMessage(UUID messageId) {
       return transactionService.getRequestMessage(messageId);
    }

    /**
     * Get thread by the thread Id
     * @param threadId id of the thread
     * @return thread
     */
    private Thread getThread(long threadId) {
        return Thread.getAllStackTraces()
                .keySet()
                .stream()
                .filter(thread -> thread.getId() == threadId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Thread with id " + threadId + " not found"));
    }
}
