package com.ratrod.archaion.entities;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.registry.ACEntityTypes;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class BraveSpawnProjectile extends ThrowableProjectile {

    public BraveSpawnProjectile(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public BraveSpawnProjectile(Level level, LivingEntity owner) {
        super(ACEntityTypes.BRAVE_SPAWN_PROJECTILE.get(), level);
        this.setNoGravity(true);
        this.snapTo(owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), owner.getYRot(), owner.getXRot());
        this.setOwner(owner);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        if (this.level().isClientSide()) {
            if (firstTick) {
                ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("brave_spawn"));
                AAALevel.addParticle(level(), info.bindOnEntity(this).position(0, 0.5, 0).scale(1.0F));
            }
        }

        super.tick();

        if (this.tickCount >= 100) {
            this.spawnBrave();
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (hitResult.getDirection() == Direction.UP) {
            this.spawnBrave();
        } else {
            Vec3 velocity = this.getDeltaMovement();
            Vec3 normal = Vec3.atLowerCornerOf(hitResult.getDirection().getUnitVec3i());
            double approach = velocity.dot(normal);
            this.setDeltaMovement(velocity.subtract(normal.scale((1.5) * approach)));
            this.setPos(this.position().add(normal.scale(0.05)));
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        this.spawnBrave();
    }

    private void spawnBrave() {
        if (this.level().isClientSide() || this.isRemoved()) {
            return;
        }

        ServerLevel server = (ServerLevel) this.level();
        BraveEntity brave = ACEntityTypes.BRAVE.get().create(server, EntitySpawnReason.TRIGGERED);
        brave.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
        brave.setCharged(true);
        if (this.getOwner() instanceof LivingEntity owner) {
            brave.setOwnerUUID(owner.getUUID());
        }
        server.addFreshEntity(brave);

        this.playSound(ACSounds.ECHO_STAR_BLAST.get(), 4.0F, 1.0F);
        ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("brave_spawn_blast"));
        AAALevel.addParticle(level(), true, info.position(this.position().add(0, 1, 0)).scale(1.5F));

        this.discard();
    }
}
