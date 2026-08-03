package com.ratrod.archaion.entities.ai.goals;

import com.ratrod.archaion.entities.BraveEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class BraveDistanceAwayGoal extends Goal {
    private final BraveEntity entity;
    private final double speedModifier;
    private final double desiredDistance;
    private int pathRecalcTicks;

    public BraveDistanceAwayGoal(BraveEntity entity, double speedModifier, double desiredDistance) {
        this.entity = entity;
        this.speedModifier = speedModifier;
        this.desiredDistance = desiredDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.getTarget();
        if (entity.attackManager.isBusy()) return false;
        return target != null && target.isAlive() && this.entity.mustRetreat(target.position());
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.entity.getTarget();
        if (entity.attackManager.isBusy()) return false;
        return target != null && target.isAlive() && this.entity.distanceToSqr(target) < this.desiredDistance * this.desiredDistance;
    }

    @Override
    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (target == null) {
            return;
        }

        if (--this.pathRecalcTicks <= 0) {
            this.pathRecalcTicks = 8 + entity.getRandom().nextInt(5);

            double dx = this.entity.getX() - target.getX();
            double dz = this.entity.getZ() - target.getZ();
            double distanceSqr = dx * dx + dz * dz;
            if (distanceSqr < 1.0E-4) {
                return;
            }

            double distance = Math.sqrt(distanceSqr);
            Vec3 destination = new Vec3(
                    target.getX() + dx / distance * this.desiredDistance,
                    this.entity.getY(),
                    target.getZ() + dz / distance * this.desiredDistance
            );
            this.entity.getNavigation().moveTo(destination.x, destination.y, destination.z, this.speedModifier);
        }
    }

    @Override
    public void stop() {
        this.entity.getNavigation().stop();
    }
}