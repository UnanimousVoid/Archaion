package com.ratrod.archaion;

import com.ratrod.archaion.client.models.LastOfDeepslateModel;
import com.ratrod.archaion.client.renderers.EchoStarProjectileRenderer;
import com.ratrod.archaion.client.renderers.LastOfDeepslateRenderer;
import com.ratrod.archaion.registry.ACEntityTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

//@Mod(value = Archaion.MODID, dist = Dist.CLIENT)
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
        event.registerEntityRenderer(ACEntityTypes.ECHO_STAR.get(), EchoStarProjectileRenderer::new);
    }

    @SubscribeEvent
    static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LastOfDeepslateModel.LAYER_LOCATION, LastOfDeepslateModel::createBodyLayer);
    }
}
