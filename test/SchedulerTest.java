import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZoneId;
import java.util.HashMap;
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
        assertDoesNotThrow(() -> scheduler.setTask("backup", "* * 12 1/1 * ? *", () -> {System.out.println("backup");}));
        HashMap<String, Task> tasks = scheduler.getTasks();

        assertEquals(1, tasks.size());
        assertEquals("backup", tasks.get("backup").getName());
        assertEquals("* * 12 1/1 * ? *", tasks.get("backup").getPeriodicity());
        assertDoesNotThrow(() -> tasks.get("backup").getRunnable().run());
    }

    @Test
    void doitModifierUneTache(){
        HashMap<String, Task> tasks = scheduler.getTasks();

        scheduler.setTask("backup", "* * 12 1/1 * ? *", () -> {System.out.println("backup");});
        assertEquals("backup", tasks.get("backup").getName());
        assertEquals("* * 12 1/1 * ? *", tasks.get("backup").getPeriodicity());

        scheduler.setTask("backup", "* * 12 1/2 * ? *", () -> {System.out.println("backup");});
        assertEquals("backup", tasks.get("backup").getName());
        assertEquals("* * 12 1/2 * ? *", tasks.get("backup").getPeriodicity());
    }

    @Test
    void doitRetournerErreurSiParametreNull() {
        assertThrows(IllegalArgumentException.class, () -> scheduler.setTask(null, "* * 12 1/1 * ? *", () -> {System.out.println("backup");}));
        assertThrows(IllegalArgumentException.class, () -> scheduler.setTask("backup", null, () -> {System.out.println("backup");}));
        assertThrows(IllegalArgumentException.class, () -> scheduler.setTask("backup", "* * 12 1/1 * ? *", null));
    }

}
