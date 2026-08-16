package com.ratrod.archaion.registry;

import com.ratrod.archaion.api.trial.ACTrialRegistry;
import com.ratrod.archaion.api.trial.ACTrialSpawnerBlock;
import com.ratrod.archaion.api.trial.TrialSpawnerVariant;
import com.ratrod.archaion.api.trial.TrialVaultVariant;
import com.ratrod.archaion.datagen.loot.ACLootTables;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

import java.util.List;

public final class ACTrialVariants {
    public static final TrialSpawnerVariant DEEPSLATE_SPAWNER = ACTrialRegistry.registerSpawner("deepslate_spawner", properties -> properties.mapColor(MapColor.DEEPSLATE).sound(SoundType.TRIAL_SPAWNER).strength(50.0F, 1200.0F).noOcclusion().lightLevel(state -> state.getValue(ACTrialSpawnerBlock.STATE).lightLevel()));
    public static final TrialVaultVariant DEEPSLATE_VAULT = ACTrialRegistry.registerVault("deepslate_vault", properties -> properties.mapColor(MapColor.DEEPSLATE).sound(SoundType.VAULT).strength(-1.0F, 3600000.0F).noOcclusion(), ACLootTables.DEEPSLATE_VAULT, ACItems.ECHO_KEY, 4.0, 4.5);

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
