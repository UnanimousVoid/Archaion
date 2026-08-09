package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.registry.ACBlocks;
import com.ratrod.archaion.registry.ACItems;
import com.ratrod.archaion.registry.ACStructures;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ACTagProvider {
    public static class StructuresProvider extends StructureTagsProvider {
        public StructuresProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider, Archaion.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            // ancient_keep is a handwritten pack JSON, not part of the datagen registry lookup
            this.tag(ACStructures.ON_ANCIENT_KEEP_MAPS).addOptional(ACStructures.ANCIENT_KEEP);
        }
    }

    public static class EntityTypesProvider extends EntityTypeTagsProvider {
        public EntityTypesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider, Archaion.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            // Brave deflects only while charged — handled in BraveEntity.deflection(), no tag needed
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

            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                    ACBlocks.SOUL_LAMP.get(),
                    ACBlocks.DEEPSLATE_PILLAR.get()
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

            this.tag(ItemTags.BREAKS_DECORATED_POTS).add(
                    ACItems.ECHO_MACE.get()
            );

            this.tag(ItemTags.FIRE_ASPECT_ENCHANTABLE).add(
                    ACItems.ECHO_MACE.get()
            );

            this.tag(ItemTags.WEAPON_ENCHANTABLE).add(
                    ACItems.ECHO_MACE.get()
            );

            this.tag(ItemTags.MACE_ENCHANTABLE).add(
                    ACItems.ECHO_MACE.get()
            );

            this.tag(ItemTags.BOW_ENCHANTABLE).add(
                    ACItems.ECHOS_GRACE.get()
            );

            this.tag(ItemTags.CROSSBOW_ENCHANTABLE).add(
                    ACItems.ECHOS_GRACE.get()
            );
        }
    }
}
