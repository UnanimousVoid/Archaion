package com.ratrod.archaion.entities.ai.actions;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.api.entity.ManagedAction;
import com.ratrod.archaion.entities.LastOfDeepslate;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LODRollAction extends ManagedAction<LastOfDeepslate> {

    private Vec3 rollDir = Vec3.ZERO;
    private Vec3 lastPos = Vec3.ZERO;
    private LivingEntity lockedTarget;
    private int stuckTicks;
    private int escapeSide = 1;
    private boolean huggingWall;

    public LODRollAction(LastOfDeepslate entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (!entity.hasLineOfSight(target)) return false;
        if (entity.getY() + entity.getBbHeight() < target.getY()) return false;
        return entity.getArchaicSystem().getPhasesTriggered() >= 1;
    }

    @Override
    public void onStart() {
        this.timer = 0;
        this.rollDir = directionFromYaw(entity.getYRot());
        this.lastPos = entity.position();
        this.stuckTicks = 0;
        this.escapeSide = 1;
        this.huggingWall = false;
        this.lockedTarget = entity.getTarget();
        entity.rollingAnim.start();
        entity.playSound(ACSounds.LOD_ACTION_START.get(), 3.0F, 1.0F);
    }

    @Override
    public boolean onTick() {
        timer++;

        boolean charging = timer > 32 && timer < 120;

        if (lockedTarget == null || !lockedTarget.isAlive()) {
            return false;
        }

        if (lockedTarget != null && lockedTarget.isAlive()) {
            if (charging && isStuck()) {
                this.rollDir = directionFromYaw(yawOf(this.rollDir) + escapeSide * 30.0F);
            } else {
                steerHeading(lockedTarget);
            }

            if (charging) {
                this.rollDir = avoidWalls(this.rollDir);
                if (timer % 4 == 0) {
                    applyBoom();
                }
                if (timer % 2 == 0) {
                    entity.playSound(ACSounds.ECHO_STAR_BLAST.get(), 2.0F, 0.6F + this.entity.level().getRandom().nextFloat() * 0.2F);
                }
            }

            applyRotation();
        }

        double rollVy = entity.getDeltaMovement().y;
        if (charging) {
            entity.setDeltaMovement(this.rollDir.scale(rollSpeed()).add(0.0, rollVy, 0.0));
        } else {
            entity.setDeltaMovement(0.0, rollVy, 0.0);
        }

        return timer < 140;
    }

    @Override
    public void onStop() {
        entity.rollingAnim.stop();
        lockedTarget = null;
    }

    private void applyBoom() {
        ServerLevel level = (ServerLevel) entity.level();

        RandomSource random = level.getRandom();
        ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("echo_blast_intercept"));
        AAALevel.addParticle(level, info.position(entity.position().offsetRandom(random, 0.5F)).rotation(0, random.nextFloat() * 90, 0).scale(2.0F));

        double bbWidth = entity.getBbWidth();
        AABB area = AABB.ofSize(entity.position(), bbWidth, 3, bbWidth);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area, e -> e != entity && e.isAlive() && entity.canAttack(e));
        for (LivingEntity target : targets) {
            if (target.hurtServer(level, level.damageSources().explosion(entity, entity), 20.0F)) {
                Vec3 knockback = target.position().subtract(entity.position()).normalize().scale(3.0).add(0, 0.35, 0);
                target.setDeltaMovement(target.getDeltaMovement().add(knockback));
                target.hurtMarked = true;
            }
        }
    }

    private void steerHeading(LivingEntity target) {
        float headingSmoothing = 0.4F;
        float maxTurnRate = 1.75F;
        Vec3 desired = target.position().subtract(entity.position()).with(Direction.Axis.Y, 0.0);
        double length = desired.length();
        if (length < 1.0E-4) return;
        desired = desired.normalize();

        float currentYaw = yawOf(this.rollDir);
        float easedYaw = Mth.rotLerp(headingSmoothing, currentYaw, yawOf(desired));
        float turn = Mth.clamp(Mth.degreesDifference(currentYaw, easedYaw), -maxTurnRate, maxTurnRate);
        this.rollDir = directionFromYaw(currentYaw + turn);
    }

    private Vec3 avoidWalls(Vec3 dir) {
        float wallTurnRate = 22.0F;
        float wallProbeMargin = 1.5F;
        huggingWall = false;
        Vec3 from = entity.position().add(0, entity.getBbHeight() * 0.5, 0);
        double probe = entity.getBbWidth() * 0.5 + wallProbeMargin;
        BlockHitResult hit = entity.level().clip(new ClipContext(from, from.add(dir.scale(probe)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));

        if (hit.getType() == HitResult.Type.BLOCK && hit.getDirection().getAxis().isHorizontal()) {
            Vec3 normal = hit.getDirection().getUnitVec3();
            Vec3 tangent = dir.subtract(normal.scale(dir.dot(normal)));
            if (tangent.lengthSqr() < 1.0E-6) {
                tangent = new Vec3(-normal.z, 0, normal.x);
            } else {
                tangent = tangent.normalize();
            }
            huggingWall = true;
            float currentYaw = yawOf(dir);
            float slide = Mth.clamp(Mth.degreesDifference(currentYaw, yawOf(tangent)), -wallTurnRate, wallTurnRate);
            return directionFromYaw(currentYaw + slide);
        }
        return dir;
    }

    private boolean isStuck() {
        double dx = entity.getX() - this.lastPos.x;
        double dz = entity.getZ() - this.lastPos.z;
        this.lastPos = entity.position();

        if (timer > 40 && timer <= 115 && dx * dx + dz * dz < 0.0009) {
            stuckTicks++;
            if (stuckTicks % 8 == 0) escapeSide = -escapeSide;
            return true;
        }
        stuckTicks = 0;
        return false;
    }

    private void applyRotation() {
        float headingYaw = yawOf(this.rollDir);
        entity.setYRot(headingYaw);
        entity.yBodyRot = headingYaw;
        entity.setXRot(Mth.rotLerp(0.3F, entity.getXRot(), 0.0F));
    }

    private double rollSpeed() {
        float maxSpeed = 1.2F;
        float ramp = Mth.clamp((timer - 30) / 8.0F, 0.0F, 1.0F);
        float fade = timer > 115 ? (140 - timer) / 25.0F : 1.0F;
        float speed = maxSpeed * ramp * Mth.clamp(fade, 0.0F, 1.0F);
        if (huggingWall) speed *= 0.8F;
        return speed;
    }

    private static float yawOf(Vec3 dir) {
        return (float) (Mth.atan2(-dir.x, dir.z) * Mth.RAD_TO_DEG);
    }

    private static Vec3 directionFromYaw(float yaw) {
        float rad = yaw * Mth.DEG_TO_RAD;
        return new Vec3(-Mth.sin(rad), 0.0, Mth.cos(rad));
    }
}