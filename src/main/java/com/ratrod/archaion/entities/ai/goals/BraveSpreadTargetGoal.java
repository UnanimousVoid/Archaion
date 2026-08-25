package com.ratrod.archaion.entities.ai.goals;

import com.ratrod.archaion.entities.Brave;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;

import java.util.Comparator;
import java.util.List;

public class BraveSpreadTargetGoal extends TargetGoal {

    private final Brave brave;
    private int recheckDelay;

    public BraveSpreadTargetGoal(Brave brave) {
        super(brave, false);
        this.brave = brave;
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
    public void start() {
        this.recheckDelay = 0;
    }

    @Override
    public void tick() {
        if (this.brave.level().isClientSide()) return;

        LivingEntity current = this.mob.getTarget();
        if (current != null && !this.isValidTarget(current)) {
            this.mob.setTarget(null);
        }

        if (--this.recheckDelay > 0) return;
        this.recheckDelay = 10;

        if (this.brave.isCharged() && this.brave.getOwnerUUID() != null) {
            this.spreadToOwnSlot();
        } else {
            Player nearest = this.nearestPlayer();
            if (nearest != null) {
                this.mob.setTarget(nearest);
            }
        }
    }

    private boolean isValidTarget(LivingEntity target) {
        return target.isAlive()
                && !target.isSpectator()
                && !target.isInvulnerable()
                && (!(target instanceof Player player) || !player.isCreative())
                && this.brave.canAttack(target);
    }

    private void spreadToOwnSlot() {
        LivingEntity owner = this.brave.getOwner();
        if (owner == null || !owner.isAlive()) return;

        List<Player> players = this.brave.level().getEntitiesOfClass(Player.class, this.brave.getBoundingBox().inflate(48.0), EntitySelector.NO_CREATIVE_OR_SPECTATOR);
        players.removeIf(p -> !p.isAlive() || !this.brave.canAttack(p));
        if (players.isEmpty()) return;

        List<Brave> owned = this.brave.level().getEntitiesOfClass(Brave.class, owner.getBoundingBox().inflate(512.0), b -> b.isAlive() && b.isCharged() && owner.getUUID().equals(b.getOwnerUUID()));
        owned.sort(Comparator.comparingInt(Entity::getId));
        int slot = owned.indexOf(this.brave);
        if (slot < 0) return;
        this.mob.setTarget(players.get(slot % players.size()));
    }

    private Player nearestPlayer() {
        List<Player> players = this.brave.level().getEntitiesOfClass(Player.class, this.brave.getBoundingBox().inflate(48.0), EntitySelector.NO_CREATIVE_OR_SPECTATOR);
        Player nearest = null;
        double best = Double.MAX_VALUE;
        for (Player player : players) {
            if (!player.isAlive() || !this.brave.canAttack(player)) continue;
            double dist = this.brave.distanceToSqr(player);
            if (dist < best) {
                best = dist;
                nearest = player;
            }
        }
        return nearest;
    }
}
