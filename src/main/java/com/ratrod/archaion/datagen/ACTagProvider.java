package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.registry.ACBlocks;
import com.ratrod.archaion.registry.ACEntityTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.apache.commons.lang3.arch.Processor;

import java.util.concurrent.CompletableFuture;

public class ACTagProvider {
    public static class EntityTypesProvider extends EntityTypeTagsProvider {
        public EntityTypesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider, Archaion.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            this.tag(EntityTypeTags.DEFLECTS_PROJECTILES).add(ACEntityTypes.BRAVE.get());
        }
    }

    public static class BlocksProvider extends BlockTagsProvider {
        public BlocksProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider, Archaion.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            this.tag(BlockTags.WALLS).add(
                    ACBlocks.REINFORCED_DEEPSLATE_BRICK_WALL.get(),
                    ACBlocks.REINFORCED_DEEPSLATE_TILE_WALL.get(),
                    ACBlocks.REINFORCED_POLISHED_DEEPSLATE_WALL.get()
            );
        }
    }

    public static class ItemsProvider extends ItemTagsProvider {
        public ItemsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags) {
            super(output, lookupProvider, Archaion.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            this.tag(ItemTags.WALLS).add(
                    ACBlocks.REINFORCED_DEEPSLATE_BRICK_WALL.get().asItem(),
                    ACBlocks.REINFORCED_DEEPSLATE_TILE_WALL.get().asItem(),
                    ACBlocks.REINFORCED_POLISHED_DEEPSLATE_WALL.get().asItem()
            );
        }
    }
}
