package com.dang.commonlib.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionSynchronizer {
    private final TransactionRepository transactionRepository;
    private static final long TIMEOUT_MILLIS = 60000;

    public long registerTransaction(UUID eventId) {
        long threadId = Thread.currentThread().getId();
        Transaction transaction = new Transaction();
        transaction.setEventId(eventId);
        transaction.setThreadId(threadId);
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

    public void resume(UUID eventId) {
        Long threadId = transactionRepository
                .getThreadIdByEventId(eventId)
                .orElseThrow(
                        () -> new RuntimeException("Transaction with event id " + eventId + " not found")
                );
        Thread eventThread = getThread(threadId);
        synchronized (eventThread) {
            eventThread.notify();
        }
        log.info("Continuing transaction");
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
