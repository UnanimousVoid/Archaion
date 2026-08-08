package com.ratrod.archaion.client.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jspecify.annotations.Nullable;

public class EchosGracePull implements RangeSelectItemModelProperty {
    public static final MapCodec<EchosGracePull> MAP_CODEC = MapCodec.unit(new EchosGracePull());

    public float get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        LivingEntity entity = owner == null ? null : owner.asLivingEntity();
        if (entity != null && entity.getUseItem() == itemStack) {
            float maxDraw = EnchantmentHelper.modifyCrossbowChargingTime(itemStack, entity, 1.0F) * 20.0F;
            return UseDuration.useDuration(itemStack, entity) / maxDraw;
        } else {
            return 0.0F;
        }
    }

    public MapCodec<EchosGracePull> type() {
        return MAP_CODEC;
    }
}
