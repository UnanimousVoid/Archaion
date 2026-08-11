package com.ratrod.archaion.client.misc;

import com.ratrod.archaion.Archaion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;

import java.awt.*;

public class BossbarRenderers {

    protected static final Identifier BARS_LOCATION = Archaion.prefix("textures/gui/hud_misc.png");

    public static void renderLastOfDeepslate(CustomizeGuiOverlayEvent.BossEventProgress event) {

        GuiGraphicsExtractor guiGraphics = event.getGuiGraphics();
        Minecraft mc = Minecraft.getInstance();
        int x = event.getX();
        int y = event.getY();
        float progress = event.getBossEvent().getProgress();

        // Bar Background
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BARS_LOCATION, x, y, 0, 0, 192, 32, 256, 256);

        // Empty Bar
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BARS_LOCATION, x, y, 0, 64, 192, 32, 256, 256);

        // Full Bar
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BARS_LOCATION, x, y, 0, 32, (int)(192 * progress), 32, 256, 256);

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
