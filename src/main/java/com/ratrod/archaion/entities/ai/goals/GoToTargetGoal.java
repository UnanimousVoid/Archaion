package com.ratrod.archaion.entities.ai.goals;

import com.ratrod.archaion.api.entity.ActionManager;
import com.ratrod.archaion.entities.ai.ACEntity;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class GoToTargetGoal extends Goal {
    protected final Mob entity;
    protected final float speedModifier;
    protected int pathRecalcTicks;
    protected float distToTarget;

    // Wall-slide fallback state (same pattern as LODRollAction)
    private Vec3 fallbackDir = Vec3.ZERO;
    private Vec3 lastPos = Vec3.ZERO;
    private boolean inFallback;
    private int stuckTicks;
    private int escapeSide = 1;

    public GoToTargetGoal(Mob entity, float speedModifier, float distToTarget) {
        this.entity = entity;
        this.speedModifier = speedModifier;
        this.distToTarget = distToTarget;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.entity.getTarget() != null && this.entity.getTarget().isAlive() && this.entity.getTarget().distanceTo(entity) > distToTarget;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (target == null) return;
        boolean far = this.entity.distanceTo(target) > this.distToTarget;
        boolean stalled = !hasHorizontalProgress();

        if (--this.pathRecalcTicks <= 0) {
            this.pathRecalcTicks = 8;
            boolean navOk = this.entity.getNavigation().moveTo(target.position().x, target.position().y, target.position().z, this.speedModifier);
            if (this.inFallback) {
                if (navOk && !stalled) this.inFallback = false;
            } else if (!navOk) {
                this.inFallback = true;
                this.fallbackDir = headingTo(target);
            }
        }

        if (!this.inFallback && far && this.entity.getNavigation().isInProgress() && stalled) {
            this.inFallback = true;
            this.fallbackDir = headingTo(target);
            this.entity.getNavigation().stop();
        }

        if (this.inFallback) applyFallbackSteering(target, stalled);
    }

    private boolean isBusyWithAction() {
        return this.entity instanceof ACEntity<?> ac && ac.getActionManagers().stream().anyMatch(ActionManager::isBusy);
    }

    private void applyFallbackSteering(LivingEntity target, boolean stalled) {
        if (isBusyWithAction()) return;

        if (stalled) {
            stuckTicks++;
            if (stuckTicks % 8 == 0) escapeSide = -escapeSide;
            this.fallbackDir = directionFromYaw(yawOf(this.fallbackDir) + this.escapeSide * 30.0F);
        } else {
            stuckTicks = 0;
            Vec3 toTarget = headingTo(target);
            if (toTarget.lengthSqr() > 0) {
                float yaw = yawOf(this.fallbackDir);
                float eased = Mth.rotLerp(0.4F, yaw, yawOf(toTarget));
                float turn = Mth.clamp(Mth.degreesDifference(yaw, eased), -1.75F, 1.75F);
                this.fallbackDir = directionFromYaw(yaw + turn);
            }
        }

        this.fallbackDir = avoidWalls(this.fallbackDir);

        double vy = entity.getDeltaMovement().y;
        entity.setDeltaMovement(this.fallbackDir.scale(walkSpeed()).add(0.0, vy, 0.0));

        float yaw = yawOf(this.fallbackDir);
        entity.setYRot(yaw);
        entity.yBodyRot = yaw;
    }

    private boolean hasHorizontalProgress() {
        double dx = entity.getX() - this.lastPos.x;
        double dz = entity.getZ() - this.lastPos.z;
        this.lastPos = entity.position();
        return dx * dx + dz * dz >= 0.0009;
    }

    private double walkSpeed() {
        return this.entity.getAttributeValue(Attributes.MOVEMENT_SPEED) * this.speedModifier;
    }

    private Vec3 avoidWalls(Vec3 dir) {
        Vec3 from = entity.position().add(0, entity.getBbHeight() * 0.5, 0);
        double probe = entity.getBbWidth() * 0.5 + 1.5F;
        BlockHitResult hit = entity.level().clip(new ClipContext(from, from.add(dir.scale(probe)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
        if (hit.getType() == HitResult.Type.BLOCK && hit.getDirection().getAxis().isHorizontal()) {
            Vec3 normal = Vec3.atLowerCornerOf(hit.getDirection().getNormal());            Vec3 tangent = dir.subtract(normal.scale(dir.dot(normal)));
            if (tangent.lengthSqr() < 1.0E-6) {
                tangent = new Vec3(-normal.z, 0, normal.x);
            } else {
                tangent = tangent.normalize();
            }
            return directionFromYaw(yawOf(dir) + Mth.clamp(Mth.degreesDifference(yawOf(dir), yawOf(tangent)), -22.0F, 22.0F));
        }
        return dir;
    }

    private Vec3 headingTo(LivingEntity target) {
        Vec3 dir = target.position().subtract(entity.position()).with(Direction.Axis.Y, 0.0);
        double len = dir.length();
        return len < 1.0E-4 ? Vec3.ZERO : dir.normalize();
    }

    private static float yawOf(Vec3 dir) {
        return (float) (Mth.atan2(-dir.x, dir.z) * Mth.RAD_TO_DEG);
    }

    private static Vec3 directionFromYaw(float yaw) {
        float rad = yaw * Mth.DEG_TO_RAD;
        return new Vec3(-Mth.sin(rad), 0.0, Mth.cos(rad));
    }

    @Override
    public void stop() {
        this.entity.getNavigation().stop();
        this.inFallback = false;
    }
}
