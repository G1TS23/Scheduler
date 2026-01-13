public class Task {
    private String name;
    private String periodicity;
    private Runnable runnable;

    public Task(String name, String periodicity, Runnable runnable){
        this.name = name;
        this.periodicity = periodicity;
        this.runnable = runnable;
    }

    public String getName() {
        return name;
    }
    public String getPeriodicity() {
        return periodicity;
    }
    public Runnable getRunnable() {
        return runnable;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPeriodicity(String periodicity) {
        this.periodicity = periodicity;
    }
    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }
}
