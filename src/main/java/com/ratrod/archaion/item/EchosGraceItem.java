package com.ratrod.archaion.item;

import com.ratrod.archaion.entities.EchoStarProjectile;
import com.ratrod.archaion.registry.ACItems;
import com.ratrod.archaion.registry.ACSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EchosGraceItem extends ProjectileWeaponItem {
    public static final int MAX_DRAW_DURATION = 20;
    public static final int DEFAULT_RANGE = 15;

    public EchosGraceItem(Item.Properties properties) {
        super(properties);
    }

    public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
        if (entity instanceof Player player) {
            int timeHeld = this.getUseDuration(itemStack, entity) - remainingTime;
            if (timeHeld < 0) {
                return false;
            } else {
                float pow = getPowerForTime(timeHeld, itemStack, entity);
                if ((double)pow < 0.1) {
                    return false;
                } else {
                    List<ItemStack> projectiles = new ArrayList<>();
                    int count = 1;
                    if (level instanceof ServerLevel serverLevel) {
                        count = EnchantmentHelper.processProjectileCount(serverLevel, itemStack, player, 1);
                    }
                    ItemStack charge = ACItems.ECHO_CHARGE.get().getDefaultInstance();
                    for (int i = 0; i < count; i++) {
                        projectiles.add(charge.copy());
                    }

                    if (level instanceof ServerLevel serverLevel) {
                        this.shoot(serverLevel, player, player.getUsedItemHand(), itemStack, projectiles, pow * 2.0F, 1.0F, pow == 1.0F, null);
                    }

                    level.playSound(null, player.getX(), player.getY(), player.getZ(), ACSounds.LOD_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + pow * 0.5F);
                    player.awardStat(Stats.ITEM_USED.get(this));
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    protected void shootProjectile(LivingEntity shooter, Projectile projectileEntity, int index, float power, float uncertainty, float angle, @Nullable LivingEntity targetOverrride) {
        projectileEntity.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + angle, 0.0F, power, uncertainty);
    }

    public static float getPowerForTime(int timeHeld, ItemStack itemStack, LivingEntity holder) {
        float maxDraw = EnchantmentHelper.modifyCrossbowChargingTime(itemStack, holder, 1.0F) * 20.0F;
        float pow = (float)timeHeld / maxDraw;
        pow = (pow * pow + pow * 2.0F) / 3.0F;
        if (pow > 1.0F) {
            pow = 1.0F;
        }

        return pow;
    }

    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        return 72000;
    }

    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.BOW;
    }

    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return itemStack -> itemStack.is(ACItems.ECHO_CHARGE.get());
    }

    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack projectile, boolean isCrit) {
        EchoStarProjectile echoStar = new EchoStarProjectile(level, shooter, projectile);
        Holder<Enchantment> power = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.POWER);
        int powerLevel = weapon.getEnchantmentLevel(power);
        echoStar.setBaseDamage(12.0F);
        echoStar.setPowerBonus(powerLevel);
        return echoStar;
    }

    @Override
    public ItemStack getDefaultCreativeAmmo(@Nullable Player player, ItemStack projectileWeaponItem) {
        return ACItems.ECHO_CHARGE.get().getDefaultInstance();
    }
}
