package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;

public class ACTrialSpawnerConfigs {
    public static final ResourceKey<TrialSpawnerConfig> DEEPSLATE_SPAWNER_ZOMBIE = ResourceKey.create(Registries.TRIAL_SPAWNER_CONFIG, Archaion.prefix("deepslate_spawner_zombie"));
    public static final ResourceKey<TrialSpawnerConfig> DEEPSLATE_SPAWNER_SKELETON = ResourceKey.create(Registries.TRIAL_SPAWNER_CONFIG, Archaion.prefix("deepslate_spawner_skeleton"));
    public static final ResourceKey<TrialSpawnerConfig> DEEPSLATE_SPAWNER_BRAVE = ResourceKey.create(Registries.TRIAL_SPAWNER_CONFIG, Archaion.prefix("deepslate_spawner_brave"));

}
