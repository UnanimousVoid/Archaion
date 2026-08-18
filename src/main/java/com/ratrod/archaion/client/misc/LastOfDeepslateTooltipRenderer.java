package com.ratrod.archaion.client.misc;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.entities.LastOfDeepslate;
import com.ratrod.archaion.entities.ai.SleepingState;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;

import java.util.List;

public class LastOfDeepslateTooltipRenderer {

    public static void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Entity player = mc.player;
        if (player == null) {
            return;
        }

        LastOfDeepslate target = findLookedAtLastOfDeepslate(player, deltaTracker);
        if (target == null || target.getSleepingState() != SleepingState.SLEEPING) {
            return;
        }

        renderTooltip(guiGraphics, target, mc, target.getDisplayName(), deltaTracker);
    }

    private static void renderTooltip(GuiGraphicsExtractor guiGraphics, LastOfDeepslate target, Minecraft mc, Component name, DeltaTracker deltaTracker) {
        Font font = mc.font;
        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
        float tick = mc.player.tickCount + partialTick;
        MutableComponent nameUpdated = name.copy().withStyle(ChatFormatting.AQUA);
        List<ClientTooltipComponent> components = List.of(
                ClientTooltipComponent.create(nameUpdated.getVisualOrderText()),
                ClientTooltipComponent.create(new EchoChargeRequiredTooltip(4 - target.getEchoChargesFed()))
        );

        int mouseX = guiGraphics.guiWidth() / 2;
        int mouseY = (guiGraphics.guiHeight() / 2);
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(mouseX, mouseY + Mth.sin(tick * 0.1F) * 2);
        guiGraphics.tooltip(font, components, 0, 0, DefaultTooltipPositioner.INSTANCE, Archaion.prefix("lod"));
        guiGraphics.pose().popMatrix();
    }

    private static LastOfDeepslate findLookedAtLastOfDeepslate(Entity player, DeltaTracker deltaTracker) {
        double range = 32.0D;
        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
        Vec3 from = player.getEyePosition(partialTick);
        Vec3 view = player.getViewVector(partialTick);
        Vec3 to = from.add(view.x * range, view.y * range, view.z * range);

        AABB searchBox = player.getBoundingBox().expandTowards(view.x * range, view.y * range, view.z * range).inflate(1.0D);
        EntityHitResult hit = ProjectileUtil.getEntityHitResult(player, from, to, searchBox, e -> e instanceof LastOfDeepslate && e.isAlive(), Mth.square(range));
        if (hit == null) {
            return null;
        }

        BlockHitResult blockHit = player.level().clip(new ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        if (blockHit.getType() != HitResult.Type.MISS && from.distanceToSqr(blockHit.getLocation()) < from.distanceToSqr(hit.getLocation())) {
            return null;
        }

        return (LastOfDeepslate) hit.getEntity();
    }
}
