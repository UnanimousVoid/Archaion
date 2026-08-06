package com.ratrod.archaion.datagen.loot;

import com.ratrod.archaion.Archaion;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class ACLootTables {
    public static final ResourceKey<LootTable> DEEPSLATE_VAULT = ResourceKey.create(Registries.LOOT_TABLE, Archaion.prefix("chests/deepslate_vault"));
    public static final ResourceKey<LootTable> DEEPSLATE_SPAWNER_MISC = ResourceKey.create(Registries.LOOT_TABLE, Archaion.prefix("spawner/deepslate_spawner_misc"));
    public static final ResourceKey<LootTable> DEEPSLATE_SPAWNER_KEY = ResourceKey.create(Registries.LOOT_TABLE, Archaion.prefix("spawner/deepslate_spawner_key"));
    public static final ResourceKey<LootTable> DEEPSLATE_SPAWNER_THROWABLES = ResourceKey.create(Registries.LOOT_TABLE, Archaion.prefix("spawner/deepslate_spawner_throwables"));

    public static final ResourceKey<LootTable> EQUIPMENT_DEEPSLATE_SPAWNER = ResourceKey.create(Registries.LOOT_TABLE, Archaion.prefix("equipment/deepslate_spawner"));
    public static final ResourceKey<LootTable> EQUIPMENT_DEEPSLATE_SPAWNER_RANGED = ResourceKey.create(Registries.LOOT_TABLE, Archaion.prefix("equipment/deepslate_spawner_ranged"));
    public static final ResourceKey<LootTable> EQUIPMENT_DEEPSLATE_SPAWNER_MELEE = ResourceKey.create(Registries.LOOT_TABLE, Archaion.prefix("equipment/deepslate_spawner_melee"));

    public static final ResourceKey<LootTable> ANCIENT_KEEP_MAP = ResourceKey.create(Registries.LOOT_TABLE, Archaion.prefix("chests/ancient_keep_map"));
}
