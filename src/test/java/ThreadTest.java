import it.unimol.diffusiontool.threads.StoppableThread;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("Test for threads")
public class ThreadTest {
    @Test
    public void mainTest() throws InterruptedException {
        AtomicInteger timeInt = new AtomicInteger();
        StoppableThread testThread = new StoppableThread(() -> {
            while (timeInt.get() < 5) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timeInt.getAndIncrement();
                System.out.println(timeInt.get());
            }
        });
        testThread.start();

        testThread.join();
        assertFalse(testThread.isAlive());

        System.out.println("""
                TERMINATED.
                
                TEST SUCCESSFUL.
                """);
    }
}
