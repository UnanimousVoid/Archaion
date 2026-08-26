package com.ratrod.archaion;

import com.ratrod.archaion.entities.*;
import com.ratrod.archaion.registry.ACEntityTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = Archaion.MODID)
public class ACCommonSetup {

    @SubscribeEvent
    static void onRegisterAttributes(EntityAttributeCreationEvent event) {
        event.put(ACEntityTypes.LAST_OF_DEEPSLATE.get(), LastOfDeepslate.createAttributes().build());
        event.put(ACEntityTypes.BRAVE.get(), Brave.createAttributes().build());
        event.put(ACEntityTypes.SLATED.get(), Slated.createAttributes().build());
        event.put(ACEntityTypes.WIGHT.get(), Wight.createAttributes().build());
        event.put(ACEntityTypes.DEEPSLATE_SENTINEL.get(), DeepslateSentinel.createAttributes().build());
        event.put(ACEntityTypes.GRIMORAY.get(), Grimoray.createAttributes().build());
        event.put(ACEntityTypes.HAUNTER.get(), Haunter.createAttributes().build());
    }
}
