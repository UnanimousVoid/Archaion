package com.ratrod.archaion.entities.ai.goals;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class OrbitTargetGoal extends Goal {
    protected final Mob entity;
    protected final float speedModifier;
    protected final float orbitRadius;
    protected final float heightAboveTarget;
    protected final float orbitSpeed;
    protected int pathRecalcTicks;

    public OrbitTargetGoal(Mob entity, float speedModifier, float orbitRadius, float heightAboveTarget, float orbitSpeed) {
        this.entity = entity;
        this.speedModifier = speedModifier;
        this.orbitRadius = orbitRadius;
        this.heightAboveTarget = heightAboveTarget;
        this.orbitSpeed = orbitSpeed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public OrbitTargetGoal(Mob entity, float speedModifier) {
        this(entity, speedModifier, 10.0F, 14.0F, 0.03F);
    }

    @Override
    public boolean canUse() {
        return this.entity.getTarget() != null && this.entity.getTarget().isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (target == null) return;

        if (--this.pathRecalcTicks <= 0) {
            this.pathRecalcTicks = 10 + this.entity.getRandom().nextInt(10);
            double range = this.orbitRadius - this.entity.getRandom().nextInt((int) (this.orbitRadius * 0.4F));
            float phase = this.entity.tickCount * this.orbitSpeed;
            double xR = Mth.sin(phase) * range;
            double yR = this.heightAboveTarget;
            double zR = Mth.cos(phase) * range;
            Vec3 targetPos = target.position().add(xR, yR, zR);
            this.entity.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, this.speedModifier);
        }
    }
}
