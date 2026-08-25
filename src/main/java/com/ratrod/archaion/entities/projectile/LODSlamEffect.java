package com.ratrod.archaion.entities.projectile;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.registry.ACEntityTypes;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LODSlamEffect extends Entity {

    @Nullable public Entity source;
    public Vec3 slamDirection;
    public int generation;
    private boolean damaged;

    public LODSlamEffect(EntityType<? extends LODSlamEffect> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void addAdditionalSaveData(CompoundTag output) {
    }

    @Override
    public void readAdditionalSaveData(CompoundTag input) {
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    public void tick() {
        if (this.level().isClientSide() && firstTick) {
            ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_smash_initial"));
            AAALevel.addParticle(level(), info.position(this.position().add(0, 0.5, 0)).scale(2.0F));
        }

        super.tick();

        if (this.level().isClientSide() || this.isRemoved()) return;

        if (this.tickCount == 2 && this.generation < 10) {
            this.spawnChild();
        }

        if (this.tickCount == 20) {
            this.damageArea();
            this.discard();
        }
    }

    public static void summonRing(ServerLevel serverLevel, Vec3 originPos, @Nullable Entity source) {
        int count = 12;

        double rotationOffset = serverLevel.getRandom().nextDouble() * Math.PI * 2.0;

        for (int i = 0; i < count; i++) {
            double angle = ((Math.PI * 2.0 * i) / count) + rotationOffset;

            double dx = Math.cos(angle);
            double dz = Math.sin(angle);

            Vec3 dir = new Vec3(dx, 0, dz);

            LODSlamEffect slam = ACEntityTypes.LOD_SLAM.get().create(serverLevel);

            slam.moveTo(originPos.add(dir.scale(4.0)));
            slam.slamDirection = dir;
            slam.generation = 1;
            slam.source = source;

            serverLevel.addFreshEntity(slam);
        }
    }

    private void spawnChild() {
        ServerLevel serverLevel = (ServerLevel) this.level();
        this.place(serverLevel, this.position().add(this.slamDirection.scale(4.0)), this.slamDirection, this.generation + 1);
    }

    private void place(ServerLevel serverLevel, Vec3 pos, Vec3 dir, int gen) {
        LODSlamEffect slam = ACEntityTypes.LOD_SLAM.get().create(serverLevel);
        slam.moveTo(pos);
        slam.slamDirection = dir;
        slam.generation = gen;
        slam.source = this.source;
        serverLevel.addFreshEntity(slam);
    }

    private void damageArea() {
        if (this.damaged) return;
        this.damaged = true;
        ServerLevel serverLevel = (ServerLevel) this.level();
        Entity cause = this.source != null ? this.source : this;
        this.playSound(ACSounds.LOD_SMASH.get(), 1.2F, 1.8F);
        AABB area = AABB.ofSize(this.position(), 3.0, 12.0, 3.0);
        List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, area, e -> e != cause && e.isAlive() && canTarget(e));
        for (LivingEntity target : targets) {
            target.hurt(serverLevel.damageSources().explosion(cause, cause), 30.0F);
        }
    }

    private boolean canTarget(LivingEntity target) {
        if (this.source instanceof Mob mob) {
            return mob.canAttack(target);
        }
        return true;
    }
}
