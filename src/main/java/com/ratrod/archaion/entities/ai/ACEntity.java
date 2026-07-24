package com.ratrod.archaion.entities.ai;

import com.ratrod.archaion.api.client.animation.EntityAnimationManager;
import com.ratrod.archaion.api.entity.ActionManager;
import com.ratrod.archaion.client.clientdata.ClientBossBarData;
import com.ratrod.archaion.network.ACNetwork;
import com.ratrod.archaion.network.s2c.RemoveBossBarDataPacket;
import com.ratrod.archaion.network.s2c.SyncBossBarDataPacket;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

import java.util.List;

public interface ACEntity {
    default float getRotationFreedom() {
        return 1.0F;
    }

    default EntityAnimationManager getAnimationManager() {
        return null;
    }

    default List<ActionManager> getActionManagers() {
        return List.of();
    }

    default boolean attackTarget(ServerLevel level, Entity target, float damageModifier, Operation operation) {
        boolean flag = false;
        if (target != null) {
            return this.acSelf().doHurtTarget(level, target);
        }
        return flag;
    }

    default boolean attackTargetAddition(ServerLevel level, Entity target, float damageModifier) {
        return this.attackTarget(level, target, damageModifier, Operation.ADD);
    }

    default boolean attackTargetMultiplication(ServerLevel level, Entity target, float damageModifier) {
        return this.attackTarget(level, target, damageModifier, Operation.MULTIPLY);
    }

    default void addBossBarPlayer(ServerBossEvent bossEvent, ServerPlayer player, int bossIdx) {
        bossEvent.addPlayer(player);
        ACNetwork.sendToPlayer(player, new SyncBossBarDataPacket(bossEvent.getId(), bossIdx));
    }

    default void removeBossBarPlayer(ServerBossEvent bossEvent, ServerPlayer player) {
        bossEvent.removePlayer(player);
        ACNetwork.sendToPlayer(player, new RemoveBossBarDataPacket(bossEvent.getId()));
    }

    enum Operation {
        MULTIPLY,
        ADD
    }

    default Mob acSelf() {
        return (Mob) this;
    }
}
