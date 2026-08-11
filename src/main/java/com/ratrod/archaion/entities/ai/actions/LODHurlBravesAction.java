package com.ratrod.archaion.entities.ai.actions;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.api.entity.ManagedAction;
import com.ratrod.archaion.entities.BraveSpawnProjectile;
import com.ratrod.archaion.entities.LastOfDeepslateEntity;
import com.ratrod.archaion.registry.ACEntityTypes;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.phys.Vec3;

public class LODHurlBravesAction extends ManagedAction<LastOfDeepslateEntity> {

    public LODHurlBravesAction(LastOfDeepslateEntity entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        if (entity.isHurlBravesTriggered()) return false;
        if (entity.getHealth() > entity.getMaxHealth() * 0.5F) return false;

        return true;
    }

    @Override
    public void onStart() {
        this.timer = 0;
        entity.setHurlBravesTriggered(true);
        entity.hurlBravesAnim.forceStart();
        entity.playSound(ACSounds.LOD_ACTION_START.get(), 3.0F, 1.0F);
    }

    @Override
    public boolean onTick() {
        timer++;

        if (timer == 60) {
            this.hurlBraves();
        }

        return timer <= 80;
    }

    private void hurlBraves() {
        if (entity.level().isClientSide()) return;
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;

        float yaw = entity.getYHeadRot() * Mth.DEG_TO_RAD;
        Vec3 flatLook = new Vec3(-Mth.sin(yaw), 0, Mth.cos(yaw));
        Vec3 center = entity.position().add(flatLook.yRot(-25F * Mth.DEG_TO_RAD).scale(6).add(0, 5.5, 0));

        entity.playSound(ACSounds.LOD_SHOOT.get(), 5.0F, 0.8F);

        ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("echo_blast"));
        AAALevel.addParticle(entity.level(), info.position(center).scale(6.0F));

        int players = entity.countNearbyPlayers();
        int braveCount = Math.max(3, 3 + 2 * (players - 1));
        entity.setHurlBravesTarget(braveCount);

        for (int i = 0; i < braveCount; i++) {
            BraveSpawnProjectile projectile = ACEntityTypes.BRAVE_SPAWN_PROJECTILE.get().create(serverLevel, EntitySpawnReason.TRIGGERED);
            projectile.moveOrInterpolateTo(center);
            projectile.setOwner(entity);
            projectile.setNoGravity(false);
            float xR = -10 + (-1 + entity.getRandom().nextFloat() * 2) * 15;
            float yR = (-1 + entity.getRandom().nextFloat() * 2) * 25;
            projectile.shootFromRotation(entity, entity.getXRot() + xR, entity.getYRot() + yR, 0, 2.0F, 0.0F);
            serverLevel.addFreshEntity(projectile);
        }
    }

    @Override
    public void onStop() {
        entity.hurlBravesAnim.stop();
    }
}
