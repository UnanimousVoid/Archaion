package com.ratrod.archaion.api.trial;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public record TrialVaultVariant(
        String name,
        DeferredBlock<TrialVaultBlock> block,
        ResourceKey<LootTable> lootTable,
        DeferredItem<Item> keyItem,
        double activationRange,
        double deactivationRange
) {}
