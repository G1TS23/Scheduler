import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

public class SchedulerTest {

    @Test
    void doitInitialiserScheduler() {
        Scheduler scheduler = new Scheduler(Clock.system(ZoneId.of("Europe/Paris")));
        assertNotNull(scheduler);
    }
}
