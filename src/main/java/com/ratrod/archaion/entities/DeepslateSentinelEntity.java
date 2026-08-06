package com.ratrod.archaion.entities;

import com.ratrod.archaion.api.client.animation.ACAnimation;
import com.ratrod.archaion.api.client.animation.EntityAnimationManager;
import com.ratrod.archaion.api.entity.ActionManager;
import com.ratrod.archaion.entities.ai.ACEntity;
import com.ratrod.archaion.entities.ai.actions.SentinelChargeAction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class DeepslateSentinelEntity extends Monster implements ACEntity<DeepslateSentinelEntity> {

    private final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    private final ActionManager<DeepslateSentinelEntity> attackManager = new ActionManager<>(this);

    public final ACAnimation chargeAnim = new ACAnimation(this);

    public DeepslateSentinelEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.attackManager.addAction(new SentinelChargeAction(this), 100);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
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
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 0.0F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().size() < 2;
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
        int index = this.getPassengers().indexOf(passenger);
        float forwardOffset = index == 0 ? 0.5F : -0.7F;
        float height = dimensions.height() * scale;
        return new Vec3(0.0D, height, forwardOffset * scale).yRot(-this.getYRot() * ((float)Math.PI / 180F));
    }
}
