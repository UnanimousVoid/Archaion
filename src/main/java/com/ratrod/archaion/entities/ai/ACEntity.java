package com.ratrod.archaion.entities.ai;

import com.ratrod.archaion.api.client.animation.EntityAnimationManager;
import com.ratrod.archaion.api.entity.ActionManager;
import com.ratrod.archaion.misc.mixinhelpers.IMixinMob;
import com.ratrod.archaion.network.ACNetwork;
import com.ratrod.archaion.network.s2c.RemoveBossBarDataPacket;
import com.ratrod.archaion.network.s2c.SyncBossBarDataPacket;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.List;
import java.util.Map;

public interface ACEntity<L extends LivingEntity> {
    default float getRotationFreedom() {
        return 1.0F;
    }

    default EntityAnimationManager getAnimationManager() {
        return null;
    }

    default List<ActionManager<L>> getActionManagers() {
        return List.of();
    }

    default boolean attackTarget(ServerLevel level, Entity target, float damageModifier, Operation operation) {
        boolean flag = false;
        if (this.acSelf() instanceof IMixinMob mm) {
            mm.ac$setDamageModifier(Pair.of(damageModifier, operation));
            if (target != null) {
                return this.acSelf().doHurtTarget(level, target);
            }
            mm.ac$setDamageModifier(null);
        }
        return flag;
    }

    default void addBossBarPlayer(ServerBossEvent bossEvent, ServerPlayer player, int bossIdx) {
        this.addBossBarPlayer(bossEvent, player, bossIdx, Map.of());
    }

    default void addBossBarPlayer(ServerBossEvent bossEvent, ServerPlayer player, int bossIdx, Map<String, Integer> values) {
        bossEvent.addPlayer(player);
        ACNetwork.sendToPlayer(player, new SyncBossBarDataPacket(bossEvent.getId(), bossIdx, values));
    }

    default void syncBossBarData(ServerBossEvent bossEvent, int bossIdx, Map<String, Integer> values) {
        SyncBossBarDataPacket packet = new SyncBossBarDataPacket(bossEvent.getId(), bossIdx, values);
        for (ServerPlayer player : bossEvent.getPlayers()) {
            ACNetwork.sendToPlayer(player, packet);
        }
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
