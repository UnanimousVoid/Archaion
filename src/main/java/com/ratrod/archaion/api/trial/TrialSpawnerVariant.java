package com.ratrod.archaion.api.trial;

import net.neoforged.neoforge.registries.DeferredBlock;

/**
 * A registered trial-spawner variant: a block. All variants share the single block-entity type
 * {@link ACTrialRegistry#TRIAL_SPAWNER}. Trial spawner configs are data-driven and independent of the block,
 * so they are registered separately (see the trial-spawner-config datagen provider).
 */
public record TrialSpawnerVariant(
        String name,
        DeferredBlock<ACTrialSpawnerBlock> block
) {}
