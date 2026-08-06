package com.ratrod.archaion.entities;

import com.ratrod.archaion.api.client.animation.ACAnimation;
import com.ratrod.archaion.api.client.animation.EntityAnimationManager;
import com.ratrod.archaion.api.entity.ActionManager;
import com.ratrod.archaion.entities.ai.ACEntity;
import com.ratrod.archaion.entities.ai.actions.BraveJumpOnAction;
import com.ratrod.archaion.entities.ai.goals.BraveDistanceAwayGoal;
import com.ratrod.archaion.registry.ACSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class BraveEntity extends Monster implements ACEntity<BraveEntity> {

    public final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    public final ActionManager<BraveEntity> attackManager = new ActionManager<>(this);

    public final ACAnimation jumpingAnim = new ACAnimation(this);
    public final ACAnimation shootingAnim = new ACAnimation(this);

    public BraveEntity(EntityType<? extends Monster> entityType, Level level) {
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
                .add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new BraveDistanceAwayGoal(this, 1.2D, 18.0D));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
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
    public List<ActionManager<BraveEntity>> getActionManagers() {
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