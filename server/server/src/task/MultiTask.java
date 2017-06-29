package task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class MultiTask implements Callable<Void> {

    private List<Callable> tasks = new ArrayList<>();

    public MultiTask addTask(Callable task) {
        this.tasks.add(task);
        return this;
    }

    @Override
    public Void call() throws Exception {
        for (Callable task : this.tasks) {
            task.call();
        }
        return null;
    }
}
