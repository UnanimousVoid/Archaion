package com.ratrod.archaion.entities.ai.goals;

import com.ratrod.archaion.entities.DeepslateSentinel;
import com.ratrod.archaion.entities.Wight;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class PickUpRidersGoal extends Goal {
    private final DeepslateSentinel sentinel;
    private final double speedModifier;
    private final double searchRadius;
    private Entity target;
    private int pathRecalcTicks;

    public PickUpRidersGoal(DeepslateSentinel sentinel, double speedModifier, double searchRadius) {
        this.sentinel = sentinel;
        this.speedModifier = speedModifier;
        this.searchRadius = searchRadius;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.target = this.findPickupTarget();
        return this.target != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && this.target.isAlive() && !this.target.isPassenger()
                && this.sentinel.getPassengers().isEmpty();
    }

    @Override
    public void start() {
        this.pathRecalcTicks = 0;
    }

    @Override
    public void stop() {
        this.target = null;
        this.sentinel.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.target == null) {
            return;
        }
        if (--this.pathRecalcTicks <= 0) {
            this.pathRecalcTicks = 5;
            Vec3 targetPos = this.target.position();
            this.sentinel.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, this.speedModifier);
        }
        if (this.sentinel.distanceToSqr(this.target) < 6.0D) {
            this.target.startRiding(this.sentinel);
        }
    }

    private Entity findPickupTarget() {
        if (!this.sentinel.getPassengers().isEmpty()) {
            return null;
        }
        AABB box = this.sentinel.getBoundingBox().inflate(this.searchRadius);
        Predicate<Entity> free = e -> e.isAlive() && !e.isPassenger();
        List<Wight> wights = this.sentinel.level().getEntitiesOfClass(Wight.class, box, free).stream().filter(sentinel::hasLineOfSight).toList();
        return nearest(wights);
    }

    private <T extends Entity> T nearest(List<T> entities) {
        T nearest = null;
        double nearestDistSqr = Double.MAX_VALUE;
        for (T e : entities) {
            double distSqr = this.sentinel.distanceToSqr(e);
            if (distSqr < nearestDistSqr) {
                nearestDistSqr = distSqr;
                nearest = e;
            }
        }
        return nearest;
    }
}
