package com.ratrod.archaion.api.client.animation;

import com.ratrod.archaion.network.ACNetwork;
import com.ratrod.archaion.network.s2c.ManageAnimationStatePacket;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

public class EntityAnimationManager {
    public final Object2ObjectOpenHashMap<String, Pair<AnimationState, Supplier<?>>> animationStateMap = new Object2ObjectOpenHashMap<>();

    public final Entity entity;

    public EntityAnimationManager(Entity entity) {
        this.entity = entity;
    }

    public void registerAnimation(String name, Supplier<?> definition) {
        if (entity.level().isClientSide()) {
            AnimationState state = new AnimationState();
            this.getAnimationStateMap().put(name, Pair.of(state, definition));
        }
    }

    public void startAnimation(String name) {
        if (!entity.level().isClientSide()) {
            ACNetwork.sendToAll(new ManageAnimationStatePacket(entity.getId(), name, ManageAnimationStatePacket.Action.START));
        } else {
            this.getAnimationState(name).startIfStopped(entity.tickCount);
        }
    }

    public void forceStartAnimation(String name) {
        if (!entity.level().isClientSide()) {
            ACNetwork.sendToAll(new ManageAnimationStatePacket(entity.getId(), name, ManageAnimationStatePacket.Action.FORCE_START));
        } else {
            this.getAnimationState(name).start(entity.tickCount);
        }
    }

    public void stopAnimation(String name) {
        if (!entity.level().isClientSide()) {
            ACNetwork.sendToAll(new ManageAnimationStatePacket(entity.getId(), name, ManageAnimationStatePacket.Action.STOP));
        } else {
            this.getAnimationState(name).stop();
        }
    }

    public Object2ObjectOpenHashMap<String, Pair<AnimationState, Supplier<?>>> getAnimationStateMap() {
        return animationStateMap;
    }

    public AnimationState getAnimationState(String name) {
        return this.getAnimationStateMap().get(name).first();
    }

    public Object getAnimationDefinition(String name) {
        Pair<AnimationState, Supplier<?>> pair = this.getAnimationStateMap().get(name);
        if (pair != null && pair.second() != null) {
            return pair.second().get();
        }
        return null;
    }
}
