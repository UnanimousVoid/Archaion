package com.ratrod.archaion;

import com.ratrod.archaion.client.misc.AncientKeepClientData;
import com.ratrod.archaion.client.misc.BossbarRenderers;
import com.ratrod.archaion.client.misc.ClientBossBarData;
import com.ratrod.archaion.client.misc.LODSoundInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.event.SelectMusicEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.UUID;

@EventBusSubscriber(modid = Archaion.MODID, value = Dist.CLIENT)
public class ACClientEvents {

    @SubscribeEvent
    static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!event.getEntity().level().isClientSide()) {
            return;
        }
        AncientKeepClientData.tick(event.getEntity());
    }

    @SubscribeEvent
    static void suppressVanillaMusic(SelectMusicEvent event) {
        if (LODSoundInstance.isActive()) {
            event.setMusic(null);
        }
    }

    @SubscribeEvent
    static void renderBossbar(CustomizeGuiOverlayEvent.BossEventProgress event) {
        UUID currentBossId = event.getBossEvent().getId();
        int bossIdx = ClientBossBarData.getBossIdx(currentBossId);
        if (bossIdx == 0) {
            event.setCanceled(true);
            BossbarRenderers.renderLastOfDeepslate(event);
        }
    }
}
