package com.ratrod.archaion;

import com.ratrod.archaion.datagen.loot.ACLootTables;
import com.ratrod.archaion.entities.DeepslateSentinelEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentTable;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;

@EventBusSubscriber(modid = Archaion.MODID)
public class ACCommonEvents {

    @SubscribeEvent
    static void equipSentinelPassengers(FinalizeSpawnEvent event) {
        if (event.getEntity() instanceof DeepslateSentinelEntity sentinel) {
            for (Entity passenger : sentinel.getPassengers()) {
                if (passenger instanceof Mob mob) {
                    mob.equip(new EquipmentTable(ACLootTables.EQUIPMENT_DEEPSLATE_SPAWNER_RANGED, 0.0F));
                }
            }
        }
    }
}
