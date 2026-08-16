package com.ratrod.archaion.entities.projectile;

import com.ratrod.archaion.registry.ACEntityTypes;
import com.ratrod.archaion.registry.ACItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ThrownImpactPearl extends ThrownEnderpearl {

    public ThrownImpactPearl(EntityType<? extends ThrownEnderpearl> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownImpactPearl(Level level, LivingEntity owner, ItemStack stack) {
        super(ACEntityTypes.THROWN_IMPACT_PEARL.get(), level);
        this.snapTo(owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), owner.getYRot(), owner.getXRot());
        this.setOwner(owner);
        this.setItem(stack);
    }

    @Override
    protected Item getDefaultItem() {
        return ACItems.IMPACT_PEARL.get();
    }

    public void impact(ServerLevel level) {
        this.playSound(SoundEvents.MACE_SMASH_GROUND, 2.0F, 1.2F);
        level.levelEvent(2013, this.getOnPos(), 750);

        Vec3 center = this.position();
        int radius = 4;
        AABB area = AABB.ofSize(center, radius * 2, 6, radius * 2);

        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area, e -> e != this.getOwner() && e.isAlive());

        for (LivingEntity target : targets) {
            float damage = this.getOwner() == null ? 5 : 5 + this.distanceTo(this.getOwner()) * 0.2F;
            if (target.hurtServer(level, level.damageSources().thrown(this, this.getOwner()), damage)) {
                Vec3 knockback = target.position().subtract(center).normalize().scale(1.8);
                target.setDeltaMovement(target.getDeltaMovement().add(knockback));
                target.hurtMarked = true;
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
    }


}
