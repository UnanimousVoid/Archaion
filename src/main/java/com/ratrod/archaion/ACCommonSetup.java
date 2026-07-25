package com.ratrod.archaion;

import com.ratrod.archaion.entities.LastOfDeepslateEntity;
import com.ratrod.archaion.registry.ACEntityTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = Archaion.MODID)
public class ACCommonSetup {

    @SubscribeEvent
    static void onRegisterAttributes(EntityAttributeCreationEvent event) {
        event.put(ACEntityTypes.LAST_OF_DEEPSLATE.get(), LastOfDeepslateEntity.createAttributes().build());
    }
}
