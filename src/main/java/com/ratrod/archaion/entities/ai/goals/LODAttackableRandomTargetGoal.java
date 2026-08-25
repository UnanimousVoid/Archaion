package com.ratrod.archaion.entities.ai.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class LODAttackableRandomTargetGoal extends TargetGoal {

    private int cycleDelay;

    public LODAttackableRandomTargetGoal(Mob mob) {
        super(mob, true);
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return true;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.cycleDelay = 100 + mob.getRandom().nextInt(40);
    }

    @Override
    public void tick() {
        if (mob.level().isClientSide()) return;

        LivingEntity current = mob.getTarget();
        if (current != null && !this.isValidTarget(current)) {
            this.mob.setTarget(null);
        }

        if (mob.getTarget() == null || cycleDelay-- <= 0) {
            this.setTarget();
            cycleDelay = 100 + mob.getRandom().nextInt(40);
        }
    }

    private boolean isValidTarget(LivingEntity target) {
        if (!target.isAlive() || target.isSpectator()) {
            return false;
        }
        return !(target instanceof Player player) || !player.isCreative();
    }

    private void setTarget() {
        List<LivingEntity> candidates = this.nearbyVisibleTargets();
        if (candidates.isEmpty()) {
            this.mob.setTarget(null);
        } else {
            this.mob.setTarget(candidates.get(this.mob.getRandom().nextInt(candidates.size())));
        }
    }

    private List<LivingEntity> nearbyVisibleTargets() {
        List<LivingEntity> result = new ArrayList<>();
        AABB range = this.mob.getBoundingBox().inflate(64.0);
        Predicate<LivingEntity> predicate = this::isValidTarget;
        result.addAll(this.mob.level().getEntitiesOfClass(Player.class, range, predicate));
        result.addAll(this.mob.level().getEntitiesOfClass(IronGolem.class, range, predicate));
        return result;
    }
}
