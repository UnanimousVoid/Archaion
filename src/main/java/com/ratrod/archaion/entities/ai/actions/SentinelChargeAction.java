package com.ratrod.archaion.entities.ai.actions;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.api.entity.ManagedAction;
import com.ratrod.archaion.entities.DeepslateSentinelEntity;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SentinelChargeAction extends ManagedAction<DeepslateSentinelEntity> {

    private Vec3 chargeDir = Vec3.ZERO;
    private Vec3 lastPos = Vec3.ZERO;
    private int stuckTicks;

    public SentinelChargeAction(DeepslateSentinelEntity entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        if (entity.isChargeOnCooldown()) return false;
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (!entity.hasLineOfSight(target)) return false;
        if (!entity.getPassengers().isEmpty()) return false;
        return !entity.hasNearbyRidersToPickup(32.0D);
    }

    @Override
    public void onStart() {
        this.timer = 0;
        this.stuckTicks = 0;
        this.lastPos = entity.position();

        LivingEntity target = entity.getTarget();
        Vec3 desired = target != null ? target.position().subtract(entity.position()) : directionFromYaw(entity.getYRot());
        desired = desired.with(Direction.Axis.Y, 0.0);
        this.chargeDir = desired.lengthSqr() < 1.0E-6 ? directionFromYaw(entity.getYRot()) : desired.normalize();

        entity.chargeAnim.start();
    }

    @Override
    public boolean onTick() {
        timer++;

        boolean windingUp = timer <= 30;
        boolean charging = timer > 30 && timer <= 120;

        float yaw = yawOf(this.chargeDir);
        entity.setYRot(yaw);
        entity.yBodyRot = yaw;

        LivingEntity target = entity.getTarget();
        if (windingUp && target != null && target.isAlive()) {
            entity.getLookControl().setLookAt(target, 45.0F, 45.0F);
        }

        if (charging) {
            if (isStuck()) {
                return false;
            }
            entity.setDeltaMovement(this.chargeDir.scale(chargeSpeed()).add(0.0, entity.getDeltaMovement().y, 0.0));

            if (timer % 4 == 0) {
                applyBoom();
            }
            if (timer % 2 == 0) {
                entity.playSound(ACSounds.ECHO_STAR_BLAST.get(), 2.0F, 1.4F + this.entity.level().getRandom().nextFloat() * 0.2F);
            }

        } else {
            entity.setDeltaMovement(0.0, entity.getDeltaMovement().y, 0.0);
        }

        return timer < 140;
    }

    private void applyBoom() {
        ServerLevel level = (ServerLevel) entity.level();

        RandomSource random = level.getRandom();
        ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("echo_blast_intercept"));
        AAALevel.addParticle(level, info.position(entity.position().offsetRandom(random, 0.5F)).rotation(0, random.nextFloat() * 90, 0).scale(1.4F));

        double bbWidth = entity.getBbWidth() + 1;
        AABB area = AABB.ofSize(entity.position(), bbWidth, 3, bbWidth);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area, e -> e != entity && e.isAlive() && entity.canAttack(e));
        for (LivingEntity target : targets) {
            if (target.hurtServer(level, level.damageSources().explosion(entity, entity), 15.0F)) {
                Vec3 knockback = target.position().subtract(entity.position()).normalize().scale(3.0).add(0, 0.4, 0);
                target.setDeltaMovement(target.getDeltaMovement().add(knockback));
                target.hurtMarked = true;
            }
        }
    }

    @Override
    public void onStop() {
        entity.chargeAnim.stop();
        entity.setDeltaMovement(0.0, entity.getDeltaMovement().y, 0.0);
        entity.startChargeCooldown();
    }

    private double chargeSpeed() {
        float ramp = Mth.clamp((timer - 30) / 6.0F, 0.0F, 1.0F);
        float fade = timer > 115 ? (120 - timer) / 5.0F : 1.0F;
        return 1.1F * ramp * Mth.clamp(fade, 0.0F, 1.0F);
    }

    private boolean isStuck() {
        double dx = entity.getX() - this.lastPos.x;
        double dz = entity.getZ() - this.lastPos.z;
        this.lastPos = entity.position();
        if (dx * dx + dz * dz < 0.0005) {
            return ++stuckTicks > 8;
        }
        stuckTicks = 0;
        return false;
    }

    private static float yawOf(Vec3 dir) {
        return (float) (Mth.atan2(-dir.x, dir.z) * Mth.RAD_TO_DEG);
    }

    private static Vec3 directionFromYaw(float yaw) {
        float rad = yaw * Mth.DEG_TO_RAD;
        return new Vec3(-Mth.sin(rad), 0.0, Mth.cos(rad));
    }
}
