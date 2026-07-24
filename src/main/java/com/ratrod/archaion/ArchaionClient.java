package com.ratrod.archaion;

import com.ratrod.archaion.network.s2c.CameraShakePacket;
import com.ratrod.archaion.network.s2c.RemoveBossBarDataPacket;
import com.ratrod.archaion.network.s2c.SyncBossBarDataPacket;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

@Mod(value = Archaion.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Archaion.MODID, value = Dist.CLIENT)
public class ArchaionClient {
    public ArchaionClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {

    }
}
