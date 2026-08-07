package com.ratrod.archaion.entities;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.api.client.animation.ACAnimation;
import com.ratrod.archaion.api.client.animation.EntityAnimationManager;
import com.ratrod.archaion.api.entity.ActionManager;
import com.ratrod.archaion.entities.ai.ACEntity;
import com.ratrod.archaion.entities.ai.SleepingState;
import com.ratrod.archaion.entities.ai.actions.*;
import com.ratrod.archaion.entities.ai.controls.look.LastOfDeepslateLookControl;
import com.ratrod.archaion.entities.ai.controls.move.ACMoveControl;
import com.ratrod.archaion.entities.ai.controls.pathnav.LargeEntityPathNavigation;
import com.ratrod.archaion.entities.ai.goals.GoToTargetGoal;
import com.ratrod.archaion.registry.ACEntityDataSerializers;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class LastOfDeepslateEntity extends Monster implements ACEntity<LastOfDeepslateEntity> {

    public static final EntityDataAccessor<SleepingState> SLEEPING_STATE = SynchedEntityData.defineId(LastOfDeepslateEntity.class, ACEntityDataSerializers.SLEEPING_STATE.get());

    private final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    private final ActionManager<LastOfDeepslateEntity> attackManager = new ActionManager<>(this);

    public final ACAnimation deathAnim = new ACAnimation(this);
    public final ACAnimation wakingAnim = new ACAnimation(this);
    public final ACAnimation shootAnim = new ACAnimation(this);
    public final ACAnimation smashGroundAnim = new ACAnimation(this);
    public final ACAnimation swingSpinAnim = new ACAnimation(this);
    public final ACAnimation interceptShootAnim = new ACAnimation(this);
    public final ACAnimation rollingAnim = new ACAnimation(this);

    private int wakingStartTick = 0;
    private boolean deathAnimationPlayed = false;
    private final ServerBossEvent bossEvent;

    public LastOfDeepslateEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new ACMoveControl<>(this);
        this.navigation = new LargeEntityPathNavigation(this, level);
        this.lookControl = new LastOfDeepslateLookControl(this);
        this.bossEvent = new ServerBossEvent(Mth.createInsecureUUID(this.random), this.getDisplayName(), BossBarColor.BLUE, BossBarOverlay.PROGRESS);

        this.attackManager.addAction(new LODSmashGroundAction(this), 100);
        this.attackManager.addAction(new LODSwingSpinAction(this), 150);
        this.attackManager.addAction(new LODShootAction(this), 100);
        this.attackManager.addAction(new LODInterceptShootAction(this), 500);
        this.attackManager.addAction(new LODRollAction(this), 20);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new GoToTargetGoal(this, 1.2F, this.getBbWidth() * 1.5F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 0.0F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 22.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 8.0D)
                .add(Attributes.STEP_HEIGHT, 1.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 2.0D);
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public EntityAnimationManager getAnimationManager() {
        return animationManager;
    }

    @Override
    public List<ActionManager<LastOfDeepslateEntity>> getActionManagers() {
        return List.of(attackManager);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public float getRotationFreedom() {
        return 0.15F;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SLEEPING_STATE, SleepingState.SLEEPING);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

            SleepingState currentState = this.entityData.get(SLEEPING_STATE);
            int curWakingTick = this.tickCount - this.wakingStartTick;

            Predicate<Entity> predicate = EntitySelector.NO_CREATIVE_OR_SPECTATOR;
            boolean nearbyPlayers = level().getNearestPlayer(getX(), getY(), getZ(), 32, predicate) != null;
            if (nearbyPlayers) {
                if (currentState == SleepingState.SLEEPING) {
                    this.entityData.set(SLEEPING_STATE, SleepingState.WAKING);
                    this.playSound(ACSounds.LOD_ACTIVATE.get(), 3.0F, 1.0F);
                    this.wakingAnim.forceStart();
                    this.wakingStartTick = this.tickCount;
                }
            }

            if (currentState == SleepingState.WAKING) {
                if (curWakingTick == 45) {
                    this.playSound(ACSounds.LOD_ACTIVATE_SMASH.get(), 3.0F, 1.0F);

                    for (ServerPlayer player : ((ServerLevel)this.level()).getPlayers(p -> this.getSensing().hasLineOfSight(p) || this.distanceTo(p) < 512)) {
                        this.addBossBarPlayer(bossEvent, player, 0);
                    }
                }
                if (curWakingTick >= 80) {
                    this.entityData.set(SLEEPING_STATE, SleepingState.AWAKE);
                    this.setNoAi(false);
                }
            }

        } else {
            if (this.entityData.get(SLEEPING_STATE) != SleepingState.SLEEPING && tickCount % 8 == 0) {
                ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_aura"));
                AAALevel.addParticle(level(), info.position(this.position().add(this.getDeltaMovement().scale(3)).add(0, 0.5, 0)).scale(1.25F));
            }
        }
    }

    @Override
    protected void tickDeath() {
        this.deathTime++;

        if (!deathAnimationPlayed) {
            if (this.entityData.get(SLEEPING_STATE) != SleepingState.AWAKE) {
                this.entityData.set(SLEEPING_STATE, SleepingState.AWAKE);
            }
            this.deathAnim.forceStart();
            deathAnimationPlayed = true;
        }
        if (this.deathTime >= 80 && !this.level().isClientSide() && !this.isRemoved()) {
            this.level().broadcastEntityEvent(this, (byte)60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    public boolean isNoAi() {
        return super.isNoAi() || this.entityData.get(SLEEPING_STATE) != SleepingState.AWAKE;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        if (this.entityData.get(SLEEPING_STATE) == SleepingState.AWAKE) {
            this.addBossBarPlayer(bossEvent, player, 0);
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.removeBossBarPlayer(bossEvent, player);
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (this.entityData.get(SLEEPING_STATE) != SleepingState.AWAKE) return false;
        return super.hurtServer(level, source, damage);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.getInt("sleepState").ifPresent(num -> {
            this.entityData.set(SLEEPING_STATE, SleepingState.values()[num]);
        });
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("sleepState", this.entityData.get(SLEEPING_STATE).ordinal());
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ACSounds.LOD_AMBIENT.get();
    }

    @Override
    protected float getSoundVolume() {
        return 3.0F;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSounds.LOD_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ACSounds.LOD_HURT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
        super.playStepSound(pos, blockState);
        this.playSound(ACSounds.LOD_STEP.get(), 1.0F, 1.0F);
    }

    @Override
    public float getSecondsToDisableBlocking() {
        return 5.0F;
    }
}
