package com.ratrod.archaion.datagen.loot;

import com.ratrod.archaion.Archaion;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class ACLootTables {
    public static final ResourceKey<LootTable> DEEPSLATE_VAULT = ResourceKey.create(Registries.LOOT_TABLE, Archaion.prefix("chests/deepslate_vault"));
    public static final ResourceKey<LootTable> DEEPSLATE_SPAWNER = ResourceKey.create(Registries.LOOT_TABLE, Archaion.prefix("spawner/deepslate_spawner"));
}
