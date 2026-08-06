package com.ratrod.archaion.registry;

import com.mojang.serialization.MapCodec;
import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.loot.AddLootTableModifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ACLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Archaion.MODID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<AddLootTableModifier>> ADD_LOOT_TABLE =
            GLOBAL_LOOT_MODIFIER_SERIALIZERS.register("add_loot_table", () -> AddLootTableModifier.CODEC);
}
