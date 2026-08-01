package com.ratrod.archaion.registry;

import com.ratrod.archaion.worldgen.structures.AncientKeepStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ACStructureTypes {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE;
    public static DeferredHolder<StructureType<?>, StructureType<AncientKeepStructure>> ANCIENT_KEEP_STRUCTURE;

    static {
        STRUCTURE_TYPE = DeferredRegister.create(Registries.STRUCTURE_TYPE, "archaion");
        ANCIENT_KEEP_STRUCTURE = STRUCTURE_TYPE.register("ancient_keep", () -> () -> AncientKeepStructure.CODEC);
    }
}
