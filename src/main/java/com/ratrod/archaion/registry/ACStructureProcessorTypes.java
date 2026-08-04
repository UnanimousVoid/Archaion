package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.worldgen.processors.DeepslateVaultProcessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ACStructureProcessorTypes {
    public static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSOR = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, Archaion.MODID);

    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<DeepslateVaultProcessor>> DEEPSLATE_VAULT = STRUCTURE_PROCESSOR.register("deepslate_vault", () -> () -> DeepslateVaultProcessor.CODEC);
}
