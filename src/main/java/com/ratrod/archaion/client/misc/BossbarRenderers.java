package com.ratrod.archaion.client.misc;

import com.ratrod.archaion.Archaion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;

import java.awt.*;
import java.util.Map;

public class BossbarRenderers {

    protected static final Identifier BARS_LOCATION = Archaion.prefix("textures/gui/hud_misc.png");

    public static void renderLastOfDeepslate(CustomizeGuiOverlayEvent.BossEventProgress event) {
        GuiGraphicsExtractor guiGraphics = event.getGuiGraphics();
        Minecraft mc = Minecraft.getInstance();
        int x = event.getX();
        int y = event.getY();
        float progress = event.getBossEvent().getProgress();

        Map<String, Integer> values = ClientBossBarData.getValues(event.getBossEvent().getId());
        int phaseTriggered = values.getOrDefault("archaicPhase", 0);

        // Bar Background
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BARS_LOCATION, x, y, 0, 0, 192, 32, 256, 256);

        // Empty Bar
        float ageInTicks = mc.player.tickCount + event.getPartialTick().getGameTimeDeltaPartialTick(true);
        int segmentWidth = 4;
        int waveMult = phaseTriggered == 1 ? 1 : 3;

        for (int segmentX = 0; segmentX < 192; segmentX += segmentWidth) {
            int width = Math.min(segmentWidth, 192 - segmentX);
            int offsetY = phaseTriggered >= 1 ? (int)(Mth.sin(ageInTicks * (0.075F * waveMult) + segmentX * 0.05F) * 4.0F) : 0;
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BARS_LOCATION, x + segmentX, y + offsetY, segmentX, 64, width, 32, 256, 256);
        }

        // Full Bar
        int fullWidth = (int)(192 * progress);

        for (int segmentX = 0; segmentX < fullWidth; segmentX += segmentWidth) {
            int width = Math.min(segmentWidth, fullWidth - segmentX);
            int offsetY = phaseTriggered >= 1 ? (int)(Mth.sin(ageInTicks * (0.075F * waveMult) + segmentX * 0.05F) * 4.0F) : 0;
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BARS_LOCATION, x + segmentX, y + offsetY + 1, segmentX, 32, width, 32, 256, 256);
        }

        // Archaic Raid
        int raidAlive = values.getOrDefault("archaicRaidAlive", 0);
        int raidTotal = values.getOrDefault("archaicRaidTotal", 0);
        int raidXOffset = (192 / 2) - (109 / 2);

        if (raidTotal > 0 && raidAlive > 0) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BARS_LOCATION, x + raidXOffset, y + 30, 0, 160, 109, 32, 256, 256);

            float raidProgress = Mth.clamp(raidAlive / (float) raidTotal, 0.0F, 1.0F);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BARS_LOCATION, x + raidXOffset, y + 30, 0, 128, (int)(109 * raidProgress), 32, 256, 256);
        }

        if (values.getOrDefault("hasChargedArchaics", 0) == 1) {
            float yy = Mth.sin(ageInTicks * 0.075F) * 4;
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(0, yy);
            int color = ARGB.gray(Mth.clamp(0.5F + Mth.cos(ageInTicks * 0.2F), 0F, 1F));
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BARS_LOCATION, x, y, 0, 96, 192, 32, 256, 256, color);
            guiGraphics.pose().popMatrix();
        }

        // Text (Imitate Outline Rendering)
        Component name = event.getBossEvent().getName();
        int nameWidth = mc.font.width(name);
        int textX = x + (186 - nameWidth) / 2;
        int textY = y;
        int textColor = new Color(64, 255, 251).getRGB();
        int outlineColor = new Color(4, 30, 134).getRGB();

        for (int xo = -1; xo <= 1; xo++) {
            for (int yo = -1; yo <= 1; yo++) {
                if (xo != 0 || yo != 0) {
                    guiGraphics.text(mc.font, name, textX + xo, textY + yo, outlineColor);
                }
            }
        }

        guiGraphics.text(mc.font, name, textX, textY, textColor);
        event.setIncrement(42);
    }
}