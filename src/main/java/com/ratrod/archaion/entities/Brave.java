package com.ratrod.archaion.entities;

import com.ratrod.archaion.api.client.animation.ACAnimation;
import com.ratrod.archaion.api.client.animation.EntityAnimationManager;
import com.ratrod.archaion.api.entity.ActionManager;
import com.ratrod.archaion.entities.ai.ACEntity;
import com.ratrod.archaion.entities.ai.actions.BraveJumpOnAction;
import com.ratrod.archaion.entities.ai.goals.BraveDistanceAwayGoal;
import com.ratrod.archaion.entities.ai.goals.BraveSpreadTargetGoal;
import com.ratrod.archaion.registry.ACSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class Brave extends Monster implements ACEntity<Brave>, Archaic {

    public static final EntityDataAccessor<Boolean> IS_CHARGED = SynchedEntityData.defineId(Brave.class, EntityDataSerializers.BOOLEAN);

    public final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    public final ActionManager<Brave> attackManager = new ActionManager<>(this);

    public final ACAnimation jumpingAnim = new ACAnimation(this);
    public final ACAnimation shootingAnim = new ACAnimation(this);

    @Nullable private UUID ownerUUID;

    public Brave(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.ON_TOP_OF_TRAPDOOR, -1.0F);
        this.setPathfindingMalus(PathType.FIRE, -1.0F);
        this.xpReward = 20;

        this.attackManager.addAction(new BraveJumpOnAction(this), 100);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.35F)
                .add(Attributes.MAX_HEALTH, 55.0)
                .add(Attributes.FOLLOW_RANGE, 48.0)
                .add(Attributes.ATTACK_DAMAGE, 12.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new BraveDistanceAwayGoal(this, 1.1D, 18.0D));
        this.targetSelector.addGoal(1, new BraveSpreadTargetGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
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
        Entity entity = this.level().getEntity(this.ownerUUID);
        return entity instanceof LivingEntity living ? living : null;
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

    public boolean mustRetreat(Vec3 target) {
        Vec3 ourPosition = this.blockPosition().getCenter();
        return target.closerThan(ourPosition, 15.0, 15.0);
    }

    @Override
    public EntityAnimationManager getAnimationManager() {
        return animationManager;
    }

    @Override
    public List<ActionManager<Brave>> getActionManagers() {
        return List.of(attackManager);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return (target.is(EntityType.PLAYER) || target.is(EntityType.IRON_GOLEM)) && super.canAttack(target);
    }

    @Override
    public int getMaxHeadYRot() {
        return 30;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ACSounds.BRAVE_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ACSounds.BRAVE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSounds.BRAVE_DEATH.get();
    }

    @Override
    protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
    }

    @Override
    protected void playMuffledStepSound(BlockState blockState, BlockPos pos) {
    }

    @Override
    protected void playCombinationStepSounds(BlockState primaryStepSound, BlockState secondaryStepSound, BlockPos primaryPos, BlockPos secondaryPos) {
    }
}