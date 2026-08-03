package com.ratrod.archaion.api.client.animation;

import com.ratrod.archaion.entities.ai.ACEntity;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;

public class ACAnimation {

    private int id = -1;
    private final AnimationState state;
    private final EntityAnimationManager manager;

    public ACAnimation(Entity entity) {
        this.state = new AnimationState();
        this.manager = ((ACEntity<?>) entity).getAnimationManager();
        this.manager.registerAnimation(this);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public AnimationState getState() {
        return state;
    }

    public void start() {
        manager.startAnimation(this);
    }

    public void forceStart() {
        manager.forceStartAnimation(this);
    }

    public void stop() {
        manager.stopAnimation(this);
    }
}
