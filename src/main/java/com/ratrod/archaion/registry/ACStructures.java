package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ACStructures {
    public static final ResourceKey<Structure> ANCIENT_KEEP = ResourceKey.create(Registries.STRUCTURE, Archaion.prefix("ancient_keep"));
}
