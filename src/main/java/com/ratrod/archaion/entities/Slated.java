package com.ratrod.archaion.entities;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class Slated extends Zombie {
    public Slated(EntityType<? extends Slated> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 40)
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
}