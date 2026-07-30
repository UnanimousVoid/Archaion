package com.ratrod.archaion.entities.ai.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class GoToTargetGoal extends Goal {
    protected final Mob entity;
    protected final float speedModifier;
    protected int pathRecalcTicks;
    protected float distToTarget;

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

        if (--this.pathRecalcTicks <= 0) {
            this.pathRecalcTicks = 8;
            Vec3 targetPos = target.position();
            this.entity.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, this.speedModifier);
        }
    }

    @Override
    public void stop() {
        this.entity.getNavigation().stop();
    }
}
