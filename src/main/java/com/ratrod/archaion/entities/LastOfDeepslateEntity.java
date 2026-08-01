package com.ratrod.archaion.entities;

import com.ratrod.archaion.api.client.animation.ACAnimation;
import com.ratrod.archaion.api.client.animation.EntityAnimationManager;
import com.ratrod.archaion.api.entity.ActionManager;
import com.ratrod.archaion.client.animations.LastOfDeepslateAnimations;
import com.ratrod.archaion.entities.ai.ACEntity;
import com.ratrod.archaion.entities.ai.SleepingState;
import com.ratrod.archaion.entities.ai.actions.LODShootAction;
import com.ratrod.archaion.entities.ai.actions.LODSmashGroundAction;
import com.ratrod.archaion.entities.ai.actions.LODSwingSpinAction;
import com.ratrod.archaion.entities.ai.controls.move.ACMoveControl;
import com.ratrod.archaion.entities.ai.controls.pathnav.LargeEntityPathNavigation;
import com.ratrod.archaion.entities.ai.goals.GoToTargetGoal;
import com.ratrod.archaion.registry.ACEntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
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

import java.util.List;

public class LastOfDeepslateEntity extends Monster implements ACEntity<LastOfDeepslateEntity> {

    public static final EntityDataAccessor<SleepingState> SLEEPING_STATE = SynchedEntityData.defineId(LastOfDeepslateEntity.class, ACEntityDataSerializers.SLEEPING_STATE.get());

    private final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    private final ActionManager<LastOfDeepslateEntity> attackManager = new ActionManager<>(this);

    public final ACAnimation deathAnim = new ACAnimation(this, () -> LastOfDeepslateAnimations.DYING);
    public final ACAnimation wakingAnim = new ACAnimation(this, () -> LastOfDeepslateAnimations.WAKING);
    public final ACAnimation shootAnim = new ACAnimation(this, () -> LastOfDeepslateAnimations.SHOOT_LAND);
    public final ACAnimation smashGroundAnim = new ACAnimation(this, () -> LastOfDeepslateAnimations.SMASH_GROUND);
    public final ACAnimation swingSpinAnim = new ACAnimation(this, () -> LastOfDeepslateAnimations.SPIN_SWING);

    private int wakingStartTick = 0;
    private boolean deathAnimationPlayed = false;

    public LastOfDeepslateEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new ACMoveControl<>(this);
        this.navigation = new LargeEntityPathNavigation(this, level);

        this.attackManager.addAction(new LODSmashGroundAction(this), 100);
        this.attackManager.addAction(new LODSwingSpinAction(this), 150);
        this.attackManager.addAction(new LODShootAction(this), 100);
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
                .add(Attributes.MAX_HEALTH, 300.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D)
                .add(Attributes.FOLLOW_RANGE, 128.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 8.0D)
                .add(Attributes.STEP_HEIGHT, 1.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
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
        if (this.entityData.get(SLEEPING_STATE) != SleepingState.AWAKE) {
            this.targetSelector.tick();
        }

        super.tick();

        if (!this.level().isClientSide()) {
            SleepingState currentState = this.entityData.get(SLEEPING_STATE);

            if (this.getTarget() != null) {
                if (currentState == SleepingState.SLEEPING) {
                    this.entityData.set(SLEEPING_STATE, SleepingState.WAKING);
                    this.wakingAnim.forceStart();
                    this.wakingStartTick = this.tickCount;
                } else if (currentState == SleepingState.WAKING) {
                    if (this.tickCount - this.wakingStartTick >= 80) {
                        this.entityData.set(SLEEPING_STATE, SleepingState.AWAKE);
                        this.setNoAi(false);
                    }
                }
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
    public float getSecondsToDisableBlocking() {
        return 5.0F;
    }
}
