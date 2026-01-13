import java.time.Clock;

public class Scheduler {
    private final Clock clock;

    public Scheduler(Clock clock) {
        if (clock == null) {
            throw new IllegalArgumentException("clock cannot be null");
        }
        this.clock = clock;
    }
}
