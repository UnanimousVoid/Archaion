package com.ratrod.archaion;

import com.ratrod.archaion.registry.ACEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = Archaion.MODID)
public class ACCommonEvents {

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        MobEffectInstance ab = target.getEffect(ACEffects.ARMOR_BREAK);
        if (ab == null) return;

        float mult = 1.0F - 0.075F * (ab.getAmplifier() + 1);

        event.addReductionModifier(DamageContainer.Reduction.ENCHANTMENTS, (c, r) -> r * mult);
        event.addReductionModifier(DamageContainer.Reduction.MOB_EFFECTS, (c, r) -> r * mult);
    }
}
