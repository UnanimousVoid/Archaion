package com.ratrod.archaion.entities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class Slated extends Zombie {
    public Slated(EntityType<? extends Slated> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 50.0D);
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public void setBaby(boolean baby) {
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
        return super.finalizeSpawn(level, difficulty, spawnReason, new ZombieGroupData(false, true));
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean result = super.doHurtTarget(level, target);
        if (result && this.getMainHandItem().isEmpty() && target instanceof LivingEntity) {
            float difficulty = level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            ((LivingEntity)target).addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 140 * (int)difficulty), this);
        }

        return result;
    }

    @Override
    protected boolean convertsInWater() {
        return false;
    }
}