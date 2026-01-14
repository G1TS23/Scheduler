import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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

    /**
     * Function that deletes a task from the scheduler
     * @param name
     */
    public Task deleteTask(String name){
        Task task = this.tasks.remove(name);
        if (Objects.isNull(task)){
            throw new IllegalArgumentException("task does not exist");
        }
        return task;
    }

    public void update(){
        Instant now = this.clock.instant().truncatedTo(ChronoUnit.MINUTES);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, this.clock.getZone());
        CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
        CronParser parser = new CronParser(cronDefinition);

        this.tasks.forEach((name, task) -> {
            ExecutionTime executionTime = ExecutionTime.forCron(parser.parse(task.getPeriodicity()));
            if(executionTime.isMatch(zonedDateTime)) {
                task.getRunnable().run();
            }
        });
    }
}
