import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    /**
     * Verifies task modification updates periodicity
     */
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

    /**
     * Tests task's runnable modification
     */
    @Test
    void doitModifierLeRunnableDUneTache(){
        HashMap<String, Task> tasks = scheduler.getTasks();
        Runnable backupEveryday = () -> {System.out.println("backup everyday");};
        Runnable backupEvery2days = () -> {System.out.println("backup every 2 days");};

        scheduler.setTask("backup", "* * 12 1/1 * ? *", backupEveryday);
        assertEquals("backup", tasks.get("backup").getName());
        assertEquals("* * 12 1/1 * ? *", tasks.get("backup").getPeriodicity());
        assertDoesNotThrow(() -> tasks.get("backup").getRunnable().run());
        assertEquals(backupEveryday, tasks.get("backup").getRunnable());

        scheduler.setTask("backup", "* * 12 1/2 * ? *", backupEvery2days);
        assertEquals("backup", tasks.get("backup").getName());
        assertEquals("* * 12 1/2 * ? *", tasks.get("backup").getPeriodicity());
        assertDoesNotThrow(() -> tasks.get("backup").getRunnable().run());
        assertEquals(backupEvery2days, tasks.get("backup").getRunnable());
    }

    /**
     * Tests null parameter handling; expects exceptions
     */
    @Test
    void doitRetournerErreurSiParametreNull() {
        assertThrows(IllegalArgumentException.class, () -> scheduler.setTask(null, "* * 12 1/1 * ? *", () -> {System.out.println("backup");}));
        assertThrows(IllegalArgumentException.class, () -> scheduler.setTask("backup", null, () -> {System.out.println("backup");}));
        assertThrows(IllegalArgumentException.class, () -> scheduler.setTask("backup", "* * 12 1/1 * ? *", null));
    }

    /**
     * Tests task deletion; validates state after deletion
     */
    @Test
    void doitSupprimerUneTache(){
        HashMap<String, Task> tasks = scheduler.getTasks();
        assertDoesNotThrow(() -> scheduler.setTask("backup", "* * 12 1/1 * ? *", () -> {System.out.println("backup");}));
        assertEquals(1, scheduler.getTasks().size());
        assertNotNull(tasks.get("backup"));

        assertDoesNotThrow(() -> scheduler.deleteTask("backup"));
        assertEquals(0, scheduler.getTasks().size());
        assertNull(tasks.get("backup"));
    }

    /**
     * Tests task deletion; expects exception for non-existent task
     */
    @Test
    void doitEmettreUneExceptionLorsDeLaSuppressionDeTacheInexistante(){
        HashMap<String, Task> tasks = scheduler.getTasks();
        assertDoesNotThrow(() -> scheduler.setTask("backup", "* * 12 1/1 * ? *", () -> {System.out.println("backup");}));

        assertThrows(IllegalArgumentException.class, () -> scheduler.deleteTask("save"));
    }

    @Test
    void doitEmettreUneExceptionLorsDeLaSuppressionAvecNomNull(){
        HashMap<String, Task> tasks = scheduler.getTasks();
        assertDoesNotThrow(() -> scheduler.setTask("backup", "* * 12 1/1 * ? *", () -> {System.out.println("backup");}));

        assertThrows(IllegalArgumentException.class, () -> scheduler.deleteTask(null));
    }

    @Test
    void doitLancerLaTacheALHeure(){
        Clock mockClock = Clock.fixed(Instant.ofEpochSecond(39600L), ZoneId.of("Europe/Paris"));
        Scheduler scheduler = new Scheduler(mockClock);
        Runnable mockRunnable = mock(Runnable.class);
        scheduler.setTask("backup", "* * 12 1/1 * ? *", mockRunnable);
        scheduler.update();
        verify(mockRunnable).run();
    }
}
