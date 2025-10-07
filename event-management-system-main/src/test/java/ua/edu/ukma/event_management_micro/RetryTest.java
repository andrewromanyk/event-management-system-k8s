package ua.edu.ukma.event_management_micro;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.edu.ukma.event_management_micro.core.CoreService;

import java.util.concurrent.atomic.AtomicInteger;

class RetryTest {

    CoreService coreService = new CoreService();

    @Test
    void testRetry() {
        Assertions.assertDoesNotThrow(() -> {
            coreService.retryCall(2, () -> {
                System.out.println("Nothing happens, no error");
                return null;
            }
            );
        });
    }

    @Test
    void testRetryError() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            coreService.retryCall(5, () -> {
                throw new RuntimeException("Always fails");
            });
        });
    }

    @Test
    void testRetryFailsTwoTime() {
        AtomicInteger attempt = new AtomicInteger(0);
        Assertions.assertDoesNotThrow(() -> {
            coreService.retryCall(5, () -> {
                if (attempt.get() <= 2) {
                    attempt.incrementAndGet();
                    System.out.println("Failing attempt " + attempt.get() );
                    throw new RuntimeException("Fails first two times");
                }
                System.out.println("Success on attempt " + attempt.get() );
                return null;
            });
        });
    }

}
