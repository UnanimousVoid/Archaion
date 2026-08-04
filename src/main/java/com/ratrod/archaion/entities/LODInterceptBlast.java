package com.ratrod.archaion.entities;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.registry.ACEntityTypes;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LODInterceptBlast extends Entity {

    private int chainIndex = 1;
    @Nullable private Entity source;

    public LODInterceptBlast(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public static LODInterceptBlast create(ServerLevel level, Vec3 position, int chainIndex, @Nullable Entity source) {
        LODInterceptBlast blast = ACEntityTypes.LOD_INTERCEPT_BLAST.get().create(level, EntitySpawnReason.TRIGGERED);
        blast.snapTo(position.x, position.y, position.z, 0.0F, 0.0F);
        blast.chainIndex = chainIndex;
        blast.source = source;
        return blast;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }

    @Override
    public void tick() {

        if (firstTick) this.blast();

        super.tick();

        if (this.level().isClientSide()) return;
        ServerLevel serverLevel = (ServerLevel) this.level();
        Entity cause = this.source != null ? this.source : this;

        if (tickCount == 3) {
            if (this.chainIndex < 5) {
                serverLevel.addFreshEntity(LODInterceptBlast.create(serverLevel, this.position().add(0, 6, 0), this.chainIndex + 1, cause));
            }
            this.discard();
        }
    }

    public void blast() {
        if (this.level().isClientSide()) return;
        ServerLevel serverLevel = (ServerLevel) this.level();

        this.playSound(ACSounds.LOD_SHOOT.get(), 5.0F, 0.5F + random.nextFloat() * 0.2F);

        float sizeMult = 0.9F;
        ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("echo_blast_intercept"));
        AAALevel.addParticle(serverLevel, info.position(this.position()).rotation(0, random.nextFloat() * 90, 0).scale(3.0F * sizeMult));

        Entity cause = this.source != null ? this.source : this;
        AABB area = AABB.ofSize(this.position(), 14 * sizeMult, 14 * sizeMult, 14 * sizeMult);
        List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, area, e -> e != cause && e.isAlive() && canTarget(e));
        for (LivingEntity target : targets) {
            target.hurtServer(serverLevel, serverLevel.damageSources().explosion(cause, cause), 20.0F);
            Vec3 knockback = target.position().subtract(this.position()).normalize().scale(3.0).add(0, 0.35, 0);
            target.setDeltaMovement(target.getDeltaMovement().add(knockback));
            target.hurtMarked = true;
        }
    }

    private boolean canTarget(LivingEntity target) {
        if (this.source instanceof Mob mob) {
            return mob.canAttack(target);
        }
        return true;
    }
}
