package com.ratrod.archaion.entities.ai.actions;

import com.ratrod.archaion.api.entity.ManagedAction;
import com.ratrod.archaion.entities.LastOfDeepslateEntity;
import com.ratrod.archaion.entities.ai.ACEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SwingSpinAction extends ManagedAction<LastOfDeepslateEntity> {


    public SwingSpinAction(LastOfDeepslateEntity entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (!entity.hasLineOfSight(target)) return false;
        return entity.distanceTo(target) <= entity.getBbWidth() * 1.5F;
    }

    @Override
    public void onStart() {
        this.timer = 0;
        entity.swingSpinAnim.start();
    }

    @Override
    public void onStop() {
        entity.swingSpinAnim.stop();
    }

    @Override
    public boolean onTick() {
        timer++;

        if (timer == 15) {
            this.applySwingDamage();
        }

        return timer < 50;
    }

    private void applySwingDamage() {
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;

        Vec3 center = entity.position();
        float width = entity.getBbWidth() * 1.25F;
        AABB area = AABB.ofSize(center, width * 2, 6, width * 2);

        List<LivingEntity> targets = entity.level().getEntitiesOfClass(LivingEntity.class, area, e -> e != entity && e.isAlive() && entity.canAttack(e));

        for (LivingEntity target : targets) {
            entity.attackTarget(serverLevel, target, 1.0F, ACEntity.Operation.MULTIPLY);
            Vec3 knockback = target.position().subtract(center).normalize().scale(1.5).add(0, 1.2, 0);
            target.setDeltaMovement(target.getDeltaMovement().add(knockback));
            target.hurtMarked = true;
        }
    }
}
