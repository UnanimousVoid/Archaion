package com.ratrod.archaion.entities;

import com.ratrod.archaion.api.client.animation.ACAnimation;
import com.ratrod.archaion.api.client.animation.EntityAnimationManager;
import com.ratrod.archaion.api.entity.ActionManager;
import com.ratrod.archaion.entities.ai.ACEntity;
import com.ratrod.archaion.entities.ai.actions.SentinelChargeAction;
import com.ratrod.archaion.entities.ai.controls.pathnav.LargeEntityPathNavigation;
import com.ratrod.archaion.entities.ai.goals.PickUpRidersGoal;
import com.ratrod.archaion.registry.ACSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class DeepslateSentinelEntity extends Monster implements ACEntity<DeepslateSentinelEntity> {

    private final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    private final ActionManager<DeepslateSentinelEntity> attackManager = new ActionManager<>(this);

    public final ACAnimation chargeAnim = new ACAnimation(this);

    private int chargeCooldownTicks;

    public DeepslateSentinelEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.navigation = new LargeEntityPathNavigation(this, level);

        this.attackManager.addAction(new SentinelChargeAction(this), 100);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    public EntityAnimationManager getAnimationManager() {
        return animationManager;
    }

    @Override
    public List<ActionManager<DeepslateSentinelEntity>> getActionManagers() {
        return List.of(attackManager);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PickUpRidersGoal(this, 1.0D, 32.0D));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 1.0D, 48.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 0.0F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        if (this.chargeCooldownTicks > 0) {
            this.chargeCooldownTicks--;
        }
        super.tick();
    }

    public boolean isChargeOnCooldown() {
        return this.chargeCooldownTicks > 0;
    }

    public void startChargeCooldown() {
        this.chargeCooldownTicks = this.random.nextIntBetweenInclusive(120, 180);
    }

    public boolean hasNearbyRidersToPickup(double searchRadius) {
        if (!this.getPassengers().isEmpty()) {
            return false;
        }
        AABB box = this.getBoundingBox().inflate(searchRadius);
        Predicate<Entity> free = e -> e.isAlive() && !e.isPassenger();
        return !this.level().getEntitiesOfClass(Wight.class, box, free).isEmpty();
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty() && passenger instanceof Wight;
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
        float forwardOffset = 0.5F;
        float height = dimensions.height() * scale;
        return new Vec3(0.0D, height, forwardOffset * scale).yRot(-this.getYRot() * ((float)Math.PI / 180F));
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        return hasNearbyRidersToPickup(32) ? null : super.getControllingPassenger();
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ACSounds.SENTINEL_AMBIENT.get();
    }

    @Override
    protected float getSoundVolume() {
        return 2.0F;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ACSounds.SENTINEL_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSounds.SENTINEL_DEATH.get();
    }
}
