package com.ratrod.archaion.api.trial;

import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import net.neoforged.neoforge.registries.DeferredBlock;

public record TrialSpawnerVariant(
        String name,
        DeferredBlock<ACTrialSpawnerBlock> block,
        TrialSpawnerConfig config
) {}
