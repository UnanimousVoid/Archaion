package com.ratrod.archaion.entities.ai.actions;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.api.entity.ManagedAction;
import com.ratrod.archaion.entities.LODFallingBlock;
import com.ratrod.archaion.entities.LastOfDeepslate;
import com.ratrod.archaion.entities.ai.ACEntity;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LODSmashGroundAction extends ManagedAction<LastOfDeepslate> {

    public LODSmashGroundAction(LastOfDeepslate entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive()) return false;
//        if (!entity.hasLineOfSight(target)) return false;
        if (entity.getY() + 3 < target.getY()) return false;
        float r = 1.5F;
        if (entity.getArchaicSystem().getPhasesTriggered() >= 1) {
            r = 8F;
        }
        return entity.distanceTo(target) <= entity.getBbWidth() * r;
    }

    @Override
    public void onStart() {
        this.timer = 0;
        entity.smashGroundAnim.start();
        entity.playSound(ACSounds.LOD_ACTION_START.get(), 3.0F, 1.0F);
    }

    @Override
    public boolean onTick() {
        timer++;
        Difficulty difficulty = entity.level().getDifficulty();
        boolean isHard = difficulty == Difficulty.HARD;

        if (timer == 27) {
            this.applySmashDamage(isHard ? 1.075F : 1.0F);
        }

        if (timer > 18 && entity.getTarget() != null) {
            this.entity.getLookControl().setLookAt(entity.getTarget(), 30.0F, 30.0F);
        }

        return timer < (isHard ? 50 : 60);
    }

    private void applyBlockFalling() {
        Level level = entity.level();
        BlockPos basePos = entity.blockPosition();
        int hRad = 32;
        int scanRange = 64;

        for (int xx = -hRad; xx <= hRad; xx++) {
            for (int zz = -hRad; zz <= hRad; zz++) {
                int x = basePos.getX() + xx;
                int z = basePos.getZ() + zz;
                BlockPos.MutableBlockPos scanPos = new BlockPos.MutableBlockPos(x, basePos.getY(), z);
                boolean found = false;

                for (int i = 0; i < scanRange; i++) {
                    scanPos.move(Direction.UP);
                    if (!level.isEmptyBlock(scanPos)) {
                        found = true;
                        break;
                    }
                }

                if (found && level.getRandom().nextFloat() < 0.01) {
                    BlockPos fallingPos = scanPos.immutable();
                    LODFallingBlock.spawn((ServerLevel) level, level.getBlockState(fallingPos), new Vec3(fallingPos.getX() + 0.5, fallingPos.getY() - 0.5, fallingPos.getZ() + 0.5), entity);

                    BlockPos.MutableBlockPos groundPos = new BlockPos.MutableBlockPos(x, basePos.getY(), z);
                    while (level.isEmptyBlock(groundPos) && groundPos.getY() > level.getMinBuildHeight()) {
                        groundPos.move(Direction.DOWN);
                    }

                    ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_falling_block_indicator"));
                    AAALevel.addParticle(level, info.position(Vec3.atBottomCenterOf(groundPos)).scale(1.5F));
                }
            }
        }
    }

    private void applySmashDamage(float attackMultiplier) {
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;

        entity.playSound(ACSounds.LOD_SMASH.get(), 3.0F, 1.0F);
        if (entity.getArchaicSystem().getPhasesTriggered() >= 1) {
            this.applyBlockFalling();
        }

        float yaw = entity.getYHeadRot() * Mth.DEG_TO_RAD;
        Vec3 flatLook = new Vec3(-Mth.sin(yaw), 0, Mth.cos(yaw));
        Vec3 center = entity.position().add(flatLook.yRot(7.5F * Mth.DEG_TO_RAD).scale(7.5));

        ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_boom_ground"));
        AAALevel.addParticle(serverLevel, info.position(center.add(0, 0.2, 0)).scale(3.0F));

        AABB area = AABB.ofSize(center, 9, 6, 9);

        List<LivingEntity> targets = entity.level().getEntitiesOfClass(LivingEntity.class, area, e -> e != entity && e.isAlive() && entity.canAttack(e));

        for (LivingEntity target : targets) {
            if (entity.attackTarget(target, attackMultiplier, ACEntity.Operation.MULTIPLY)) {
                Vec3 knockback = target.position().subtract(center).normalize().scale(1.5).add(0, 0.35, 0);
                target.setDeltaMovement(target.getDeltaMovement().add(knockback));
                target.hurtMarked = true;
            }
        }
    }

    @Override
    public void onStop() {
        entity.smashGroundAnim.stop();
    }
}
