package com.ratrod.archaion.entities;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public class Slated extends Zombie implements Archaic {

    public static final EntityDataAccessor<Boolean> IS_CHARGED = SynchedEntityData.defineId(Slated.class, EntityDataSerializers.BOOLEAN);
    @Nullable private UUID ownerUUID;

    public Slated(EntityType<? extends Slated> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_CHARGED, false);
    }

    public boolean isCharged() {
        return this.entityData.get(IS_CHARGED);
    }

    public void setCharged(boolean charged) {
        this.entityData.set(IS_CHARGED, charged);
    }

    @Override
    protected void dropExperience(ServerLevel level, @Nullable Entity entity) {
        this.xpReward = this.archaicXpReward(this.xpReward);
        super.dropExperience(level, entity);
    }

    @Nullable
    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(@Nullable UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setCharged(input.getBooleanOr("isCharged", false));
        input.getString("ownerUUID").ifPresent(s -> this.ownerUUID = UUID.fromString(s));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("isCharged", this.isCharged());
        if (this.ownerUUID != null) {
            output.putString("ownerUUID", this.ownerUUID.toString());
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
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