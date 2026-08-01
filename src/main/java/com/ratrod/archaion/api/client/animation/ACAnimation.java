package com.ratrod.archaion.api.client.animation;

import com.ratrod.archaion.entities.ai.ACEntity;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

public class ACAnimation {

    private int id = -1;
    private final AnimationState state;
    private final Supplier<AnimationDefinition> definition;
    private final EntityAnimationManager manager;

    public ACAnimation(Entity entity, Supplier<AnimationDefinition> definition) {
        this.state = new AnimationState();
        this.definition = definition;
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

    public AnimationDefinition getDefinition() {
        return definition.get();
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
