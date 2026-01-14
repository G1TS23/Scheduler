import java.time.Clock;
import java.util.*;

public class Scheduler {

    private final Clock clock;
    private final HashMap<String, Task> tasks;

    public Scheduler(Clock clock) {
        if (Objects.isNull(clock)) {
            throw new IllegalArgumentException("clock cannot be null");
        }
        this.clock = clock;
        this.tasks = new HashMap<>();
    }

    /**
     * Function that returns the list of tasks
     * @return list of tasks
     */
    public HashMap<String, Task> getTasks() {
        return this.tasks;
    }

    /**
     * Function that adds or modify a task to the scheduler
     * @param name
     * @param periodicity
     * @param runnable
     */
    public void setTask(String name, String periodicity, Runnable runnable){
        if (Objects.isNull(name) || Objects.isNull(periodicity) || Objects.isNull(runnable)) {
            throw new IllegalArgumentException("parameter cannot be null");
        }
        Task task = new Task(name, periodicity, runnable);
        this.tasks.put(name, task);
    }

    public void deleteTask(String name){
        this.tasks.remove(name);
    }
}
