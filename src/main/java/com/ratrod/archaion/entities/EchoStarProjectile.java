package com.ratrod.archaion.entities;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.registry.ACEntityTypes;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EchoStarProjectile extends ThrowableProjectile {

    private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> HOMING_TARGET_ID = SynchedEntityData.defineId(EchoStarProjectile.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);

    private float powerBonus;

    public EchoStarProjectile(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public EchoStarProjectile(Level level, LivingEntity owner, ItemStack stack) {
        super(ACEntityTypes.ECHO_STAR.get(), level);
        this.snapTo(owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), owner.getYRot(), owner.getXRot());
        this.setOwner(owner);
    }

    @Override
    public void tick() {

        if (this.level().isClientSide()) {
            if (firstTick) {
                ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("echo_star"));
                AAALevel.addParticle(level(), info.bindOnEntity(this).position(0, 0.5, 0).scale(0.6F));
            }

            if (tickCount % 2 == 0) {
                ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_boom"));
                AAALevel.addParticle(level(), info.position(this.position().add(0, 0.5, 0)).scale(2F));
            }

            if (getOwner() == null) {
                this.discard();
                return;
            }
        }

        super.tick();

        if (tickCount >= 100) {
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        this.damageArea();
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        this.damageArea();
    }

    public void setPowerBonus(float powerBonus) {
        this.powerBonus = powerBonus;
    }

    public void damageArea() {
        if (!level().isClientSide()) {
            ServerLevel server = (ServerLevel) level();
            float damage = 25.0F + this.powerBonus;

            for (LivingEntity target : server.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3))) {
                if (target != this.getOwner() && !isOwnedChargedBrave(target)) {
                    target.hurtServer(server, server.damageSources().explosion(this, this.getOwner()), damage);
                }
            }

            this.playSound(ACSounds.ECHO_STAR_BLAST.get(), 4.0F, 1.0F);
            ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_boom_group"));
            AAALevel.addParticle(level(), true, info.position(this.position().add(0, 0.5, 0)).scale(1.5F));

            this.discard();
        }
    }

    private boolean isOwnedChargedBrave(LivingEntity target) {
        if (!(this.getOwner() instanceof LastOfDeepslateEntity lod)) return false;
        return target instanceof BraveEntity brave && lod.getUUID().equals(brave.getOwnerUUID());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(HOMING_TARGET_ID, Optional.empty());
    }

    public void setHomingTarget(@Nullable LivingEntity target) {
        this.entityData.set(HOMING_TARGET_ID, Optional.ofNullable(EntityReference.of(target)));
    }

    @Nullable
    public LivingEntity getHomingTarget() {
        return this.entityData.get(HOMING_TARGET_ID)
                .map(ref -> ref.getEntity(this.level(), LivingEntity.class))
                .orElse(null);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.entityData.set(HOMING_TARGET_ID, Optional.ofNullable(EntityReference.read(input, "HomingTarget")));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        this.entityData.get(HOMING_TARGET_ID).ifPresent(ref -> ref.store(output, "HomingTarget"));
    }
}
