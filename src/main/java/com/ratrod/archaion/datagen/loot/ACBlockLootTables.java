package com.ratrod.archaion.datagen.loot;

import com.ratrod.archaion.registry.ACBlocks;
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
        this.dropSelf(ACBlocks.SOUL_LAMP.get());
        this.dropSelf(ACBlocks.DEEPSLATE_PILLAR.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return List.of(ACBlocks.SOUL_LAMP.get(), ACBlocks.DEEPSLATE_PILLAR.get());
    }
}
