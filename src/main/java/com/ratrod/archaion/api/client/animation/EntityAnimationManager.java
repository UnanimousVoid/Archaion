package com.ratrod.archaion.api.client.animation;

import com.ratrod.archaion.network.ACNetwork;
import com.ratrod.archaion.network.s2c.ManageAnimationStatePacket;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.Entity;

public class EntityAnimationManager {
    public final Int2ObjectOpenHashMap<ACAnimation> animationMap = new Int2ObjectOpenHashMap<>();
    public final Entity entity;
    private int registeringId;

    public EntityAnimationManager(Entity entity) {
        this.entity = entity;
    }

    public void registerAnimation(ACAnimation animation) {
        animation.setId(registeringId++);
        if (entity.level().isClientSide()) {
            this.animationMap.put(animation.getId(), animation);
        }
    }

    public void startAnimation(ACAnimation animation) {
        if (!entity.level().isClientSide()) {
            ACNetwork.sendToAll(new ManageAnimationStatePacket(entity.getId(), animation.getId(), ManageAnimationStatePacket.Action.START));
        } else {
            animation.getState().startIfStopped(entity.tickCount);
        }
    }

    public void forceStartAnimation(ACAnimation animation) {
        if (!entity.level().isClientSide()) {
            ACNetwork.sendToAll(new ManageAnimationStatePacket(entity.getId(), animation.getId(), ManageAnimationStatePacket.Action.FORCE_START));
        } else {
            animation.getState().start(entity.tickCount);
        }
    }

    public void stopAnimation(ACAnimation animation) {
        if (!entity.level().isClientSide()) {
            ACNetwork.sendToAll(new ManageAnimationStatePacket(entity.getId(), animation.getId(), ManageAnimationStatePacket.Action.STOP));
        } else {
            animation.getState().stop();
        }
    }

    public ACAnimation getAnimation(int id) {
        return this.animationMap.get(id);
    }

    public Int2ObjectOpenHashMap<ACAnimation> getAnimationMap() {
        return animationMap;
    }
}
