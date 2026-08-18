package com.ratrod.archaion.entities.ai.actions;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.api.entity.ManagedAction;
import com.ratrod.archaion.entities.LastOfDeepslate;
import com.ratrod.archaion.entities.ai.ACEntity;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LODSmashGroundAction extends ManagedAction<LastOfDeepslate> {

    public LODSmashGroundAction(LastOfDeepslate entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (!entity.hasLineOfSight(target)) return false;
        if (entity.getY() + 3 < target.getY()) return false;
        return entity.distanceTo(target) <= entity.getBbWidth() * 1.5F;
    }

    @Override
    public void onStart() {
        this.timer = 0;
        entity.smashGroundAnim.start();
        entity.playSound(ACSounds.LOD_ACTION_START.get(), 3.0F, 1.0F);
    }

    @Override
    public boolean onTick() {
        timer++;

        if (timer == 27) {
            this.applySmashDamage();
        }

        if (timer > 18 && entity.getTarget() != null) {
            this.entity.getLookControl().setLookAt(entity.getTarget(), 30.0F, 30.0F);
        }

        return timer < 50;
    }

    private void applySmashDamage() {
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;

        entity.playSound(ACSounds.LOD_SMASH.get(), 3.0F, 1.0F);

        float yaw = entity.getYHeadRot() * Mth.DEG_TO_RAD;
        Vec3 flatLook = new Vec3(-Mth.sin(yaw), 0, Mth.cos(yaw));
        Vec3 center = entity.position().add(flatLook.yRot(7.5F * Mth.DEG_TO_RAD).scale(7.5));

        ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_boom_ground"));
        AAALevel.addParticle(serverLevel, info.position(center.add(0, 0.2, 0)).scale(3.0F));

        AABB area = AABB.ofSize(center, 12, 6, 12);

        List<LivingEntity> targets = entity.level().getEntitiesOfClass(LivingEntity.class, area, e -> e != entity && e.isAlive() && entity.canAttack(e));

        for (LivingEntity target : targets) {
            entity.attackTarget(serverLevel, target, 1.1F, ACEntity.Operation.MULTIPLY);
            Vec3 knockback = target.position().subtract(center).normalize().scale(1.5).add(0, 0.35, 0);
            target.setDeltaMovement(target.getDeltaMovement().add(knockback));
            target.hurtMarked = true;
        }
    }

    @Override
    public void onStop() {
        entity.smashGroundAnim.stop();
    }
}
