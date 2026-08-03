package com.ratrod.archaion;

import com.ratrod.archaion.client.models.BraveModel;
import com.ratrod.archaion.client.models.LastOfDeepslateModel;
import com.ratrod.archaion.client.renderers.*;
import com.ratrod.archaion.registry.ACBlockEntities;
import com.ratrod.archaion.registry.ACEntityTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = Archaion.MODID, value = Dist.CLIENT)
public class ACClientSetup {

    public ACClientSetup(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ACEntityTypes.LAST_OF_DEEPSLATE.get(), LastOfDeepslateRenderer::new);
        event.registerEntityRenderer(ACEntityTypes.SLATED.get(), SlatedRenderer::new);
        event.registerEntityRenderer(ACEntityTypes.WIGHT.get(), WightRenderer::new);
        event.registerEntityRenderer(ACEntityTypes.ECHO_STAR.get(), EchoStarProjectileRenderer::new);
        event.registerEntityRenderer(ACEntityTypes.BRAVE.get(), BraveRenderer::new);
        event.registerEntityRenderer(ACEntityTypes.THROWN_ECHO_MACE.get(), ThrownEchoMaceRenderer::new);

        event.registerBlockEntityRenderer(ACBlockEntities.DEEPSLATE_TRIAL_SPAWNER.get(), DeepslateSpawnerRenderer::new);
    }

    @SubscribeEvent
    static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LastOfDeepslateModel.LAYER_LOCATION, LastOfDeepslateModel::createBodyLayer);
        event.registerLayerDefinition(BraveModel.LAYER_LOCATION, BraveModel::createBodyLayer);
    }
}
