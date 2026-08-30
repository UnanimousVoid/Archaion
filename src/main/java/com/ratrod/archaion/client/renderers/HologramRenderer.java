package com.ratrod.archaion.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ratrod.archaion.block.HologramBlockEntity;
import com.ratrod.archaion.client.renderers.renderstate.HologramRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class HologramRenderer implements BlockEntityRenderer<HologramBlockEntity, HologramRenderState> {

    private final Font font;

    public HologramRenderer(BlockEntityRendererProvider.Context context) {
        this.font = Minecraft.getInstance().font;
    }

    @Override
    public HologramRenderState createRenderState() {
        return new HologramRenderState();
    }

    @Override
    public void extractRenderState(HologramBlockEntity blockEntity, HologramRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.text = blockEntity.getText();
        state.textColor = blockEntity.getTextColor();
        state.ageInTicks = blockEntity.clientTicks + partialTicks;
    }

    @Override
    public void submit(HologramRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        int maxWidth = 160;
        float height = 1.5F + Mth.sin(state.ageInTicks * 0.1F) * 0.1F;
        int light = 0xF000F0;

        if (state.text.isEmpty()) {
            return;
        }

        List<FormattedCharSequence> lines = this.font.split(Component.translatable(state.text), maxWidth);
        if (lines.isEmpty()) {
            return;
        }

        float distance = (float) Minecraft.getInstance().player.position().distanceTo(Vec3.atCenterOf(state.blockPos).add(0.0, height - 0.5, 0.0));
        double nearest = 2.0;
        double furthest = 6.0;
        float alpha = (float) Math.clamp((furthest - distance) / (furthest - nearest), 0.0, 1.0);

        int color = applyAlpha(state.textColor, alpha);
        int outlineColor = applyAlpha(0x023238, alpha);

        if (alpha > 0.0F) {
            float scale = 0.02F;

            // Render Text
            poseStack.pushPose();
            poseStack.translate(0.5D, height, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(-camera.yRot + 180));
            poseStack.translate(0.0D, 0.0D, 0.01D);
            poseStack.scale(scale, -scale, scale);

            float maxLineWidth = 0.0F;
            for (FormattedCharSequence line : lines) {
                maxLineWidth = Math.max(maxLineWidth, this.font.width(line));
            }
            float totalHeight = lines.size() * this.font.lineHeight;
            float x = -maxLineWidth / 2.0F;
            float y = -totalHeight;

            for (FormattedCharSequence line : lines) {
                float lineX = x + (maxLineWidth - this.font.width(line)) / 2.0F;
                submitNodeCollector.submitText(poseStack, lineX, y, line, true, Font.DisplayMode.SEE_THROUGH, light, color, 0, outlineColor);
                y += this.font.lineHeight;
            }
            poseStack.popPose();

            // Render Fan
            poseStack.pushPose();
            poseStack.translate(0.5D, 0.5D, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(-camera.yRot));
            float r = ((color >> 16) & 0xFF) / 255.0F;
            float g = ((color >> 8) & 0xFF) / 255.0F;
            float b = (color & 0xFF) / 255.0F;

            int segments = 8;
            float halfAngle = (float) Math.toRadians(60.0);
            float radius = 2;

            submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.debugQuads(), (pose, consumer) -> {
                for (int i = 0; i < segments; i++) {
                    float angleA = -halfAngle + (2 * halfAngle) * (i / (float) segments);
                    float angleB = -halfAngle + (2 * halfAngle) * ((i + 1) / (float) segments);

                    float xA = (float) Math.sin(angleA) * radius;
                    float yA = (float) Math.cos(angleA) * radius;
                    float xB = (float) Math.sin(angleB) * radius;
                    float yB = (float) Math.cos(angleB) * radius;

                    consumer.addVertex(pose, 0.0F, 0.0F, 0.0F).setColor(r, g, b, alpha);
                    consumer.addVertex(pose, xA, yA, 0.0F).setColor(r, g, b, 0.0F);
                    consumer.addVertex(pose, xB, yB, 0.0F).setColor(r, g, b, 0.0F);
                    consumer.addVertex(pose, xB, yB, 0.0F).setColor(r, g, b, 0.0F);
                }
            });

            poseStack.popPose();
        }
    }

    public static int applyAlpha(int argb, float alpha) {
        int a = (int) (0xFF * Math.clamp(alpha, 0.0F, 1.0F));
        return (argb & 0x00FFFFFF) | (a << 24);
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 128;
    }

    @Override
    public boolean shouldRender(HologramBlockEntity blockEntity, Vec3 cameraPosition) {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(HologramBlockEntity blockEntity) {
        net.minecraft.core.BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX() - 1.0, pos.getY(), pos.getZ() - 1.0, pos.getX() + 2.0, pos.getY() + 4.0, pos.getZ() + 2.0);
    }
}
