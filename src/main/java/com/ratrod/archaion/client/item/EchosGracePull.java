package com.ratrod.archaion.client.item;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

public class EchosGracePull implements ItemPropertyFunction {

    @Override
    public float call(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        if (entity != null && entity.getUseItem() == stack) {
            float maxDraw = EnchantmentHelper.modifyCrossbowChargingTime(stack, entity, 1.0F) * 20.0F;
            return Math.min(1.0F, (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / maxDraw);
        } else {
            return 0.0F;
        }
    }
}
