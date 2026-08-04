package com.ratrod.archaion.entities.ai.actions;

import com.ratrod.archaion.api.entity.ManagedAction;
import com.ratrod.archaion.entities.BraveEntity;
import com.ratrod.archaion.entities.ai.ACEntity;
import com.ratrod.archaion.registry.ACSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.LongJumpUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BraveJumpOnAction extends ManagedAction<BraveEntity> {

    public BraveJumpOnAction(BraveEntity entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (!entity.hasLineOfSight(target)) return false;
        return !entity.mustRetreat(target.position());
//        return true;
    }

    @Override
    public void onStart() {
        this.timer = 0;
        this.entity.jumpingAnim.start();
    }

    @Override
    public boolean onTick() {
        timer++;

        if (entity.getTarget() != null) {
            entity.getLookControl().setLookAt(entity.getTarget(), 180, 180);
        }

        LivingEntity target = entity.getTarget();
        if (timer == 15 && target != null) {
            entity.playSound(ACSounds.BRAVE_JUMP.get(), 3.0F, 1.0F);
            this.jumpTowards(target);
        }

        if (entity.onGround() && timer >= 30) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.1F, 1.0, 0.1F));
            this.applyLandingDamage();
            return false;
        }

        return timer < 80;
    }

    @Override
    public void onStop() {
        entity.setDiscardFriction(false);
        entity.jumpingAnim.stop();
    }

    private void jumpTowards(LivingEntity target) {
        float range = 0.6F * (float) entity.getAttributeValue(Attributes.FOLLOW_RANGE);
        LongJumpUtil.calculateJumpVectorForAngle(entity, target.position(), range, 65, true)
                .ifPresentOrElse(this::launch, () -> this.launch(this.fallbackJumpVector(target)));
    }

    private void launch(Vec3 velocity) {
        if (velocity.lengthSqr() < 1.0E-8) {
            return;
        }

        entity.setYRot(entity.yBodyRot);
        entity.setDiscardFriction(true);
        entity.setDeltaMovement(velocity);
    }

    private Vec3 fallbackJumpVector(LivingEntity target) {
        Vec3 delta = target.position().subtract(entity.position());
        double horizontal = Math.sqrt(delta.x * delta.x + delta.z * delta.z);
        if (horizontal < 1.0E-4) {
            return Vec3.ZERO;
        }

        double horizontalSpeed = Mth.clamp(horizontal * 0.08F, 0.5F, 1.1F);
        return new Vec3(delta.x / horizontal * horizontalSpeed, 0.75F, delta.z / horizontal * horizontalSpeed);
    }

    private void applyLandingDamage() {
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;

        Vec3 center = entity.position();
        int radius = 4;
        AABB area = AABB.ofSize(center, radius * 2, 6, radius * 2);

        List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, area, e -> e != entity && e.isAlive() && entity.canAttack(e));

        entity.playSound(SoundEvents.MACE_SMASH_GROUND, 2.0F, 1.1F);
        entity.level().levelEvent(2013, entity.getOnPos(), 750);

        for (LivingEntity target : targets) {
            if (entity.attackTarget(serverLevel, target, 1.0F, ACEntity.Operation.MULTIPLY)) {
                Vec3 knockback = target.position().subtract(center).normalize().scale(1.8);
                target.setDeltaMovement(target.getDeltaMovement().add(knockback));
                target.hurtMarked = true;
            }
        }
    }
}