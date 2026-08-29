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
import com.ratrod.archaion.entities.ai.goals.LODAttackableRandomTargetGoal;
import com.ratrod.archaion.entities.ai.systems.ArchaicRaid;
import com.ratrod.archaion.item.EchoChargeItem;
import com.ratrod.archaion.misc.LODTheme;
import com.ratrod.archaion.network.ACNetwork;
import com.ratrod.archaion.network.BossBarDataOutput;
import com.ratrod.archaion.network.s2c.BossMusicPacket;
import com.ratrod.archaion.registry.ACEffects;
import com.ratrod.archaion.registry.ACEntityDataSerializers;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class LastOfDeepslate extends Monster implements ACEntity<LastOfDeepslate> {

    public static final EntityDataAccessor<SleepingState> SLEEPING_STATE = SynchedEntityData.defineId(LastOfDeepslate.class, ACEntityDataSerializers.SLEEPING_STATE.get());
    public static final EntityDataAccessor<Boolean> HAS_CHARGED_ARCHAICS = SynchedEntityData.defineId(LastOfDeepslate.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> ECHO_CHARGES_FED = SynchedEntityData.defineId(LastOfDeepslate.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> RAID_PHASE = SynchedEntityData.defineId(LastOfDeepslate.class, EntityDataSerializers.INT);

    public final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    public final ActionManager<LastOfDeepslate> attackManager = new ActionManager<>(this);

    public final ACAnimation deathAnim = new ACAnimation(this);
    public final ACAnimation wakingAnim = new ACAnimation(this);
    public final ACAnimation shootAnim = new ACAnimation(this);
    public final ACAnimation smashGroundAnim = new ACAnimation(this);
    public final ACAnimation swingSpinAnim = new ACAnimation(this);
    public final ACAnimation interceptShootAnim = new ACAnimation(this);
    public final ACAnimation rollingAnim = new ACAnimation(this);
    public final ACAnimation spawnArchaicsAnim = new ACAnimation(this);
    public final ACAnimation bodySlamAnim = new ACAnimation(this);

    public final ServerBossEvent bossEvent;
    public final ArchaicRaid archaicSystem = new ArchaicRaid(this);

    public int shootingCooldown = 0;
    public int wakingStartTick = 0;
    public boolean deathAnimationPlayed = false;
    public LODTheme musicPhase = LODTheme.PHASE_1;

    public LastOfDeepslate(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new ACMoveControl<>(this);
        this.navigation = new LargeEntityPathNavigation(this, level);
        this.lookControl = new LastOfDeepslateLookControl(this);
        this.bossEvent = new ServerBossEvent(Mth.createInsecureUUID(this.random), this.getDisplayName(), BossBarColor.BLUE, BossBarOverlay.PROGRESS);

        this.attackManager.addAction(new LODSmashGroundAction(this), 100);
        this.attackManager.addAction(new LODSwingSpinAction(this), 150);
        this.attackManager.addAction(new LODShootAction(this), 150);
        this.attackManager.addAction(new LODInterceptShootAction(this), 500);
        this.attackManager.addAction(new LODRollAction(this), action -> action.entity.getArchaicSystem().getPhasesTriggered() < 2 ? 40 : 70);
        this.attackManager.addAction(new LODSpawnArchaicsAction(this), 2000);
        this.attackManager.addAction(new LODBodySlamAction(this), 100);
    }

    public ArchaicRaid getArchaicSystem() {
        return this.archaicSystem;
    }

    public ServerBossEvent getBossEvent() {
        return this.bossEvent;
    }

    public void sendBossMusic(LODTheme theme) {
        for (ServerPlayer player : this.bossEvent.getPlayers()) {
            ACNetwork.sendToPlayer(player, new BossMusicPacket(theme));
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GoToTargetGoal(this, 1.2F, this.getBbWidth() * 1.5F));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 0.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new LODAttackableRandomTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 600.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(Attributes.ATTACK_DAMAGE, 40.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.ARMOR, 15.0D)
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
    public List<ActionManager<LastOfDeepslate>> getActionManagers() {
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
    public boolean canAttack(LivingEntity target) {
        if (this.archaicSystem.isOwnedArchaic(target)) {
            return false;
        }
        return super.canAttack(target);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {

            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
            this.archaicSystem.tick();
            if (shootingCooldown > 0) shootingCooldown --;

            SleepingState currentState = this.getSleepingState();
            int curWakingTick = this.tickCount - this.wakingStartTick;

            if (currentState == SleepingState.WAKING) {
                if (curWakingTick == 45) {
                    this.playSound(ACSounds.LOD_ACTIVATE_SMASH.get(), 3.0F, 1.0F);

                    for (ServerPlayer player : ((ServerLevel)this.level()).getPlayers(p -> this.getSensing().hasLineOfSight(p) || this.distanceTo(p) < 512)) {
                        this.addBossBarPlayer(bossEvent, player, 0);
                    }
                    this.sendPhaseSwitchMessage();

                }
                if (curWakingTick >= 80) {
                    this.setSleepingState(SleepingState.AWAKE);
                    this.setNoAi(false);
                }
            }

        } else {
            if (this.getSleepingState() != SleepingState.SLEEPING) {
                if (tickCount % 8 == 0) {
                    ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_aura"));
                    AAALevel.addParticle(level(), info.position(this.position().add(this.getDeltaMovement().scale(3)).add(0, 0.5, 0)).scale(1.25F));
                }

                if (tickCount % 20 == 0 && this.getPhase() == 2) {
                    ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_smoking"));
                    AAALevel.addParticle(level(), info.bindOnEntity(this).position(0, 3, 0).scale(1.5F));
                }
            }
        }
    }

    public void sendPhaseSwitchMessage() {
        this.getBossEvent().getPlayers().forEach(player -> {
            int phase = this.archaicSystem.getPhasesTriggered();
            String key = String.format("misc.archaion.last_of_deepslate.phase_%s_notifier", phase + 1);
            int color = switch (phase) {
                case 2 -> 0xfff769;
                case 1 -> 0x00b3ff;
                default -> 0x59fff9;
            };
            player.sendSystemMessage(Component.translatable(key).withColor(color));
        });
    }

    @Override
    protected void tickDeath() {
        this.deathTime++;

        if (!deathAnimationPlayed) {
            if (this.getSleepingState() != SleepingState.AWAKE) {
                this.setSleepingState(SleepingState.AWAKE);
            }
            bossEvent.getPlayers().forEach(player -> {
                ACNetwork.sendToPlayer(player, new BossMusicPacket(LODTheme.STOP));
            });
            this.deathAnim.forceStart();
            deathAnimationPlayed = true;
        }
        if (this.deathTime >= 60 && !this.level().isClientSide() && !this.isRemoved()) {
            this.level().broadcastEntityEvent(this, (byte)60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    public boolean isNoAi() {
        return super.isNoAi() || this.getSleepingState() != SleepingState.AWAKE;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        if (this.getSleepingState() == SleepingState.AWAKE) {
            this.addBossBarPlayer(bossEvent, player, 0);
        }
    }

    @Override
    public void writeBossBarData(BossBarDataOutput output) {
        this.archaicSystem.writeBossBarData(output);
    }

    @Override
    public void addBossBarPlayer(ServerBossEvent bossEvent, ServerPlayer player, int bossIdx, Map<String, Integer> values) {
        ACEntity.super.addBossBarPlayer(bossEvent, player, bossIdx, values);
        ACNetwork.sendToPlayer(player, new BossMusicPacket(this.musicPhase));
    }

    @Override
    public void removeBossBarPlayer(ServerBossEvent bossEvent, ServerPlayer player) {
        ACEntity.super.removeBossBarPlayer(bossEvent, player);
        ACNetwork.sendToPlayer(player, new BossMusicPacket(LODTheme.STOP));
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

        if (this.getSleepingState() != SleepingState.AWAKE) return false;

        if (this.attackManager.getCurrentAction() instanceof LODSpawnArchaicsAction) return false;

        // Damage reduction based on Surviving Archaics
        int aliveArchaics = this.archaicSystem.countChargedArchaics();
        damage *= this.archaicSystem.getArchaicProtectionMultiplier(aliveArchaics);

        // Burst damage scalable reduction
        float threshold = this.getMaxHealth() * 0.04F;
        if (damage > threshold) {
            damage *= this.getAntiBurstMultiplier(damage, threshold);
        }

        // Avoid death when Archaics are still alive
        if (aliveArchaics > 0 && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            damage = Math.min(damage, this.getHealth() - 1.0F);
        }

        return super.hurtServer(level, source, damage);
    }

    public float getAntiBurstMultiplier(float damage, float threshold) {
        int players = Math.max(1, this.archaicSystem.countNearbyPlayers());
        float floor = Math.max(0.05F, 0.2F - 0.03F * (players - 1));
        float excess = damage - threshold;
        return floor + (1.0F - floor) / (1.0F + excess / threshold);
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean result = super.doHurtTarget(level, target);
        if (result && target instanceof LivingEntity living) {
            int phases = this.archaicSystem.getPhasesTriggered();
            int amplifier = phases >= 2 ? 1 : (phases == 1 ? 0 : -1);
            if (amplifier >= 0) {
                living.addEffect(new MobEffectInstance(ACEffects.ARMOR_BREAK, 200, amplifier));
            }
        }
        return result;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SLEEPING_STATE, SleepingState.SLEEPING);
        builder.define(HAS_CHARGED_ARCHAICS, false);
        builder.define(ECHO_CHARGES_FED, 0);
        builder.define(RAID_PHASE, 0);
    }

    public boolean hasChargedArchaics() {
        return this.entityData.get(HAS_CHARGED_ARCHAICS);
    }

    public int getEchoChargesFed() {
        return this.entityData.get(ECHO_CHARGES_FED);
    }

    public void setEchoChargesFed(int echoChargesFed) {
        this.entityData.set(ECHO_CHARGES_FED, echoChargesFed);
    }

    public void setHasChargedArchaics(boolean hasChargedArchaics) {
        this.entityData.set(HAS_CHARGED_ARCHAICS, hasChargedArchaics);
    }

    public int getPhase() {
        return this.entityData.get(RAID_PHASE);
    }

    public void setPhase(int phase) {
        this.entityData.set(RAID_PHASE, phase);
    }

    public SleepingState getSleepingState() {
        return this.entityData.get(SLEEPING_STATE);
    }

    public void setSleepingState(SleepingState state) {
        this.entityData.set(SLEEPING_STATE, state);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.getInt("sleepState").ifPresent(num -> {
            this.setSleepingState(SleepingState.values()[num]);
        });
        this.archaicSystem.load(input);
        this.setPhase(this.archaicSystem.getPhasesTriggered());
        this.setEchoChargesFed(input.getIntOr("echoChargesFed", 0));
        this.musicPhase = input.getBooleanOr("finalThemeStarted", false)
                ? LODTheme.PHASE_3
                : LODTheme.fromPhase(input.getIntOr("musicPhase", 1));

        if (!(attackManager.getCurrentAction() instanceof LODSpawnArchaicsAction)) attackManager.stopCurrentAction();
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("sleepState", this.getSleepingState().ordinal());
        this.archaicSystem.save(output);
        output.putInt("echoChargesFed", this.getEchoChargesFed());
        output.putInt("musicPhase", this.musicPhase.ordinal() + 1);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.getSleepingState() == SleepingState.SLEEPING && stack.getItem() instanceof EchoChargeItem) {
            stack.shrink(1);
            this.feedEchoCharge();
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    public void feedEchoCharge() {
        this.setEchoChargesFed(this.getEchoChargesFed() + 1);
        this.playSound(ACSounds.LOD_AMBIENT.get(), 3.0F, 1.0F);
        int requiredEchoCharges = 4;
        if (this.getEchoChargesFed() >= requiredEchoCharges) {
            this.beginWaking();
        }

        this.playSound(ACSounds.LOD_ECHO_CHARGE_INTERACT.get(), 3.0F, 1.0F);
        ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_feed_charge"));
        AAALevel.addParticle(level(), info.position(this.position().add(this.getDeltaMovement().scale(3)).add(0, 0.5, 0)).scale(1.75F));

    }

    private void beginWaking() {
        this.setSleepingState(SleepingState.WAKING);
        this.playSound(ACSounds.LOD_ACTIVATE.get(), 3.0F, 1.0F);
        this.wakingAnim.forceStart();
        this.wakingStartTick = this.tickCount;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        if (this.getSleepingState() != SleepingState.AWAKE) return null;
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
        return 7.5F;
    }
}
