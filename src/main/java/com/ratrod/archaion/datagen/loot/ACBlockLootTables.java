package com.ratrod.archaion.datagen.loot;

import com.ratrod.archaion.datagen.ACLootTableProvider;
import com.ratrod.archaion.registry.ACBlocks;
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
        this.dropSelf(ACBlocks.SOUL_LAMP.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        Set<Block> excluded = Set.of(
                ACBlocks.REINFORCED_POLISHED_DEEPSLATE.get(),
                ACBlocks.REINFORCED_DEEPSLATE_BRICKS.get(),
                ACBlocks.REINFORCED_DEEPSLATE_TILES.get(),
                ACBlocks.REINFORCED_DEEPSLATE_PILLAR.get(),
                ACBlocks.DEEPSLATE_PILLAR.get(),
                ACBlocks.REINFORCED_BARS.get(),
                ACBlocks.REINFORCED_DEEPSLATE_BRICK_STAIRS.get(),
                ACBlocks.REINFORCED_DEEPSLATE_TILE_STAIRS.get(),
                ACBlocks.REINFORCED_POLISHED_DEEPSLATE_STAIRS.get(),
                ACBlocks.REINFORCED_DEEPSLATE_BRICK_SLAB.get(),
                ACBlocks.REINFORCED_DEEPSLATE_TILE_SLAB.get(),
                ACBlocks.REINFORCED_POLISHED_DEEPSLATE_SLAB.get(),
                ACBlocks.REINFORCED_DEEPSLATE_BRICK_WALL.get(),
                ACBlocks.REINFORCED_DEEPSLATE_TILE_WALL.get(),
                ACBlocks.REINFORCED_POLISHED_DEEPSLATE_WALL.get()
        );

        return ACLootTableProvider.knownSet(BuiltInRegistries.BLOCK).stream()
                .filter(block -> !excluded.contains(block))
                .collect(Collectors.toSet());
    }
}
