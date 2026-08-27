package com.ratrod.archaion.entities;

import com.ratrod.archaion.api.client.animation.ACAnimation;
import com.ratrod.archaion.api.client.animation.EntityAnimationManager;
import com.ratrod.archaion.api.entity.ActionManager;
import com.ratrod.archaion.entities.ai.ACEntity;
import com.ratrod.archaion.entities.ai.actions.HaunterExplodeAction;
import com.ratrod.archaion.registry.ACSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class Haunter extends Monster implements ACEntity<Haunter>, Archaic, PowerableMob {

    public static final EntityDataAccessor<Boolean> IS_CHARGED = SynchedEntityData.defineId(Haunter.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> IS_SWELLING = SynchedEntityData.defineId(Haunter.class, EntityDataSerializers.BOOLEAN);

    public final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    public final ActionManager<Haunter> attackManager = new ActionManager<>(this);

    public final ACAnimation explodingAnim = new ACAnimation(this);

    public int explodingCooldown;
    @Nullable private UUID ownerUUID;

    public Haunter(EntityType<? extends Haunter> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 12;

        this.attackManager.addAction(new HaunterExplodeAction(this), 100);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_CHARGED, false);
        builder.define(IS_SWELLING, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && this.explodingCooldown > 0) {
            this.explodingCooldown--;
        }
    }

    public boolean isSwelling() {
        return this.entityData.get(IS_SWELLING);
    }

    public void setSwelling(boolean swelling) {
        this.entityData.set(IS_SWELLING, swelling);
    }

    public boolean isCharged() {
        return this.entityData.get(IS_CHARGED);
    }

    @Override
    public boolean isPowered() {
        return this.isCharged();
    }

    public void setCharged(boolean charged) {
        this.entityData.set(IS_CHARGED, charged);
    }

    public void setOwnerUUID(@Nullable UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    @Nullable
    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.ownerUUID == null) return null;
        if (this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(this.ownerUUID);
            return entity instanceof LivingEntity living ? living : null;
        }
        return null;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public EntityAnimationManager getAnimationManager() {
        return animationManager;
    }

    @Override
    public List<ActionManager<Haunter>> getActionManagers() {
        return List.of(attackManager);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return (target.getType() == EntityType.PLAYER || target.getType() == EntityType.IRON_GOLEM) && super.canAttack(target);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return false;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag input) {
        super.readAdditionalSaveData(input);
        this.setCharged(input.getBoolean("isCharged"));
        this.explodingCooldown = input.getInt("explodingCooldown");
        if (input.contains("ownerUUID")) {
            this.ownerUUID = UUID.fromString(input.getString("ownerUUID"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("isCharged", this.isCharged());
        output.putInt("explodingCooldown", this.explodingCooldown);
        if (this.ownerUUID != null) {
            output.putString("ownerUUID", this.ownerUUID.toString());
        }
    }

    @Override
    protected void dropExperience(@Nullable Entity entity) {
        this.xpReward = this.archaicXpReward(this.xpReward);
        super.dropExperience(entity);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ACSounds.HAUNTER_AMBIENT.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return ACSounds.HAUNTER_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return ACSounds.HAUNTER_HURT.get();
    }
}