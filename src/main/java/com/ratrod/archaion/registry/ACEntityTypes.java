package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.entities.LastOfDeepslateEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ACEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Archaion.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<LastOfDeepslateEntity>> LAST_OF_DEEPSLATE = ENTITY_TYPE.register("last_of_deepslate",
            () -> EntityType.Builder.of(LastOfDeepslateEntity::new, MobCategory.MONSTER).sized(6.0F, 6.5F).build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("last_of_deepslate"))));
}
