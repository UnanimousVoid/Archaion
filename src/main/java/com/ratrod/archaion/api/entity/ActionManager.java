package com.ratrod.archaion.api.entity;

import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.List;

public class ActionManager<T extends Entity> {

    private final T entity;
    private final WeightedList.Builder<ManagedAction<T>> actionBuilder = WeightedList.builder();
    private @Nullable WeightedList<ManagedAction<T>> action;
    private boolean dirty = true;

    private @Nullable ManagedAction<T> currentAction;

    public ActionManager(T entity) {
        this.entity = entity;
    }

    public final void addAction(ManagedAction<T> task, int weight) {
        if (weight > 0) {
            this.actionBuilder.add(task, weight);
            this.dirty = true;
        }
    }

    public void tick() {
        if (this.currentAction != null) {
            if (!this.currentAction.onTick() || !entity.isAlive()) {
                this.stopCurrentAction();
                this.tryStartNewTask();
            }
        } else {
            this.tryStartNewTask();
        }
    }

    private WeightedList<ManagedAction<T>> getActions() {
        if (this.action == null || this.dirty) {
            this.action = this.actionBuilder.build();
            this.dirty = false;
        }
        return this.action;
    }

    private void tryStartNewTask() {
        List<Weighted<ManagedAction<T>>> availableActions = this.getActions().unwrap().stream()
                .filter(weighted -> weighted.value().canStart())
                .toList();

        if (availableActions.isEmpty()) {
            return;
        }

        RandomSource random = this.entity.getRandom();
        WeightedList.of(availableActions).getRandom(random).ifPresent(this::startAction);
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