package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ACTagProvider {
    public static class EntityTypesProvider extends EntityTypeTagsProvider {
        public EntityTypesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider, Archaion.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {

        }
    }
}
