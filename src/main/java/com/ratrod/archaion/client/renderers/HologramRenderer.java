package com.ratrod.archaion.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.ratrod.archaion.block.HologramBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class HologramRenderer implements BlockEntityRenderer<HologramBlockEntity> {

    private final Font font;

    public HologramRenderer(BlockEntityRendererProvider.Context context) {
        this.font = Minecraft.getInstance().font;
    }

    @Override
    public void render(HologramBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        int maxWidth = 160;
        float height = 1.5F + Mth.sin((blockEntity.clientTicks + partialTick) * 0.1F) * 0.1F;
        int light = 0xF000F0;

        if (blockEntity.getText().isEmpty()) {
            return;
        }

        List<FormattedCharSequence> lines = this.font.split(Component.translatable(blockEntity.getText()), maxWidth);
        if (lines.isEmpty()) {
            return;
        }

        float distance = (float) Minecraft.getInstance().player.position().distanceTo(Vec3.atCenterOf(blockEntity.getBlockPos()).add(0.0, height - 0.5, 0.0));
        double nearest = 2.0;
        double furthest = 6.0;
        float alpha = (float) Math.clamp((furthest - distance) / (furthest - nearest), 0.0, 1.0);

        int color = applyAlpha(blockEntity.getTextColor(), alpha);
        int outlineColor = applyAlpha(0x023238, alpha);

        if (alpha > 0.0F) {
            float scale = 0.02F;
            float yRot = Minecraft.getInstance().gameRenderer.getMainCamera().getYRot();

            // Render Text
            poseStack.pushPose();
            poseStack.translate(0.5D, height, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(-yRot + 180));
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
                this.font.drawInBatch8xOutline(line, lineX, y, color, outlineColor, poseStack.last().pose(), buffer, light);
                y += this.font.lineHeight;
            }
            poseStack.popPose();

            // Render Fan
            poseStack.pushPose();
            poseStack.translate(0.5D, 0.5D, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(-yRot));
            float r = ((color >> 16) & 0xFF) / 255.0F;
            float g = ((color >> 8) & 0xFF) / 255.0F;
            float b = (color & 0xFF) / 255.0F;

            int segments = 8;
            float halfAngle = (float) Math.toRadians(60.0);
            float radius = 2;

            VertexConsumer consumer = buffer.getBuffer(RenderType.debugQuads());
            for (int i = 0; i < segments; i++) {
                float angleA = -halfAngle + (2 * halfAngle) * (i / (float) segments);
                float angleB = -halfAngle + (2 * halfAngle) * ((i + 1) / (float) segments);

                float xA = (float) Math.sin(angleA) * radius;
                float yA = (float) Math.cos(angleA) * radius;
                float xB = (float) Math.sin(angleB) * radius;
                float yB = (float) Math.cos(angleB) * radius;

                consumer.addVertex(poseStack.last().pose(), 0.0F, 0.0F, 0.0F).setColor(r, g, b, alpha);
                consumer.addVertex(poseStack.last().pose(), xA, yA, 0.0F).setColor(r, g, b, 0.0F);
                consumer.addVertex(poseStack.last().pose(), xB, yB, 0.0F).setColor(r, g, b, 0.0F);
                consumer.addVertex(poseStack.last().pose(), xB, yB, 0.0F).setColor(r, g, b, 0.0F);
            }
            poseStack.popPose();
        }
    }

    public static int applyAlpha(int argb, float alpha) {
        int a = (int) (0xFF * Math.clamp(alpha, 0.0F, 1.0F));
        return (argb & 0x00FFFFFF) | (a << 24);
    }

    @Override
    public boolean shouldRenderOffScreen(HologramBlockEntity blockEntity) {
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
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX() - 1.0, pos.getY(), pos.getZ() - 1.0, pos.getX() + 2.0, pos.getY() + 4.0, pos.getZ() + 2.0);
    }
}
