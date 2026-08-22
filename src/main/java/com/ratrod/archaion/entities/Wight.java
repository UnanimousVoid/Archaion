package com.ratrod.archaion.entities;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public class Wight extends Skeleton implements Archaic {

    public static final EntityDataAccessor<Boolean> IS_CHARGED = SynchedEntityData.defineId(Wight.class, EntityDataSerializers.BOOLEAN);
    @Nullable private UUID ownerUUID;

    public Wight(EntityType<? extends Wight> entityType, Level level) {
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
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.MAX_HEALTH, 22);
    }

    @Override
    protected AbstractArrow getArrow(ItemStack projectile, float power, @Nullable ItemStack firingWeapon) {
        AbstractArrow arrow = super.getArrow(projectile, power, firingWeapon);
        if (arrow instanceof Arrow) {
            ((Arrow)arrow).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 300));
            arrow.setBaseDamage(6);
        }

        return arrow;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        float dmgResult = this.getVehicle() instanceof DeepslateSentinel ? (damage * 0.5F) : damage;
        return super.hurtServer(level, source, dmgResult);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.STRAY_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.STRAY_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.STRAY_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.STRAY_STEP;
    }
}