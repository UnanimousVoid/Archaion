package com.ratrod.archaion.api.trial;

import com.ratrod.archaion.registry.ACBlockEntities;
import com.ratrod.archaion.registry.ACBlocks;
import com.ratrod.archaion.registry.ACItems;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ACTrialRegistry {
    private static final List<TrialSpawnerVariant> SPAWNERS = new ArrayList<>();
    private static final List<TrialVaultVariant> VAULTS = new ArrayList<>();

    private static final List<DeferredBlock<TrialVaultBlock>> VAULT_DEFERRED = new ArrayList<>();
    private static Set<Block> vaultBlocksCache;

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ACTrialSpawnerBlockEntity>> TRIAL_SPAWNER = ACBlockEntities.BLOCK_ENTITIES.register("trial_spawner", () -> new BlockEntityType<>(ACTrialSpawnerBlockEntity::new, SPAWNERS.stream().map(v -> v.block().get()).collect(Collectors.toSet())));

    private ACTrialRegistry() {
    }

    public static TrialSpawnerVariant registerSpawner(String name, Function<BlockBehaviour.Properties, BlockBehaviour.Properties> properties) {
        DeferredBlock<ACTrialSpawnerBlock> block = ACBlocks.BLOCK.registerBlock(name, ACTrialSpawnerBlock::new, properties::apply);
        ACItems.ITEM.registerSimpleBlockItem(block);
        TrialSpawnerVariant variant = new TrialSpawnerVariant(name, block);
        SPAWNERS.add(variant);
        return variant;
    }

    public static TrialVaultVariant registerVault(String name, Function<BlockBehaviour.Properties, BlockBehaviour.Properties> properties, ResourceKey<LootTable> lootTable, DeferredItem<Item> keyItem, double activationRange, double deactivationRange) {
        DeferredBlock<TrialVaultBlock> block = ACBlocks.BLOCK.registerBlock(name, p -> new TrialVaultBlock(p, () -> new VaultConfig(lootTable, activationRange, deactivationRange, new ItemStack(keyItem.get()), Optional.empty())), properties::apply);
        ACItems.ITEM.registerSimpleBlockItem(block);
        VAULT_DEFERRED.add(block);
        TrialVaultVariant variant = new TrialVaultVariant(name, block, lootTable, keyItem, activationRange, deactivationRange);
        VAULTS.add(variant);
        return variant;
    }

    public static List<TrialSpawnerVariant> spawners() {
        return List.copyOf(SPAWNERS);
    }

    public static List<TrialVaultVariant> vaults() {
        return List.copyOf(VAULTS);
    }

    public static boolean isVaultBlock(Block block) {
        return vaultBlocks().contains(block);
    }

    public static Set<Block> vaultBlocks() {
        if (vaultBlocksCache == null) {
            Set<Block> blocks = new HashSet<>();
            for (DeferredBlock<TrialVaultBlock> deferred : VAULT_DEFERRED) {
                blocks.add(deferred.get());
            }
            vaultBlocksCache = Collections.unmodifiableSet(blocks);
        }
        return vaultBlocksCache;
    }
}
