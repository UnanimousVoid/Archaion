package com.ratrod.archaion.api.entity;

import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.List;

public class ActionManager<T extends Entity> {

    private final T entity;
    private final SimpleWeightedRandomList.Builder<ManagedAction<T>> actionBuilder = SimpleWeightedRandomList.builder();
    private @Nullable SimpleWeightedRandomList<ManagedAction<T>> action;
    private boolean dirty = true;


    private @Nullable ManagedAction<T> currentAction;
    private @Nullable ManagedAction<T> priorityAction;

    public ActionManager(T entity) {
        this.entity = entity;
    }

    public void setPriorityAction(ManagedAction<T> action) {
        this.priorityAction = action;
    }

    public final void addAction(ManagedAction<T> task, int weight) {
        if (weight > 0) {
            this.actionBuilder.add(task, weight);
            this.dirty = true;
        } else {
            throw new IllegalStateException("Weight of a ManagedAction cannot be 0 or below.");
        }
    }

    public void tick() {

        if (!entity.isAlive()) {
            this.stopCurrentAction();
            return;
        }

        if (this.priorityAction != null && this.priorityAction.canStart()) {
            if (this.currentAction != this.priorityAction) {
                this.stopCurrentAction();
                this.startAction(this.priorityAction);
            }
            if (!this.priorityAction.onTick()) {
                this.stopCurrentAction();
            }
            return;
        }

        if (this.currentAction != null) {
            if (!this.currentAction.onTick()) {
                this.stopCurrentAction();
                this.tryStartNewTask();
            }
        } else {
            this.tryStartNewTask();
        }
    }

    private SimpleWeightedRandomList<ManagedAction<T>> getActions() {
        if (this.action == null || this.dirty) {
            this.action = this.actionBuilder.build();
            this.dirty = false;
        }
        return this.action;
    }

    private void tryStartNewTask() {
        List<WeightedEntry.Wrapper<ManagedAction<T>>> availableActions = this.getActions().unwrap().stream()
                .filter(weighted -> weighted.data().canStart())
                .toList();

        if (availableActions.isEmpty()) {
            return;
        }

        RandomSource random = this.entity.getRandom();
        SimpleWeightedRandomList.Builder<ManagedAction<T>> builder = SimpleWeightedRandomList.builder();
        for (WeightedEntry.Wrapper<ManagedAction<T>> weighted : availableActions) {
            builder.add(weighted.data(), weighted.getWeight().asInt());
        }
        builder.build().getRandomValue(random).ifPresent(this::startAction);
    }

    private void startAction(ManagedAction<T> action) {
        this.currentAction = action;
        this.currentAction.onStart();
    }


    public boolean isBusy() {
        return this.currentAction != null;
    }

    @Nullable
    public ManagedAction<T> getCurrentAction() {
        return currentAction;
    }

    public void stopCurrentAction() {
        if (this.currentAction != null) {
            this.currentAction.onStop();
            this.currentAction = null;
        }
    }
}