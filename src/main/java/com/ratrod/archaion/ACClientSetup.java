package com.ratrod.archaion;

import com.ratrod.archaion.client.animations.BraveAnimations;
import com.ratrod.archaion.client.animations.ClientAnimationRegistry;
import com.ratrod.archaion.client.animations.DeepslateSentinelAnimations;
import com.ratrod.archaion.client.animations.LastOfDeepslateAnimations;
import com.ratrod.archaion.client.item.EchosGracePull;
import com.ratrod.archaion.client.models.BraveModel;
import com.ratrod.archaion.client.models.DeepslateSentinelModel;
import com.ratrod.archaion.client.models.LastOfDeepslateModel;
import com.ratrod.archaion.client.renderers.*;
import com.ratrod.archaion.registry.ACBlockEntities;
import com.ratrod.archaion.registry.ACEntityTypes;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.List;

@EventBusSubscriber(modid = Archaion.MODID, value = Dist.CLIENT)
public class ACClientSetup {

    public ACClientSetup(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {

        ClientAnimationRegistry.register(ACEntityTypes.LAST_OF_DEEPSLATE.get(), List.of(
                LastOfDeepslateAnimations.DYING,
                LastOfDeepslateAnimations.WAKING,
                LastOfDeepslateAnimations.SHOOT_LAND,
                LastOfDeepslateAnimations.SMASH_GROUND,
                LastOfDeepslateAnimations.SPIN_SWING,
                LastOfDeepslateAnimations.INTERCEPT_SHOOT,
                LastOfDeepslateAnimations.ROLL
        ));

        ClientAnimationRegistry.register(ACEntityTypes.BRAVE.get(), List.of(
                BraveAnimations.JUMPING,
                BraveAnimations.TEST
        ));

        ClientAnimationRegistry.register(ACEntityTypes.DEEPSLATE_SENTINEL.get(), List.of(
                DeepslateSentinelAnimations.CHARGE
        ));
    }

    @SubscribeEvent
    static void onRegisterRangeSelectItemModelProperties(RegisterRangeSelectItemModelPropertyEvent event) {
        event.register(Archaion.prefix("echos_grace_pull"), EchosGracePull.MAP_CODEC);
    }

    @SubscribeEvent
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ACEntityTypes.LAST_OF_DEEPSLATE.get(), LastOfDeepslateRenderer::new);
        event.registerEntityRenderer(ACEntityTypes.SLATED.get(), SlatedRenderer::new);
        event.registerEntityRenderer(ACEntityTypes.WIGHT.get(), WightRenderer::new);
        event.registerEntityRenderer(ACEntityTypes.ECHO_STAR.get(), EchoStarProjectileRenderer::new);
        event.registerEntityRenderer(ACEntityTypes.BRAVE.get(), BraveRenderer::new);
        event.registerEntityRenderer(ACEntityTypes.THROWN_ECHO_MACE.get(), ThrownEchoMaceRenderer::new);
        event.registerEntityRenderer(ACEntityTypes.THROWN_IMPACT_PEARL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ACEntityTypes.LOD_INTERCEPT_BLAST.get(), LODInterceptBlastRenderer::new);
        event.registerEntityRenderer(ACEntityTypes.DEEPSLATE_SENTINEL.get(), DeepslateSentinelRenderer::new);

        event.registerBlockEntityRenderer(ACBlockEntities.DEEPSLATE_TRIAL_SPAWNER.get(), DeepslateSpawnerRenderer::new);
    }

    @SubscribeEvent
    static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LastOfDeepslateModel.LAYER_LOCATION, LastOfDeepslateModel::createBodyLayer);
        event.registerLayerDefinition(BraveModel.LAYER_LOCATION, () -> BraveModel.createBodyLayer(CubeDeformation.NONE));
        event.registerLayerDefinition(BraveModel.CHARGED_LAYER_LOCATION, () -> BraveModel.createBodyLayer(new CubeDeformation(2.0F)));
        event.registerLayerDefinition(DeepslateSentinelModel.LAYER_LOCATION, DeepslateSentinelModel::createBodyLayer);
    }

}
