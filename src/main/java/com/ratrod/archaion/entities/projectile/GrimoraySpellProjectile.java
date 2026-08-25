package com.ratrod.archaion.entities.projectile;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.entities.Grimoray;
import com.ratrod.archaion.entities.GrimorayType;
import com.ratrod.archaion.registry.ACEntityDataSerializers;
import com.ratrod.archaion.registry.ACEntityTypes;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

public class GrimoraySpellProjectile extends ThrowableProjectile {

    public static final EntityDataAccessor<GrimorayType> GRIMORAY_TYPE = SynchedEntityData.defineId(GrimoraySpellProjectile.class, ACEntityDataSerializers.GRIMORAY_TYPE.get());

    public GrimoraySpellProjectile(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public GrimoraySpellProjectile(Level level, LivingEntity owner) {
        super(ACEntityTypes.GRIMORAY_SPELL.get(), level);
        this.setOwner(owner);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (this.level() instanceof ServerLevel serverLevel) {
            this.applyEffect(serverLevel, hitResult.getLocation());
        }
        this.discard();
    }

    private void applyEffect(ServerLevel serverLevel, Vec3 pos) {
        switch (this.getGrimorayType()) {
            case POISON_CLOUD -> {
                AreaEffectCloud cloud = new AreaEffectCloud(serverLevel, pos.x, pos.y, pos.z);
                if (this.getOwner() instanceof LivingEntity owner) {
                    cloud.setOwner(owner);
                }
                cloud.setRadius(3.0F);
                cloud.setDuration(100);
                cloud.setWaitTime(10);
                cloud.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
                serverLevel.addFreshEntity(cloud);
                this.playSound(SoundEvents.SQUID_DEATH, 1.5f, 1.0f);
            }
            case HARMING -> {
                int r = 2;
                AABB area = new AABB(pos.x - r, pos.y - r, pos.z - r, pos.x + r, pos.y + r, pos.z + r);
                for (LivingEntity target : serverLevel.getEntitiesOfClass(LivingEntity.class, area, e -> e != this.getOwner() && !(e instanceof Grimoray) && e.isAffectedByPotions())) {
                    MobEffects.HARM.value().applyInstantenousEffect(this, this.getOwner(), target, 0, 1.0);
                }
                this.playSound(SoundEvents.SQUID_DEATH, 1.5f, 0.8f);
            }
            case HEALING -> {
            }
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.05;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            if (this.getGrimorayType() == GrimorayType.POISON_CLOUD) {
                ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("grimoray_spell_poison"));
                AAALevel.addParticle(level(), info.position(position()).scale(0.15F));
            } else if (this.getGrimorayType() == GrimorayType.HARMING) {
                ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("grimoray_spell_harming"));
                AAALevel.addParticle(level(), info.position(position()).scale(0.15F));
            }
        }

        if (this.tickCount > 50) {
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(GRIMORAY_TYPE, GrimorayType.POISON_CLOUD);
    }

    public GrimorayType getGrimorayType() {
        return this.entityData.get(GRIMORAY_TYPE);
    }

    public void setGrimorayType(GrimorayType type) {
        this.entityData.set(GRIMORAY_TYPE, type);
    }
}
