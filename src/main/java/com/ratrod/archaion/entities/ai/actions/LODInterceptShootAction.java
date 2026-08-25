package com.ratrod.archaion.entities.ai.actions;

import com.ratrod.archaion.api.entity.ManagedAction;
import com.ratrod.archaion.entities.projectile.EchoStarProjectile;
import com.ratrod.archaion.entities.LastOfDeepslate;
import com.ratrod.archaion.entities.projectile.LODInterceptBlast;
import com.ratrod.archaion.registry.ACEntityTypes;
import com.ratrod.archaion.registry.ACSounds;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class LODInterceptShootAction extends ManagedAction<LastOfDeepslate> {

    public LODInterceptShootAction(LastOfDeepslate entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (!entity.hasLineOfSight(target)) return false;
        return entity.getY() + entity.getBbHeight() < target.getY();
    }

    @Override
    public void onStart() {
        this.timer = 0;
        entity.interceptShootAnim.start();
        entity.playSound(ACSounds.LOD_ACTION_START.get(), 3.0F, 1.0F);
    }

    @Override
    public boolean onTick() {
        timer++;

        if (timer == 14) {

            float yaw = entity.getYHeadRot() * Mth.DEG_TO_RAD;
            Vec3 flatLook = new Vec3(-Mth.sin(yaw), 0, Mth.cos(yaw));
            Vec3 center = entity.position().add(flatLook.yRot(-90 * Mth.DEG_TO_RAD).scale(3).with(Direction.Axis.Y, entity.getBbHeight() + 6));

            if (entity.level() instanceof ServerLevel serverLevel) {
                LODInterceptBlast blast = ACEntityTypes.LOD_INTERCEPT_BLAST.get().create(serverLevel);
                blast.moveTo(center);
                blast.source = this.entity;
                blast.setOwner(this.entity);
                blast.setDeltaMovement(0, 3.0, 0);
                serverLevel.addFreshEntity(blast);
            }

            for (int i = 0; i < 5; i++) {
                EchoStarProjectile projectile = ACEntityTypes.ECHO_STAR.get().create(entity.level());
                projectile.moveTo(center);
                projectile.setOwner(entity);
                double motionX = (entity.getRandom().nextDouble() - 0.5) * 2;
                double motionY = 1.0;
                double motionZ = (entity.getRandom().nextDouble() - 0.5) * 2;
                projectile.setDeltaMovement(motionX, motionY, motionZ);
                entity.level().addFreshEntity(projectile);
            }
        }

        return timer < 40;
    }

    @Override
    public void onStop() {
        entity.interceptShootAnim.stop();
    }
}
