package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.entities.*;
import com.ratrod.archaion.entities.projectile.EchoStarProjectile;
import com.ratrod.archaion.entities.projectile.GrimoraySpellProjectile;
import com.ratrod.archaion.entities.projectile.LODInterceptBlast;
import com.ratrod.archaion.entities.projectile.LODSlamEffect;
import com.ratrod.archaion.entities.projectile.ThrownEchoMace;
import com.ratrod.archaion.entities.projectile.ThrownImpactPearl;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ACEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Archaion.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<LastOfDeepslate>> LAST_OF_DEEPSLATE = ENTITY_TYPE.register("last_of_deepslate",
            () -> EntityType.Builder.of(LastOfDeepslate::new, MobCategory.MONSTER).sized(6.0F, 6.5F).eyeHeight(5).build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("last_of_deepslate"))));

    public static final DeferredHolder<EntityType<?>, EntityType<Brave>> BRAVE = ENTITY_TYPE.register("brave",
            () -> EntityType.Builder.of(Brave::new, MobCategory.MONSTER).sized(0.6F, 1.77F).eyeHeight(1.3452F).clientTrackingRange(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("brave"))));

    public static final DeferredHolder<EntityType<?>, EntityType<Slated>> SLATED = ENTITY_TYPE.register("slated",
            () -> EntityType.Builder.of(Slated::new, MobCategory.MONSTER).sized(0.6F, 1.95F).eyeHeight(1.74F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(8).build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("slated"))));

    public static final DeferredHolder<EntityType<?>, EntityType<Wight>> WIGHT = ENTITY_TYPE.register("wight",
            () -> EntityType.Builder.of(Wight::new, MobCategory.MONSTER).sized(0.6F, 1.99F).eyeHeight(1.74F).ridingOffset(-0.7F).clientTrackingRange(8).build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("wight"))));

    public static final DeferredHolder<EntityType<?>, EntityType<Grimoray>> GRIMORAY = ENTITY_TYPE.register("grimoray",
            () -> EntityType.Builder.of(Grimoray::new, MobCategory.MONSTER).sized(0.9F, 1.0F).eyeHeight(1.6F).clientTrackingRange(8).build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("grimoray"))));

    public static final DeferredHolder<EntityType<?>, EntityType<DeepslateSentinel>> DEEPSLATE_SENTINEL = ENTITY_TYPE.register("deepslate_sentinel",
            () -> EntityType.Builder.of(DeepslateSentinel::new, MobCategory.MONSTER).sized(2.75F, 2.6F).eyeHeight(1.05F).nameTagOffset(2.05F).clientTrackingRange(10).build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("deepslate_sentinel"))));

    public static final DeferredHolder<EntityType<?>, EntityType<GrimoraySpellProjectile>> GRIMORAY_SPELL = ENTITY_TYPE.register("grimoray_spell",
            () -> EntityType.Builder.<GrimoraySpellProjectile>of(GrimoraySpellProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(8)
                    .noSave()
                    .noSummon()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("grimoray_spell"))));

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

    public static final DeferredHolder<EntityType<?>, EntityType<LODSlamEffect>> LOD_SLAM = ENTITY_TYPE.register("lod_slam",
            () -> EntityType.Builder.<LODSlamEffect>of(LODSlamEffect::new, MobCategory.MISC)
                    .sized(0.1F, 0.1F)
                    .noSave()
                    .noSummon()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("lod_slam"))));

    public static final DeferredHolder<EntityType<?>, EntityType<LODFallingBlock>> LOD_FALLING_BLOCK = ENTITY_TYPE.register("lod_falling_block",
            () -> EntityType.Builder.<LODFallingBlock>of(LODFallingBlock::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(8)
                    .noSave()
                    .noSummon()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Archaion.prefix("lod_falling_block"))));
}
