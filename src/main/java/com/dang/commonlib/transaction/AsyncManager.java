package com.dang.commonlib.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class AsyncManager {
    private final TransactionService transactionService;
    private static final long TIMEOUT_MILLIS = 60000;

    public long registerTransaction(UUID messageId, Object message) {
        long threadId = Thread.currentThread().getId();
        transactionService.saveTransaction(messageId, message, threadId);
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

    public void saveEventAndContinue(UUID messageId, Object message) {
        long threadId = transactionService.getThreadId(messageId);
        Thread eventThread = getThread(threadId);
        synchronized (eventThread) {
            transactionService.updateReplyMessage(messageId,message);
            eventThread.notify();
        }
        log.info("Continuing transaction");
    }

    public SpecificRecord getReplyMessage(UUID messageId) {
        return transactionService.getReplyMessage(messageId);
    }

    public SpecificRecord getRequestMessage(UUID messageId) {
       return transactionService.getRequestMessage(messageId);
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
