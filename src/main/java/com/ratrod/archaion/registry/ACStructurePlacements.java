package com.ratrod.archaion.registry;

import com.ratrod.archaion.worldgen.placement.AvoidTrialChambersStructurePlacement;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ACStructurePlacements {
    public static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENT = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, "archaion");
    public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<AvoidTrialChambersStructurePlacement>> AVOID_TRIAL_CHAMBERS =
            STRUCTURE_PLACEMENT.register("avoid_trial_chambers", () -> () -> AvoidTrialChambersStructurePlacement.CODEC);

}
