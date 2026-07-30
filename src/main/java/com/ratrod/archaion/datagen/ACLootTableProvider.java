package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.datagen.loot.ACBlockLootTables;
import com.ratrod.archaion.datagen.loot.ACChestLootTables;
import com.ratrod.archaion.datagen.loot.ACEntityLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ACLootTableProvider extends LootTableProvider {
    public ACLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(ACBlockLootTables::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(ACEntityLootTables::new, LootContextParamSets.ENTITY),
                new SubProviderEntry(ACChestLootTables::new, LootContextParamSets.CHEST)
        ), registries);
    }

    public static <T> Set<T> knownSet(Registry<T> registry) {
        return registry.stream()
                .filter(entry -> Optional.ofNullable(registry.getKey(entry))
                        .filter(key -> key.getNamespace().equals(Archaion.MODID))
                        .isPresent())
                .collect(Collectors.toSet());
    }
}
