package com.ratrod.archaion.datagen;

import com.ratrod.archaion.datagen.loot.ACLootTables;
import com.ratrod.archaion.registry.ACEntityTypes;
import com.ratrod.archaion.registry.ACTrialSpawnerConfigs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentTable;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public class ACTrialSpawnerConfigProvider {

    public static void bootstrap(BootstrapContext<TrialSpawnerConfig> ctx) {
        ctx.register(ACTrialSpawnerConfigs.DEEPSLATE_SPAWNER_ZOMBIE, TrialSpawnerConfig.builder()
                .spawnRange(4)
                .totalMobs(8.0F)
                .simultaneousMobs(2.0F)
                .totalMobsAddedPerPlayer(1)
                .simultaneousMobsAddedPerPlayer(0.5F)
                .ticksBetweenSpawn(150)
                .spawnPotentialsDefinition(
                        WeightedList.<SpawnData>builder()
                                .add(spawnDataWithEquipment(ACEntityTypes.SLATED.get(), ACLootTables.EQUIPMENT_DEEPSLATE_SPAWNER_MELEE), 2)
                                .build()
                )
                .lootTablesToEject(
                        WeightedList.<ResourceKey<LootTable>>builder()
                                .add(ACLootTables.DEEPSLATE_SPAWNER_KEY, 2)
                                .add(ACLootTables.DEEPSLATE_SPAWNER_MISC, 2)
                                .build()
                )
                .itemsToDropWhenOminous(ACLootTables.DEEPSLATE_SPAWNER_THROWABLES)
                .build());

        ctx.register(ACTrialSpawnerConfigs.DEEPSLATE_SPAWNER_SKELETON, TrialSpawnerConfig.builder()
                .spawnRange(4)
                .totalMobs(8.0F)
                .simultaneousMobs(2.0F)
                .totalMobsAddedPerPlayer(1)
                .simultaneousMobsAddedPerPlayer(0.5F)
                .ticksBetweenSpawn(150)
                .spawnPotentialsDefinition(
                        WeightedList.<SpawnData>builder()
                                .add(spawnDataWithEquipment(ACEntityTypes.WIGHT.get(), ACLootTables.EQUIPMENT_DEEPSLATE_SPAWNER_RANGED), 2)
                                .add(spawnDataWithEquipment(EntityType.STRAY, ACLootTables.EQUIPMENT_DEEPSLATE_SPAWNER_RANGED), 1)
                                .build()
                )
                .lootTablesToEject(
                        WeightedList.<ResourceKey<LootTable>>builder()
                                .add(ACLootTables.DEEPSLATE_SPAWNER_KEY, 1)
                                .add(ACLootTables.DEEPSLATE_SPAWNER_MISC, 2)
                                .build()
                )
                .itemsToDropWhenOminous(ACLootTables.DEEPSLATE_SPAWNER_THROWABLES)
                .build());

        ctx.register(ACTrialSpawnerConfigs.DEEPSLATE_SPAWNER_BRAVE, TrialSpawnerConfig.builder()
                .spawnRange(4)
                .totalMobs(8.0F)
                .simultaneousMobs(2.0F)
                .totalMobsAddedPerPlayer(1)
                .simultaneousMobsAddedPerPlayer(0.5F)
                .ticksBetweenSpawn(150)
                .spawnPotentialsDefinition(
                        WeightedList.<SpawnData>builder()
                                .add(spawnData(ACEntityTypes.BRAVE.get()))
                                .build()
                )
                .lootTablesToEject(
                        WeightedList.<ResourceKey<LootTable>>builder()
                                .add(ACLootTables.DEEPSLATE_SPAWNER_KEY, 1)
                                .add(ACLootTables.DEEPSLATE_SPAWNER_MISC, 2)
                                .build()
                )
                .itemsToDropWhenOminous(ACLootTables.DEEPSLATE_SPAWNER_THROWABLES)
                .build());

        ctx.register(ACTrialSpawnerConfigs.DEEPSLATE_SPAWNER_SENTINEL, TrialSpawnerConfig.builder()
                .spawnRange(4)
                .totalMobs(2.0F)
                .simultaneousMobs(1.0F)
                .totalMobsAddedPerPlayer(1)
                .simultaneousMobsAddedPerPlayer(0.0F)
                .ticksBetweenSpawn(300)
                .spawnPotentialsDefinition(
                        WeightedList.<SpawnData>builder()
                                .add(spawnDataSentinelWithWightPassengers(1), 3)
                                .add(spawnDataSentinelWithWightPassengers(2), 2)
                                .build()
                )
                .lootTablesToEject(
                        WeightedList.<ResourceKey<LootTable>>builder()
                                .add(ACLootTables.DEEPSLATE_SPAWNER_KEY, 1)
                                .add(ACLootTables.DEEPSLATE_SPAWNER_MISC, 2)
                                .build()
                )
                .itemsToDropWhenOminous(ACLootTables.DEEPSLATE_SPAWNER_THROWABLES)
                .build());
    }

    private static SpawnData spawnDataSentinelWithWightPassengers(int wightCount) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(ACEntityTypes.DEEPSLATE_SENTINEL.get()).toString());
        ListTag passengers = new ListTag();
        for (int i = 0; i < wightCount; i++) {
            CompoundTag passenger = new CompoundTag();
            passenger.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(ACEntityTypes.WIGHT.get()).toString());
            passengers.add(passenger);
        }
        tag.put("Passengers", passengers);
        return new SpawnData(tag, Optional.empty(), Optional.empty());
    }

    private static <T extends Entity> SpawnData spawnData(EntityType<T> type) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(type).toString());
        return new SpawnData(tag, Optional.empty(), Optional.empty());
    }

    private static <T extends Entity> SpawnData spawnDataWithEquipment(EntityType<T> type, ResourceKey<LootTable> equipmentLootTable) {
        return customSpawnDataWithEquipment(type, tag -> {}, equipmentLootTable);
    }

    private static <T extends Entity> SpawnData customSpawnDataWithEquipment(EntityType<T> type, Consumer<CompoundTag> tagModifier, @Nullable ResourceKey<LootTable> equipmentLootTable) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(type).toString());
        tagModifier.accept(tag);
        Optional<EquipmentTable> table = Optional.ofNullable(equipmentLootTable).map(lootTable -> new EquipmentTable(lootTable, 0.0F));
        return new SpawnData(tag, Optional.empty(), table);
    }
}
