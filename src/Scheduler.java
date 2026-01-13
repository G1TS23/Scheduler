import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Scheduler {

    public static class Task{
        public String name;
        public String periodicity;
        public Runnable runnable;

        public Task(String name, String periodicity, Runnable runnable){
            this.name = name;
            this.periodicity = periodicity;
            this.runnable = runnable;
        }
    }

    private final Clock clock;
    private final List<Task> tasks;

    public Scheduler(Clock clock) {
        if (Objects.isNull(clock)) {
            throw new IllegalArgumentException("clock cannot be null");
        }
        this.clock = clock;
        this.tasks = new ArrayList<>();
    }

    /**
     * Function that returns the list of tasks
     * @return list of tasks
     */
    public List<Task> getTasks() {
        return this.tasks;
    }

    public void setTask(String name, String periodicity, Runnable runnable){
        Task task = new Task(name, periodicity, runnable);
        this.tasks.add(task);
    }
}
