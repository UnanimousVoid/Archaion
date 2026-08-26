package com.ratrod.archaion.registry;

import com.ratrod.archaion.api.trial.ACTrialRegistry;
import com.ratrod.archaion.api.trial.ACTrialSpawnerBlock;
import com.ratrod.archaion.api.trial.TrialSpawnerVariant;
import com.ratrod.archaion.api.trial.TrialVaultVariant;
import com.ratrod.archaion.datagen.loot.ACLootTables;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;

public final class ACTrialVariants {
    public static final TrialSpawnerVariant DEEPSLATE_SPAWNER = ACTrialRegistry.registerSpawner("deepslate_spawner", properties -> properties.mapColor(MapColor.DEEPSLATE).sound(SoundType.TRIAL_SPAWNER).strength(50.0F, 1200.0F).noOcclusion().lightLevel(state -> state.getValue(ACTrialSpawnerBlock.STATE).lightLevel()), deepslateSpawnerConfig());
    public static final TrialVaultVariant DEEPSLATE_VAULT = ACTrialRegistry.registerVault("deepslate_vault", properties -> properties.mapColor(MapColor.DEEPSLATE).sound(SoundType.VAULT).strength(-1.0F, 3600000.0F).noOcclusion(), ACLootTables.DEEPSLATE_VAULT, ACItems.ECHO_KEY, 4.0, 4.5);

    private static TrialSpawnerConfig deepslateSpawnerConfig() {
        return new TrialSpawnerConfig(
                4,
                6.0F,
                2.0F,
                1.0F,
                0.5F,
                100,
                SimpleWeightedRandomList.empty(),
                SimpleWeightedRandomList.<ResourceKey<LootTable>>builder()
                        .add(ACLootTables.DEEPSLATE_SPAWNER_MISC, 3)
                        .add(ACLootTables.DEEPSLATE_SPAWNER_KEY, 2)
                        .build(),
                ACLootTables.DEEPSLATE_SPAWNER_THROWABLES
        );
    }

    private ACTrialVariants() {
    }

    public static void initialize() {
    }

    public static List<TrialSpawnerVariant> spawners() {
        return ACTrialRegistry.spawners();
    }

    public static List<TrialVaultVariant> vaults() {
        return ACTrialRegistry.vaults();
    }

    public static boolean isVaultBlock(Block block) {
        return ACTrialRegistry.isVaultBlock(block);
    }
}
