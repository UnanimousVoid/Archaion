package com.ratrod.archaion.entities;

import com.ratrod.archaion.api.client.animation.ACAnimation;
import com.ratrod.archaion.api.client.animation.EntityAnimationManager;
import com.ratrod.archaion.api.entity.ActionManager;
import com.ratrod.archaion.entities.ai.ACEntity;
import com.ratrod.archaion.entities.ai.actions.GrimorayShootAction;
import com.ratrod.archaion.entities.ai.controls.move.ACMoveControl;
import com.ratrod.archaion.entities.ai.goals.GrimorayFlightGoal;
import com.ratrod.archaion.registry.ACEntityDataSerializers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class Grimoray extends Monster implements ACEntity<Grimoray> {

    public static final EntityDataAccessor<GrimorayType> GRIMORAY_TYPE = SynchedEntityData.defineId(Grimoray.class, ACEntityDataSerializers.GRIMORAY_TYPE.get());

    public static final float SHOOT_RANGE = 16.0F;
    public static final float MIN_SHOOT_RANGE = 3.0F;

    public final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    public final ActionManager<Grimoray> attackManager = new ActionManager<>(this);

    public final ACAnimation shootAnim = new ACAnimation(this);

    private int spellCooldown;

    public Grimoray(EntityType<? extends Grimoray> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.moveControl = new ACMoveControl<>(this);
        this.navigation = new FlyingPathNavigation(this, level);

        GrimorayType[] types = GrimorayType.values();
        this.entityData.set(GRIMORAY_TYPE, types[this.random.nextInt(types.length)]);

        this.attackManager.addAction(new GrimorayShootAction(this), 100);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(GRIMORAY_TYPE, GrimorayType.POISON_CLOUD);
    }

    public GrimorayType getGrimorayType() {
        return this.entityData.get(GRIMORAY_TYPE);
    }

    public void setGrimorayType(GrimorayType type) {
        this.entityData.set(GRIMORAY_TYPE, type);
    }

    public int getSpellCooldown() {
        return this.spellCooldown;
    }

    public void setSpellCooldown(int ticks) {
        this.spellCooldown = ticks;
    }

    public int getSpellCDDuration() {
        return switch (this.getGrimorayType()) {
            case POISON_CLOUD -> 70;
            case HARMING -> 60;
            case HEALING -> 80;
        };
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && this.spellCooldown > 0) {
            this.spellCooldown--;
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.getInt("grimorayType").ifPresent(num -> {
            if (num >= 0 && num < GrimorayType.values().length) {
                this.setGrimorayType(GrimorayType.values()[num]);
            }
        });
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("grimorayType", this.getGrimorayType().ordinal());
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.FLYING_SPEED, 0.4D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new GrimorayFlightGoal(this, 0.35, 3.0, 6.0));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
//        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public EntityAnimationManager getAnimationManager() {
        return animationManager;
    }

    @Override
    public List<ActionManager<Grimoray>> getActionManagers() {
        return List.of(attackManager);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.BOOK_PAGE_TURN;
    }

    @Override
    protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
    }

    @Override
    protected void playBlockFallSound() {
    }
}
