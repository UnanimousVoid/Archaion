package com.ratrod.archaion;

import com.ratrod.archaion.datagen.loot.ACLootTables;
import com.ratrod.archaion.entities.LastOfDeepslateEntity;
import com.ratrod.archaion.registry.ACBlocks;
import com.ratrod.archaion.registry.ACEntityTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.Optional;

@EventBusSubscriber(modid = Archaion.MODID)
public class ACCommonSetup {

    @SubscribeEvent
    static void onRegisterAttributes(EntityAttributeCreationEvent event) {
        event.put(ACEntityTypes.LAST_OF_DEEPSLATE.get(), LastOfDeepslateEntity.createAttributes().build());
    }

    @SubscribeEvent
    static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getPlacedBlock().is(ACBlocks.DEEPSLATE_VAULT.get())) {
            if (event.getLevel().getBlockEntity(event.getPos()) instanceof VaultBlockEntity vault) {
                vault.setConfig(new VaultConfig(ACLootTables.DEEPSLATE_VAULT, 4.0, 4.5, new ItemStack(Items.DIAMOND), Optional.empty()));
            }
        }
    }
}
