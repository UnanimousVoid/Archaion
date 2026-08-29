package com.ratrod.archaion.api.entity;

import com.ratrod.archaion.misc.DynamicWeightedList;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.ToIntFunction;

public class ActionManager<T extends Entity> {

    private final T entity;
    private final DynamicWeightedList.Builder<ManagedAction<T>> actionBuilder = DynamicWeightedList.builder();
    private @Nullable DynamicWeightedList<ManagedAction<T>> action;
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

    public final void addAction(ManagedAction<T> task, ToIntFunction<ManagedAction<T>> weight) {
        this.actionBuilder.add(task, weight);
        this.dirty = true;
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

    private DynamicWeightedList<ManagedAction<T>> getActions() {
        if (this.action == null || this.dirty) {
            this.action = this.actionBuilder.build();
            this.dirty = false;
        }
        return this.action;
    }

    private void tryStartNewTask() {
        List<DynamicWeightedList.Entry<ManagedAction<T>>> availableActions = this.getActions().unwrap().stream()
                .filter(weighted -> weighted.value().canStart())
                .toList();

        if (availableActions.isEmpty()) {
            return;
        }

        RandomSource random = this.entity.getRandom();
        DynamicWeightedList.Builder<ManagedAction<T>> builder = DynamicWeightedList.builder();
        for (DynamicWeightedList.Entry<ManagedAction<T>> weighted : availableActions) {
            builder.add(weighted.value(), weighted.weight().applyAsInt(weighted.value()));
        }
        builder.build().getRandom(random).ifPresent(this::startAction);
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