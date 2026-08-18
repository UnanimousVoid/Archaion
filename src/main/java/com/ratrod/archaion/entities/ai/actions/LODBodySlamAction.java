package com.ratrod.archaion.entities.ai.actions;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.api.entity.ManagedAction;
import com.ratrod.archaion.entities.LastOfDeepslate;
import com.ratrod.archaion.entities.ai.ACEntity;
import com.ratrod.archaion.entities.projectile.LODSlamEffect;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LODBodySlamAction extends ManagedAction<LastOfDeepslate> {

    public LODBodySlamAction(LastOfDeepslate entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (!entity.hasLineOfSight(target)) return false;
        if (entity.getY() + entity.getBbHeight() < target.getY()) return false;
        float r = 1.5F;
        if (entity.getArchaicSystem().getPhasesTriggered() >= 1) {
            r = 3.5F;
        }
        return entity.distanceTo(target) <= entity.getBbWidth() * r;
    }

    @Override
    public void onStart() {
        this.timer = 0;
        entity.bodySlamAnim.start();
        entity.playSound(ACSounds.LOD_ACTION_START.get(), 3.0F, 1.0F);
    }

    @Override
    public boolean onTick() {
        timer++;
        if (timer == 30) {
            this.applyBodySlam();
        }
        return timer < 50;
    }

    private void applyBodySlam() {
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;

        entity.playSound(ACSounds.LOD_SMASH.get(), 4.0F, 0.8F);

        ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_boom_ground"));
        AAALevel.addParticle(serverLevel, info.position(entity.position().add(0, 0.5, 0)).scale(4.0F));

        if (entity.getArchaicSystem().getPhasesTriggered() >= 1) {
            LODSlamEffect.summonRing(serverLevel, entity.position(), entity);
        }

        AABB area = AABB.ofSize(entity.position(), 16, 10, 16);

        List<LivingEntity> targets = entity.level().getEntitiesOfClass(LivingEntity.class, area, e -> e != entity && e.isAlive() && entity.canAttack(e));

        for (LivingEntity target : targets) {
            entity.attackTarget(serverLevel, target, 1.2F, ACEntity.Operation.MULTIPLY);
            Vec3 knockback = target.position().subtract(entity.position()).normalize().scale(2.0).add(0, 0.8, 0);
            target.setDeltaMovement(target.getDeltaMovement().add(knockback));
            target.hurtMarked = true;
        }
    }

    @Override
    public void onStop() {
        entity.bodySlamAnim.stop();
    }
}
