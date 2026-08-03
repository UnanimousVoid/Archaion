package com.ratrod.archaion.entities.ai.actions;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.api.entity.ManagedAction;
import com.ratrod.archaion.entities.EchoStarProjectile;
import com.ratrod.archaion.entities.LastOfDeepslateEntity;
import com.ratrod.archaion.registry.ACEntityTypes;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class LODShootAction extends ManagedAction<LastOfDeepslateEntity> {

    public LODShootAction(LastOfDeepslateEntity entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (!entity.hasLineOfSight(target)) return false;
        return entity.distanceTo(target) >= entity.getBbWidth() * 1.75F;
    }

    @Override
    public void onStart() {
        this.timer = 0;
        entity.shootAnim.start();
        entity.playSound(ACSounds.LOD_ACTION_START.get(), 3.0F, 1.0F);
    }

    @Override
    public boolean onTick() {
        timer++;

        if (timer == 22) {
            entity.playSound(ACSounds.LOD_SHOOT.get(), 5.0F, 1.0F);

            float yaw = entity.getYHeadRot() * Mth.DEG_TO_RAD;
            Vec3 flatLook = new Vec3(-Mth.sin(yaw), 0, Mth.cos(yaw));
            Vec3 center = entity.position().add(flatLook.yRot(-25F * Mth.DEG_TO_RAD).scale(8).add(0, 5.5, 0));

            ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("echo_blast"));
            AAALevel.addParticle(entity.level(), info.position(center).scale(6.0F));

            for (int i = 0; i < 10; i++) {
                EchoStarProjectile projectile = ACEntityTypes.ECHO_STAR.get().create(entity.level(), EntitySpawnReason.TRIGGERED);
                projectile.moveOrInterpolateTo(center);
                projectile.setOwner(entity);
                projectile.setHomingTarget(entity.getTarget());
                float xR = -15 + (-1 + entity.getRandom().nextFloat() * 2) * 45;
                float yR = (-1 + entity.getRandom().nextFloat() * 2) * 45;
                projectile.shootFromRotation(entity, entity.getXRot() + xR, entity.getYRot() + yR, 0, 3F, 0.0F);
                entity.level().addFreshEntity(projectile);
            }
        }

        if (timer > 25) {
            if (entity.getTarget() != null) entity.getLookControl().setLookAt(entity.getTarget(), 45, 45);
        }

        return timer < 60;
    }

    @Override
    public void onStop() {
        entity.shootAnim.stop();
    }
}
