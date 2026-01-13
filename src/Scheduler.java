import java.time.Clock;
import java.util.Objects;

public class Scheduler {
    private final Clock clock;

    public Scheduler(Clock clock) {
        if (Objects.isNull(clock)) {
            throw new IllegalArgumentException("clock cannot be null");
        }
        this.clock = clock;
    }
}
