package com.ratrod.archaion.entities;

import com.ratrod.archaion.registry.ACEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.ExplodeEffect;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class ThrownEchoMace extends ThrowableProjectile {

    private static final EntityDataAccessor<ItemStack> DATA_MACE_STACK = SynchedEntityData.defineId(ThrownEchoMace.class, EntityDataSerializers.ITEM_STACK);

    public ThrownEchoMace(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownEchoMace(Level level, LivingEntity owner, ItemStack stack) {
        super(ACEntityTypes.THROWN_ECHO_MACE.get(), owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), level);
        this.setOwner(owner);
        this.entityData.set(DATA_MACE_STACK, stack.copyWithCount(1));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_MACE_STACK, ItemStack.EMPTY);
    }

    public ItemStack getThrownStack() {
        return this.entityData.get(DATA_MACE_STACK);
    }

    @Override
    public ItemStack getWeaponItem() {
        return this.getThrownStack();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            if (this.tickCount % 2 == 0) {
                this.level().addParticle(ParticleTypes.SCULK_SOUL, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        Entity target = hitResult.getEntity();
        Entity owner = this.getOwner();
        DamageSource source = this.damageSources().thrown(this, owner == null ? this : owner);
        if (this.level() instanceof ServerLevel serverLevel) {
            float damage = 8;
            damage += (float) this.tickCount * 0.5F;
            damage = EnchantmentHelper.modifyFallBasedDamage(serverLevel, this.getThrownStack(), target, source, damage);
            if (target.hurtServer(serverLevel, source, damage)) {
                if (target instanceof LivingEntity living) {
                    Vec3 delta = this.getDeltaMovement();
                    living.push(delta.x * 0.4, 0.3, delta.z * 0.4);
                }
                if (owner instanceof LivingEntity livingOwner) {
                    livingOwner.setIgnoreFallDamageFromCurrentImpulse(true, livingOwner.position());
                }
                EnchantmentHelper.doPostAttackEffectsWithItemSourceOnBreak(serverLevel, target, source, this.withoutWindBurst(), weapon -> {});
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.MACE_SMASH_AIR, SoundSource.PLAYERS, 2.0F, 1.2F);
                this.spawnSmashEffect(serverLevel, target.getOnPos(), 550, target);
            }
            this.setDeltaMovement(0, 1, 0);
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.fallDistance = this.tickCount;
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (this.level() instanceof ServerLevel serverLevel) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.MACE_SMASH_GROUND, SoundSource.PLAYERS, 2.0F, 1.0F);
            if (hitResult.getDirection() != Direction.DOWN) {
                this.spawnSmashEffect(serverLevel, hitResult.getBlockPos(), 750, null);
                this.discard();
            } else {
                this.spawnSmashEffect(serverLevel, hitResult.getBlockPos(), 200, null);
                this.setDeltaMovement(0, -1, 0);
            }
        }
    }

    private void spawnSmashEffect(ServerLevel serverLevel, BlockPos pos, int particleCount, @Nullable Entity center) {
        serverLevel.levelEvent(2013, pos, particleCount);
        this.applyWindBurst(serverLevel, pos, center);
    }

    private ItemStack withoutWindBurst() {
        ItemStack stack = this.getThrownStack();
        Holder<Enchantment> windBurst = this.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.WIND_BURST);
        ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (enchantments.getLevel(windBurst) <= 0) {
            return stack;
        }
        ItemStack copy = stack.copy();
        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(enchantments);
        mutable.set(windBurst, 0);
        copy.set(DataComponents.ENCHANTMENTS, mutable.toImmutable());
        return copy;
    }

    private void applyWindBurst(ServerLevel serverLevel, BlockPos pos, @Nullable Entity center) {
        ItemStack stack = this.getThrownStack();
        Holder<Enchantment> windBurst = serverLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.WIND_BURST);
        ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        int level = enchantments.getLevel(windBurst);
        if (level <= 0) {
            return;
        }

        Entity owner = this.getOwner();
        Vec3 centerPos = center != null ? center.position() : pos.getCenter();

        ExplodeEffect explodeEffect = null;
        for (TargetedConditionalEffect<EnchantmentEntityEffect> conditional : windBurst.value().getEffects(EnchantmentEffectComponents.POST_ATTACK)) {
            if (conditional.effect() instanceof ExplodeEffect effect) {
                explodeEffect = effect;
            }
        }
        if (explodeEffect == null) {
            return;
        }

        float radius = Math.max(explodeEffect.radius().calculate(level), 0.0F);
        float aoeDamage = 4.0F + level;
        Entity attacker = owner == null ? this : owner;

        ExplosionDamageCalculator damageCalculator = new SimpleExplosionDamageCalculator(
                explodeEffect.blockInteraction() != Level.ExplosionInteraction.NONE,
                explodeEffect.damageType().isPresent(),
                explodeEffect.knockbackMultiplier().map(value -> value.calculate(level)),
                explodeEffect.immuneBlocks()
        ) {
            @Override
            public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
                return entity != owner && (owner == null || !owner.isAlliedTo(entity));
            }

            @Override
            public float getEntityDamageAmount(Explosion explosion, Entity entity, float exposure) {
                return aoeDamage;
            }
        };

        serverLevel.explode(
                this,
                this.damageSources().thrown(this, attacker),
                damageCalculator,
                centerPos.x(), centerPos.y(), centerPos.z(),
                radius,
                explodeEffect.createFire(),
                explodeEffect.blockInteraction(),
                explodeEffect.smallParticle(),
                explodeEffect.largeParticle(),
                explodeEffect.blockParticles(),
                explodeEffect.sound()
        );
    }
}
