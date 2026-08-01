package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.entities.EchoStarProjectile;
import com.ratrod.archaion.entities.LastOfDeepslateEntity;
import com.ratrod.archaion.entities.Slated;
import com.ratrod.archaion.entities.Wight;
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

    public static final DeferredHolder<EntityType<?>, EntityType<Slated>> SLATED = ENTITY_TYPE.register("slated",
            () -> EntityType.Builder.of(Slated::new, MobCategory.MONSTER).sized(0.6F, 1.95F).eyeHeight(1.74F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(8).build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("slated"))));

    public static final DeferredHolder<EntityType<?>, EntityType<Wight>> WIGHT = ENTITY_TYPE.register("wight",
            () -> EntityType.Builder.of(Wight::new, MobCategory.MONSTER).sized(0.6F, 1.99F).eyeHeight(1.74F).ridingOffset(-0.7F).clientTrackingRange(8).build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("wight"))));

    public static final DeferredHolder<EntityType<?>, EntityType<EchoStarProjectile>> ECHO_STAR = ENTITY_TYPE.register("echo_star",
            () -> EntityType.Builder.<EchoStarProjectile>of(EchoStarProjectile::new, MobCategory.MISC).sized(1.5F, 1.5F).noSave().build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("echo_star"))));
}
