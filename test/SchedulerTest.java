import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SchedulerTest {
    private Scheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new Scheduler(Clock.system(ZoneId.of("Europe/Paris")));
    }

    @Test
    void doitInitialiserScheduler() {
        assertNotNull(scheduler);
    }

    @Test
    void neDoitPasInitialiserScheduler() {
        assertThrows(IllegalArgumentException.class, () -> new Scheduler(null));
    }

    /**
     * Verifies scheduler returns empty task list after initialization
     */
    @Test
    void doitRetournerLaListeDesTachesVide() {
        assertNotNull(scheduler.getTasks());
        assertTrue(scheduler.getTasks().isEmpty());
    }

    /**
     * Adds task; verifies task properties and execution
     */
    @Test
    void doitAjouterUneTache(){
        Task task = new Task("backup", "* * 12 1/1 * ? *", () -> {});

        assertDoesNotThrow(() -> scheduler.setTask("backup", "* * 12 1/1 * ? *", () -> {System.out.println("backup");}));
        List<Task> tasks = scheduler.getTasks();

        assertEquals(1, tasks.size());
        assertEquals(task.getName(), tasks.getFirst().getName());
        assertEquals(task.getPeriodicity(), tasks.getFirst().getPeriodicity());
        assertDoesNotThrow(() ->tasks.getFirst().getRunnable().run());
    }
}
