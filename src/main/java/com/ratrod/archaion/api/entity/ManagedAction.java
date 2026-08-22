package com.ratrod.archaion.api.entity;

import net.minecraft.world.entity.Entity;

public abstract class ManagedAction<T extends Entity> {

    protected final T entity;
    protected int timer;

    public ManagedAction(T entity) {
        this.entity = entity;
    }

    public abstract boolean canStart();

    public abstract boolean onTick();

    public void onStart() {
    }

    public void onStop() {
    }

    public int getTimer() {
        return timer;
    }
}