package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.entities.BraveEntity;
import com.ratrod.archaion.entities.BraveSpawnProjectile;
import com.ratrod.archaion.entities.DeepslateSentinelEntity;
import com.ratrod.archaion.entities.EchoStarProjectile;
import com.ratrod.archaion.entities.LastOfDeepslateEntity;
import com.ratrod.archaion.entities.LODInterceptBlast;
import com.ratrod.archaion.entities.Slated;
import com.ratrod.archaion.entities.ThrownEchoMace;
import com.ratrod.archaion.entities.ThrownImpactPearl;
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
            () -> EntityType.Builder.of(LastOfDeepslateEntity::new, MobCategory.MONSTER).sized(6.0F, 6.5F).eyeHeight(5).build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("last_of_deepslate"))));

    public static final DeferredHolder<EntityType<?>, EntityType<BraveEntity>> BRAVE = ENTITY_TYPE.register("brave",
            () -> EntityType.Builder.of(BraveEntity::new, MobCategory.MONSTER).sized(0.6F, 1.77F).eyeHeight(1.3452F).clientTrackingRange(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("brave"))));

    public static final DeferredHolder<EntityType<?>, EntityType<BraveSpawnProjectile>> BRAVE_SPAWN_PROJECTILE = ENTITY_TYPE.register("brave_spawn_projectile",
            () -> EntityType.Builder.<BraveSpawnProjectile>of(BraveSpawnProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .noSave()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("brave_spawn_projectile"))));

    public static final DeferredHolder<EntityType<?>, EntityType<Slated>> SLATED = ENTITY_TYPE.register("slated",
            () -> EntityType.Builder.of(Slated::new, MobCategory.MONSTER).sized(0.6F, 1.95F).eyeHeight(1.74F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(8).build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("slated"))));

    public static final DeferredHolder<EntityType<?>, EntityType<Wight>> WIGHT = ENTITY_TYPE.register("wight",
            () -> EntityType.Builder.of(Wight::new, MobCategory.MONSTER).sized(0.6F, 1.99F).eyeHeight(1.74F).ridingOffset(-0.7F).clientTrackingRange(8).build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("wight"))));

    public static final DeferredHolder<EntityType<?>, EntityType<DeepslateSentinelEntity>> DEEPSLATE_SENTINEL = ENTITY_TYPE.register("deepslate_sentinel",
            () -> EntityType.Builder.of(DeepslateSentinelEntity::new, MobCategory.MONSTER).sized(2.75F, 2.6F).eyeHeight(1.05F).nameTagOffset(2.05F).clientTrackingRange(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("deepslate_sentinel"))));

    public static final DeferredHolder<EntityType<?>, EntityType<EchoStarProjectile>> ECHO_STAR = ENTITY_TYPE.register("echo_star",
            () -> EntityType.Builder.<EchoStarProjectile>of(EchoStarProjectile::new, MobCategory.MISC).sized(1.5F, 1.5F).noSave().build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("echo_star"))));

    public static final DeferredHolder<EntityType<?>, EntityType<ThrownEchoMace>> THROWN_ECHO_MACE = ENTITY_TYPE.register("thrown_echo_mace",
            () -> EntityType.Builder.<ThrownEchoMace>of(ThrownEchoMace::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(8)
                    .noSave()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("thrown_echo_mace"))));

    public static final DeferredHolder<EntityType<?>, EntityType<ThrownImpactPearl>> THROWN_IMPACT_PEARL = ENTITY_TYPE.register("thrown_impact_pearl",
            () -> EntityType.Builder.<ThrownImpactPearl>of(ThrownImpactPearl::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(8)
                    .noSave()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("thrown_impact_pearl"))));

    public static final DeferredHolder<EntityType<?>, EntityType<LODInterceptBlast>> LOD_INTERCEPT_BLAST = ENTITY_TYPE.register("lod_intercept_blast",
            () -> EntityType.Builder.<LODInterceptBlast>of(LODInterceptBlast::new, MobCategory.MISC)
                    .sized(0.1F, 0.1F)
                    .noSave()
                    .noSummon()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("lod_intercept_blast"))));
}
