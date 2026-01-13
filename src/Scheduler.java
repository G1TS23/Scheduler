import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Scheduler {
    private final Clock clock;
    private final List<Object> tasks;

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
    public List<Object> getTasks() {
        return this.tasks;
    }
}
