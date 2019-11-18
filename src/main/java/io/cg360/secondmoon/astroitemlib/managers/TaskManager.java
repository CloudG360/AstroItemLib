package io.cg360.secondmoon.astroitemlib.managers;

import io.cg360.secondmoon.astroitemlib.AstroItemLib;
import io.cg360.secondmoon.astroitemlib.tasks.interfaces.IAstroTask;
import org.spongepowered.api.scheduler.Task;

import java.util.*;

public final class TaskManager {

    private int ticks;

    private HashMap<UUID, IAstroTask> tasks;
    private ArrayList<UUID> runningasynctasks;
    private ArrayList<UUID> suspendedtasks;
    private Task taskManagerRoot;

    public TaskManager() {
        this.ticks = 0;
        this.tasks = new HashMap<>();
        this.runningasynctasks = new ArrayList<>();
        this.suspendedtasks = new ArrayList<>();
    }

    public void process() {
        HashMap<UUID, IAstroTask> clone = new HashMap<>(tasks);
        for(Map.Entry<UUID, IAstroTask> entry: clone.entrySet()){
            if(runningasynctasks.contains(entry.getKey())){ continue; } //Skips it if Async (implied) and on list
            if(suspendedtasks.contains(entry.getKey())){ continue; } //Skips it if scheduled to rerun later.
            if(entry.getValue().isAsync()){
                Task.builder().async().execute(() -> {
                        runningasynctasks.add(entry.getKey());
                        entry.getValue().run();
                        if(entry.getValue().getRepeatRate() > 1){
                            suspendedtasks.add(entry.getKey());
                            Task.builder().delayTicks(entry.getValue().getRepeatRate())
                                    .execute(() -> {
                                        suspendedtasks.remove(entry.getKey());
                                    }).submit(AstroItemLib.get());
                        } else { tasks.remove(entry.getKey()); }
                        runningasynctasks.remove(entry.getKey());
                }).submit(AstroItemLib.get());
            } else {
                entry.getValue().run();
                if(entry.getValue().getRepeatRate() > 1){
                    suspendedtasks.add(entry.getKey());
                    Task.builder().delayTicks(entry.getValue().getRepeatRate())
                            .execute(() -> {
                                suspendedtasks.remove(entry.getKey());
                            }).submit(AstroItemLib.get());
                } else { tasks.remove(entry.getKey()); }
            }
        }
    }
    public void startTaskManager() {
        getManagerRoot().ifPresent(task -> {task.cancel(); taskManagerRoot = null; }); //Clear last process
        this.tasks = new HashMap<>();
        this.suspendedtasks = new ArrayList<>();
        this.runningasynctasks = new ArrayList<>();
        taskManagerRoot = Task.builder().intervalTicks(1).name("TaskManagerRoot")
                .execute(r -> {
                    process();
                }).submit(AstroItemLib.get());
    }

    public Optional<Task> getManagerRoot() { return Optional.ofNullable(this.taskManagerRoot); }
}
