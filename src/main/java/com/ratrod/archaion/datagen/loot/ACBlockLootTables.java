package com.ratrod.archaion.datagen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Set;

public class ACBlockLootTables extends BlockLootSubProvider {
    public ACBlockLootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
//        this.dropSelf(ACBlocks.SOUL_LAMP.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {

        return List.of();
//        return ACLootTableProvider.knownSet(BuiltInRegistries.BLOCK).stream()
//                .filter(block -> !excluded.contains(block))
//                .collect(Collectors.toSet());
    }
}
