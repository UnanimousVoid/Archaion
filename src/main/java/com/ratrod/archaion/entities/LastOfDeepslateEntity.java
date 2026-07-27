package com.ratrod.archaion.entities;

import com.ratrod.archaion.api.client.animation.ACAnimation;
import com.ratrod.archaion.api.client.animation.EntityAnimationManager;
import com.ratrod.archaion.api.entity.ActionManager;
import com.ratrod.archaion.client.animations.LastOfDeepslateAnimations;
import com.ratrod.archaion.entities.ai.ACEntity;
import com.ratrod.archaion.entities.ai.actions.SmashGroundAction;
import com.ratrod.archaion.entities.ai.actions.SwingSpinAction;
import com.ratrod.archaion.entities.ai.controls.move.ACMoveControl;
import com.ratrod.archaion.entities.ai.controls.pathnav.LargeEntityPathNavigation;
import com.ratrod.archaion.entities.ai.goals.GoToTargetGoal;
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

    private final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    private final ActionManager<LastOfDeepslateEntity> attackManager = new ActionManager<>(this);

    public final ACAnimation smashGroundAnim = new ACAnimation(this, () -> LastOfDeepslateAnimations.SMASH_GROUND);
    public final ACAnimation swingSpinAnim = new ACAnimation(this, () -> LastOfDeepslateAnimations.SPIN_SWING);

    public LastOfDeepslateEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new ACMoveControl<>(this);
        this.navigation = new LargeEntityPathNavigation(this, level);

        this.attackManager.addAction(new SmashGroundAction(this));
        this.attackManager.addAction(new SwingSpinAction(this));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new GoToTargetGoal(this, 1.2F, this.getBbWidth() * 1.5F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 0.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 8.0D)
                .add(Attributes.STEP_HEIGHT, 1.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
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
    public void tick() {
        super.tick();
    }
}
