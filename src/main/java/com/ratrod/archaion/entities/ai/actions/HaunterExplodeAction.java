package com.ratrod.archaion.entities.ai.actions;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.api.entity.ManagedAction;
import com.ratrod.archaion.entities.Haunter;
import com.ratrod.archaion.registry.ACEffects;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class HaunterExplodeAction extends ManagedAction<Haunter> {


    public HaunterExplodeAction(Haunter entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (entity.explodingCooldown > 0) return false;
        float range = 3.5F;
        return entity.distanceToSqr(target) <= range * range;
    }

    @Override
    public void onStart() {
        this.timer = 0;
        entity.setSwelling(true);
        entity.explodingAnim.start();
        entity.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.2F);
    }

    @Override
    public boolean onTick() {
        timer++;

        entity.getNavigation().stop();

        if (timer == 40) {
            entity.setSwelling(false);
            if (entity.level() instanceof ServerLevel serverLevel) {

                entity.playSound(ACSounds.HAUNTER_EXPLODE.get(), 3.0F, 1.0F);

                ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("haunter_boom"));
                AAALevel.addParticle(serverLevel, info.position(entity.position().add(0, 1, 0)).scale(1.5F));

                AABB area = entity.getBoundingBox().inflate(3, 0, 3);
                List<LivingEntity> targets = entity.level().getEntitiesOfClass(LivingEntity.class, area, e -> e != entity && e.isAlive() && entity.canAttack(e));

                for (LivingEntity target : targets) {
                    if (target.hurt(serverLevel.damageSources().explosion(entity, entity), 15)) {
                        target.addEffect(new MobEffectInstance(ACEffects.ARMOR_BREAK, 200, 1));
                        Vec3 knockback = target.position().subtract(entity.position()).normalize().multiply(2, 0, 2).add(0, 0.35, 0);
                        target.setDeltaMovement(target.getDeltaMovement().add(knockback));
                        target.hurtMarked = true;
                    }
                }
            }
        }

        return timer <= 60;
    }

    @Override
    public void onStop() {
        entity.setSwelling(false);
        entity.explodingCooldown = entity.getRandom().nextIntBetweenInclusive(100, 160);
        entity.explodingAnim.stop();
    }
}