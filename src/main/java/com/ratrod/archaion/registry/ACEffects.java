package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.effect.ArmorBreakEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ACEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECT = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Archaion.MODID);

    public static final DeferredHolder<MobEffect, MobEffect> ARMOR_BREAK = MOB_EFFECT.register("armor_break", () -> new ArmorBreakEffect(MobEffectCategory.HARMFUL, 0x4A4A4A));
}
