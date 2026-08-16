package com.ratrod.archaion.entities.ai.goals;

import com.ratrod.archaion.entities.Grimoray;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class GrimorayFlightGoal extends Goal {
    private final Grimoray grimoray;
    private final double speed;
    private final double hoverHeight;
    private final double strafeRadius;
    private final double separation = 3.0;
    private double angle;

    public GrimorayFlightGoal(Grimoray grimoray, double speed, double hoverHeight, double strafeRadius) {
        this.grimoray = grimoray;
        this.speed = speed;
        this.hoverHeight = hoverHeight;
        this.strafeRadius = strafeRadius;
        this.angle = grimoray.getRandom().nextFloat() * (float) (Math.PI * 2.0);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return grimoray.getTarget() != null && grimoray.getTarget().isAlive();
    }

    @Override
    public void tick() {
        LivingEntity target = grimoray.getTarget();
        if (target == null) return;

        this.angle += 0.05;
        double x = target.getX() + Math.cos(this.angle) * strafeRadius;
        double z = target.getZ() + Math.sin(this.angle) * strafeRadius;
        double y = target.getY() + hoverHeight;

        Vec3 to = new Vec3(x, y, z).subtract(grimoray.position());
        double dist = to.length();

        Vec3 vel = Vec3.ZERO;
        if (dist > 1.0E-4) {
            vel = to.scale(Math.min(this.speed, dist) / dist);
        }

        List<Grimoray> others = grimoray.level().getEntitiesOfClass(Grimoray.class, grimoray.getBoundingBox().inflate(this.separation), e -> e != grimoray);
        for (Grimoray other : others) {
            Vec3 away = grimoray.position().subtract(other.position());
            double d = away.length();
            if (d > 1.0E-4 && d < this.separation) {
                vel = vel.add(away.normalize().scale((this.separation - d) / this.separation));
            }
        }

        grimoray.setDeltaMovement(vel);
    }
}
