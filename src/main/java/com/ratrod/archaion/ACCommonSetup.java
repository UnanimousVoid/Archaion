package com.ratrod.archaion;

import com.ratrod.archaion.entities.BraveEntity;
import com.ratrod.archaion.entities.DeepslateSentinelEntity;
import com.ratrod.archaion.entities.LastOfDeepslateEntity;
import com.ratrod.archaion.entities.Slated;
import com.ratrod.archaion.entities.Wight;
import com.ratrod.archaion.registry.ACEntityTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = Archaion.MODID)
public class ACCommonSetup {

    @SubscribeEvent
    static void onRegisterAttributes(EntityAttributeCreationEvent event) {
        event.put(ACEntityTypes.LAST_OF_DEEPSLATE.get(), LastOfDeepslateEntity.createAttributes().build());
        event.put(ACEntityTypes.BRAVE.get(), BraveEntity.createAttributes().build());
        event.put(ACEntityTypes.SLATED.get(), Slated.createAttributes().build());
        event.put(ACEntityTypes.WIGHT.get(), Wight.createAttributes().build());
        event.put(ACEntityTypes.DEEPSLATE_SENTINEL.get(), DeepslateSentinelEntity.createAttributes().build());
    }
}
