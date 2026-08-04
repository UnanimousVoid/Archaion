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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EchoStarProjectile extends ThrowableProjectile {

    private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> HOMING_TARGET_ID = SynchedEntityData.defineId(EchoStarProjectile.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);

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

        LivingEntity target = this.getHomingTarget();

        if (target != null && target.isAlive()) {
            Vec3 toTarget = target.getEyePosition().subtract(this.position());
            double dist = toTarget.length();
            if (dist > 0) {
                Vec3 direction = toTarget.normalize();
                Vec3 newVelocity = this.getDeltaMovement().add(direction.scale(0.025));

                double newSpeed = newVelocity.length();

                double maxSpeed = 0.7;
                if (newSpeed > maxSpeed) {
                    newVelocity = newVelocity.normalize().scale(maxSpeed);
                }

                this.setDeltaMovement(newVelocity);
            }

            if (!this.level().isClientSide()) {
                if (this.getBoundingBox().intersects(target.getBoundingBox())) {
                    this.damageArea();
                }
            }
        }

        if (tickCount >= 100) {
            this.discard();
        }

        Vec3 delta = this.getDeltaMovement();
        this.setPos(this.getX() + delta.x, this.getY() + delta.y, this.getZ() + delta.z);
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

    public void damageArea() {
        if (!level().isClientSide()) {
            ServerLevel server = (ServerLevel) level();

            for (LivingEntity target : server.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2))) {
                if (target != this.getOwner()) {
                    target.hurtServer(server, server.damageSources().indirectMagic(this, getOwner()), 3);
                }
            }

            this.playSound(ACSounds.ECHO_STAR_BLAST.get(), 4.0F, 1.0F);
            ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_boom_group"));
            AAALevel.addParticle(level(), true, info.position(this.position().add(0, 0.5, 0)).scale(1.5F));

            this.discard();
        }
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
