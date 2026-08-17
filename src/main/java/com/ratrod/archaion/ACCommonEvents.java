package com.ratrod.archaion;

import com.ratrod.archaion.entities.*;
import com.ratrod.archaion.registry.ACEffects;
import com.ratrod.archaion.registry.ACEntityTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = Archaion.MODID)
public class ACCommonEvents {

    @SubscribeEvent
    static void onRegisterAttributes(EntityAttributeCreationEvent event) {
        event.put(ACEntityTypes.LAST_OF_DEEPSLATE.get(), LastOfDeepslate.createAttributes().build());
        event.put(ACEntityTypes.BRAVE.get(), Brave.createAttributes().build());
        event.put(ACEntityTypes.SLATED.get(), Slated.createAttributes().build());
        event.put(ACEntityTypes.WIGHT.get(), Wight.createAttributes().build());
        event.put(ACEntityTypes.DEEPSLATE_SENTINEL.get(), DeepslateSentinel.createAttributes().build());
        event.put(ACEntityTypes.GRIMORAY.get(), Grimoray.createAttributes().build());
    }

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        MobEffectInstance ab = target.getEffect(ACEffects.ARMOR_BREAK);
        if (ab == null) return;

        float mult = 1.0F - 0.1F * (ab.getAmplifier() + 1);

        event.addReductionModifier(DamageContainer.Reduction.ENCHANTMENTS, (c, r) -> r * mult);
        event.addReductionModifier(DamageContainer.Reduction.MOB_EFFECTS, (c, r) -> r * mult);
    }
}
