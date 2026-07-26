package com.ratrod.archaion.api.entity;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ActionManager<T extends Entity> {

    private final T entity;
    private final List<ManagedAction<T>> action = new ArrayList<>();
    private final Random random = new Random();

    private @Nullable ManagedAction<T> currentAction;

    public ActionManager(T entity) {
        this.entity = entity;
    }

    public final void addAction(ManagedAction<T> tasks) {
        this.action.add(tasks);
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

    private void tryStartNewTask() {
        List<ManagedAction<T>> availableActions = new ArrayList<>();
        int totalWeight = 0;

        for (ManagedAction<T> act : this.action) {
            if (act.canStart()) {
                int w = act.getWeight();
                if (w > 0) {
                    availableActions.add(act);
                    totalWeight += w;
                }
            }
        }

        if (availableActions.isEmpty() || totalWeight <= 0) {
            return;
        }

        int roll = random.nextInt(totalWeight);
        int cumulative = 0;

        ManagedAction<T> selected = null;

        for (ManagedAction<T> act : availableActions) {
            cumulative += act.getWeight();
            if (roll < cumulative) {
                selected = act;
                break;
            }
        }

        if (selected == null) {
            selected = availableActions.get(availableActions.size() - 1);
        }

        this.currentAction = selected;
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
