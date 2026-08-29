package com.ratrod.archaion.entities.projectile;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.entities.LastOfDeepslate;
import com.ratrod.archaion.entities.ai.SleepingState;
import com.ratrod.archaion.registry.ACEntityTypes;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EchoStarProjectile extends ThrowableProjectile {

    private float baseDamage = 20.0F;
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
        if (!level().isClientSide()) {
            this.damageArea();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        if (!level().isClientSide()) {
            if (hitResult.getEntity() instanceof LastOfDeepslate lod) {
                if (lod.getSleepingState() == SleepingState.SLEEPING) {
                    lod.feedEchoCharge();
                    this.discard();
                    return;
                }
            }
            this.damageArea();
        }
    }

    public void setPowerBonus(float powerBonus) {
        this.powerBonus = powerBonus;
    }

    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }

    public void damageArea() {
        ServerLevel server = (ServerLevel) level();
        float damage = this.baseDamage + this.powerBonus;

        for (LivingEntity target : server.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3))) {
            if (target != this.getOwner() && canHurt(target)) {
                target.hurtServer(server, server.damageSources().explosion(this, this.getOwner()), damage);
            }
        }

        this.playSound(ACSounds.ECHO_STAR_BLAST.get(), 4.0F, 1.0F);
        ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_boom_group"));
        AAALevel.addParticle(level(), true, info.position(this.position().add(0, 0.5, 0)).scale(1.5F));

        this.discard();
    }

    private boolean canHurt(LivingEntity target) {
        if (!(this.getOwner() instanceof LastOfDeepslate lod)) return true;
        return lod.canAttack(target);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected double getDefaultGravity() {
        return 0.02;
    }
}
