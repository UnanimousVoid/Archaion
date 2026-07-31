package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.block.DeepslateTrialSpawnerBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public class ACBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Archaion.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DeepslateTrialSpawnerBlockEntity>> DEEPSLATE_TRIAL_SPAWNER =
        BLOCK_ENTITIES.register("deepslate_trial_spawner",
            () -> new BlockEntityType<>(
                DeepslateTrialSpawnerBlockEntity::new,
                Set.of(ACBlocks.DEEPSLATE_SPAWNER.get())
            ));
}
