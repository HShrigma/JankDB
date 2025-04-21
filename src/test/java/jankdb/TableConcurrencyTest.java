package jankdb;

import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.*;

public class TableConcurrencyTest {
    private Table table;
    private final String USER1 = "user1";
    private final String USER2 = "user2";

    @BeforeEach
    void setUp() {
        table = new Table("concurrent_test");
    }

    @Test
    void testConcurrentWriteLocking() throws InterruptedException, ExecutionException {
        // Simulate two users trying to lock simultaneously
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        Future<Boolean> user1Result = executor.submit(() -> {
            latch.countDown();
            latch.await();
            return table.tryLock(USER1, 1, TimeUnit.SECONDS);
        });

        Future<Boolean> user2Result = executor.submit(() -> {
            latch.countDown();
            latch.await();
            return table.tryLock(USER2, 1, TimeUnit.SECONDS);
        });

        // Only one should get the lock
        assertTrue(user1Result.get() ^ user2Result.get(), 
                 "Exactly one user should get the lock");
        
        executor.shutdown();
    }

    @Test
    void testWriteWithLockAtomicity() throws Exception {
        table.writeWithLock(USER1, t -> {
            t.AddRecord(new Record("key=value;"));
            assertTrue(t.isLockedByOther(USER2), 
                      "Table should be locked during operation");
            return null;
        });

        assertEquals(1, table.Size());
    }

    @Test
    void testReadWithLockConcurrency() throws Exception {
        table.AddRecord(new Record("test=data;"));
    
        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(threadCount);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
    
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    startLatch.countDown();
                    startLatch.await(); // Wait for all threads
                    
                    table.readWithLock(t -> {
                        assertFalse(t.isLockedByOther("reader"));
                        endLatch.countDown();
                        return null;
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    fail("Thread interrupted");
                }
            });
        }
    
        assertTrue(endLatch.await(2, TimeUnit.SECONDS));
        executor.shutdown();
    }
}