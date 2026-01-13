import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SchedulerTest {

    @Test
    void doitInitialiserScheduler() {
        Scheduler scheduler = new Scheduler(Clock.system(ZoneId.of("Europe/Paris")));
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
        Scheduler scheduler = new Scheduler(Clock.system(ZoneId.of("Europe/Paris")));
        assertNotNull(scheduler.getTasks());
        assertTrue(scheduler.getTasks().isEmpty());
    }

    @Test
    void doitAjouterUneTache(){
        Scheduler scheduler = new Scheduler(Clock.system(ZoneId.of("Europe/Paris")));
        Scheduler.Task task = new Scheduler.Task("backup", "* * 12 1/1 * ? *", () -> {});

        scheduler.setTask("backup", "* * 12 1/1 * ? *", () -> {System.out.println("backup");});
        List<Scheduler.Task> tasks = scheduler.getTasks();

        assertEquals(1, tasks.size());
        assertEquals(task.name, tasks.getFirst().name);
        assertEquals(task.periodicity, tasks.getFirst().periodicity);
        assertDoesNotThrow(() ->tasks.getFirst().runnable.run());
    }
}
