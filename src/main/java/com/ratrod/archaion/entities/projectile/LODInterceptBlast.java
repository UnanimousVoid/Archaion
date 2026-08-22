package com.ratrod.archaion.entities.projectile;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.registry.ACSounds;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LODInterceptBlast extends ThrowableProjectile {

    @Nullable public Entity source;

    public LODInterceptBlast(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide() || this.isRemoved()) return;

        if (this.tickCount % 2 == 0) {
            this.blast();
        }

        if (this.tickCount >= 100) {
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (!this.level().isClientSide()) {
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
    }

    public void blast() {
        if (this.level().isClientSide()) return;
        ServerLevel serverLevel = (ServerLevel) this.level();

        this.playSound(ACSounds.ECHO_STAR_BLAST.get(), 5.0F, 0.5F + random.nextFloat() * 0.2F);

        float sizeMult = 1.0F;
        ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("echo_blast_intercept"));
        AAALevel.addParticle(serverLevel, true, info.position(this.position()).rotation(0, random.nextFloat() * 90, 0).scale(3.0F * sizeMult));

        Entity cause = this.source != null ? this.source : this;
        AABB area = AABB.ofSize(this.position(), 14 * sizeMult, 14 * sizeMult, 14 * sizeMult);
        List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, area, e -> e != cause && e.isAlive() && canTarget(e));
        for (LivingEntity target : targets) {
            target.hurtServer(serverLevel, serverLevel.damageSources().explosion(cause, cause), 55.0F);
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
