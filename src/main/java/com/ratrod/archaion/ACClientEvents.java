package com.ratrod.archaion;

import com.ratrod.archaion.client.misc.AncientKeepClientData;
import com.ratrod.archaion.client.misc.BossbarRenderers;
import com.ratrod.archaion.client.misc.ClientBossBarData;
import com.ratrod.archaion.client.misc.LODSoundInstance;
import net.minecraft.util.Mth;
import org.joml.Vector4f;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.event.SelectMusicEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
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

        LODSoundInstance.tryToRepair();
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

    @SubscribeEvent
    static void renderAncientKeepFog(ViewportEvent.RenderFog event) {
        float factor = AncientKeepClientData.keepFogFactor();
        if (factor > 0.0F) {
            float near = event.getNearPlaneDistance();
            float far = event.getFarPlaneDistance();
            Vector4f base = new Vector4f(event.getFogData().color);

            event.setNearPlaneDistance(Mth.lerp(factor, near, 16F));
            event.setFarPlaneDistance(Mth.lerp(factor, far, 128F));
            event.getFogData().color.set(
                    Mth.lerp(factor, base.x, 0.390F),
                    Mth.lerp(factor, base.y, 0.898F),
                    Mth.lerp(factor, base.z, 1.00F),
                    Mth.lerp(factor, base.w, 1.0F)
            );
        }
    }
}
