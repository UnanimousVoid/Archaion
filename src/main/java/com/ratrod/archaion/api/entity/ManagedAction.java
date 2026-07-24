package com.ratrod.archaion.api.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;

import java.util.List;

public abstract class ManagedAction<T extends Entity> {

    protected final T entity;
    protected final List<Flag> flags = new ObjectArrayList<>();
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

    public int getWeight() {
        return 100;
    }

    public List<Flag> getFlags() {
        return flags;
    }

    @Deprecated
    public enum Flag {
        MOVEMENT,
        LOOK
    }
}
