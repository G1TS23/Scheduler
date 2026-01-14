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
    private Runnable mockRunnable;

    @BeforeEach
    void setUp() {
        scheduler = new Scheduler(Clock.system(ZoneId.of("Europe/Paris")));
        mockRunnable = mock(Runnable.class);
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
        assertDoesNotThrow(() -> scheduler.setTask("backup", "* * 12 1/1 * ? *", mockRunnable));
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

        scheduler.setTask("backup", "* * 12 1/1 * ? *", mockRunnable);
        assertEquals("backup", tasks.get("backup").getName());
        assertEquals("* * 12 1/1 * ? *", tasks.get("backup").getPeriodicity());

        scheduler.setTask("backup", "* * 12 1/2 * ? *", mockRunnable);
        assertEquals("backup", tasks.get("backup").getName());
        assertEquals("* * 12 1/2 * ? *", tasks.get("backup").getPeriodicity());
    }

    /**
     * Tests task's runnable modification
     */
    @Test
    void doitModifierLeRunnableDUneTache(){
        HashMap<String, Task> tasks = scheduler.getTasks();
        Runnable mockRunnable1 = mock(Runnable.class);
        Runnable mockRunnable2 = mock(Runnable.class);

        scheduler.setTask("backup", "* * 12 1/1 * ? *", mockRunnable1);
        assertEquals("backup", tasks.get("backup").getName());
        assertEquals("* * 12 1/1 * ? *", tasks.get("backup").getPeriodicity());
        assertDoesNotThrow(() -> tasks.get("backup").getRunnable().run());
        assertEquals(mockRunnable1, tasks.get("backup").getRunnable());

        scheduler.setTask("backup", "* * 12 1/2 * ? *", mockRunnable2);
        assertEquals("backup", tasks.get("backup").getName());
        assertEquals("* * 12 1/2 * ? *", tasks.get("backup").getPeriodicity());
        assertDoesNotThrow(() -> tasks.get("backup").getRunnable().run());
        assertEquals(mockRunnable2, tasks.get("backup").getRunnable());
    }

    /**
     * Tests null parameter handling; expects exceptions
     */
    @Test
    void doitRetournerErreurSiParametreNull() {
        assertThrows(IllegalArgumentException.class, () -> scheduler.setTask(null, "* * 12 1/1 * ? *", mockRunnable));
        assertThrows(IllegalArgumentException.class, () -> scheduler.setTask("backup", null, mockRunnable));
        assertThrows(IllegalArgumentException.class, () -> scheduler.setTask("backup", "* * 12 1/1 * ? *", null));
    }

    /**
     * Tests task deletion; validates state after deletion
     */
    @Test
    void doitSupprimerUneTache(){
        HashMap<String, Task> tasks = scheduler.getTasks();
        assertDoesNotThrow(() -> scheduler.setTask("backup", "* * 12 1/1 * ? *", mockRunnable));
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
        assertDoesNotThrow(() -> scheduler.setTask("backup", "* * 12 1/1 * ? *", mockRunnable));

        assertThrows(IllegalArgumentException.class, () -> scheduler.deleteTask("save"));
    }

    @Test
    void doitEmettreUneExceptionLorsDeLaSuppressionAvecNomNull(){
        HashMap<String, Task> tasks = scheduler.getTasks();
        assertDoesNotThrow(() -> scheduler.setTask("backup", "* * 12 1/1 * ? *", mockRunnable));

        assertThrows(IllegalArgumentException.class, () -> scheduler.deleteTask(null));
    }

    /**
     * Test that the task runs at the scheduled time
     */
    @Test
    void doitLancerLaTacheALHeure(){
        Clock mockClock = Clock.fixed(Instant.ofEpochSecond(39600L), ZoneId.of("Europe/Paris"));
        Scheduler scheduler = new Scheduler(mockClock);
        scheduler.setTask("backup", "* * 12 1/1 * ? *", mockRunnable);
        scheduler.update();
        verify(mockRunnable).run();
    }

    /**
     * Task does not run when schedule is unmet
     */
    @Test
    void neDoitPasLancerLaTache(){
        Clock mockClock = Clock.fixed(Instant.ofEpochSecond(39600L), ZoneId.of("Europe/Paris"));
        Scheduler scheduler = new Scheduler(mockClock);
        scheduler.setTask("backup", "* * 13 1/1 * ? *", mockRunnable);
        scheduler.update();
        verify(mockRunnable, times(0)).run();
    }

    /**
     * Task runs periodically according to schedule
     */
    @Test
    void doitLancerUneTacheSelonSaPeriodicite() throws InterruptedException {
        Clock clock = Clock.system(ZoneId.of("Europe/Paris"));
        Scheduler scheduler = new Scheduler(clock);
        scheduler.setTask("backup", "*/5 * * * * ? *", mockRunnable);
        for(int i = 0; i < 20; i++) {
            scheduler.update();
            Thread.sleep(1000L);
        }
        verify(mockRunnable, times(4)).run();
    }

    @Test
    void doitLancerPlusieursTachesSelonLeurPeriodicite() throws InterruptedException {
        Clock clock = Clock.system(ZoneId.of("Europe/Paris"));
        Scheduler scheduler = new Scheduler(clock);
        Runnable mockRunnable1 = mock(Runnable.class);
        Runnable mockRunnable2 = mock(Runnable.class);
        scheduler.setTask("backup", "*/5 * * * * ? *", mockRunnable1);
        scheduler.setTask("backup2", "*/10 * * * * ? *", mockRunnable2);
        for(int i = 0; i < 20; i++) {
            scheduler.update();
            Thread.sleep(1000L);
        }
        verify(mockRunnable1, times(4)).run();
        verify(mockRunnable2, times(2)).run();
    }

    @Test
    void doitExecuterUpdateSansTache(){
        Clock clock = Clock.system(ZoneId.of("Europe/Paris"));
        Scheduler scheduler = new Scheduler(clock);
        assertDoesNotThrow(scheduler::update);
    }
}
