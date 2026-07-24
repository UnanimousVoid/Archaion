package com.ratrod.archaion.datagen.loot;

import com.ratrod.archaion.datagen.ACLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;
import java.util.stream.Collectors;

public class ACBlockLootTables extends BlockLootSubProvider {
    public ACBlockLootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }


    @Override
    public void generate() {

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        Set<Block> excluded = Set.of(

        );

        return ACLootTableProvider.knownSet(BuiltInRegistries.BLOCK).stream()
                .filter(block -> !excluded.contains(block))
                .collect(Collectors.toSet());
    }
}
