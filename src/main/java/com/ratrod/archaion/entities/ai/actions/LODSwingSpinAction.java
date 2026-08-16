package com.ratrod.archaion.entities.ai.actions;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.api.entity.ManagedAction;
import com.ratrod.archaion.entities.LastOfDeepslate;
import com.ratrod.archaion.entities.ai.ACEntity;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LODSwingSpinAction extends ManagedAction<LastOfDeepslate> {


    public LODSwingSpinAction(LastOfDeepslate entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (!entity.hasLineOfSight(target)) return false;
        return entity.distanceTo(target) <= entity.getBbWidth() * 1.25F;
    }

    @Override
    public void onStart() {
        this.timer = 0;
        entity.swingSpinAnim.start();
        entity.playSound(ACSounds.LOD_ACTION_START.get(), 3.0F, 1.0F);
    }

    @Override
    public void onStop() {
        entity.swingSpinAnim.stop();
    }

    @Override
    public boolean onTick() {
        timer++;

        if (timer == 10) {
            entity.playSound(ACSounds.LOD_SPIN.get(), 3.0F, 1.0F);
            ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("echo_spin"));
            AAALevel.addParticle(entity.level(), info.position(entity.position().add(0, 1, 0)).scale(2.0F));
        }

        if (timer == 15) {
            this.applySwingDamage();
        }

        return timer < 50;
    }

    private void applySwingDamage() {
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;

        Vec3 center = entity.position();
        AABB area = this.entity.getBoundingBox().inflate(2);

        List<LivingEntity> targets = entity.level().getEntitiesOfClass(LivingEntity.class, area, e -> e != entity && e.isAlive() && entity.canAttack(e));

        for (LivingEntity target : targets) {
            entity.attackTarget(serverLevel, target, 1.0F, ACEntity.Operation.MULTIPLY);
            Vec3 knockback = target.position().subtract(center).normalize().scale(1.5).add(0, 0.5, 0);
            target.setDeltaMovement(target.getDeltaMovement().add(knockback));
            target.hurtMarked = true;
        }
    }
}
