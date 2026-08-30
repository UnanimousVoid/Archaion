package com.ratrod.archaion.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.block.TeleporterBlock;
import com.ratrod.archaion.block.TeleporterBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.text.DecimalFormat;

public class TeleporterRenderer implements BlockEntityRenderer<TeleporterBlockEntity> {

    public static final ResourceLocation BEAM_LOCATION = Archaion.prefix("textures/entity/teleporter_beam.png");

    public TeleporterRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(TeleporterBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        BlockState blockState = blockEntity.getBlockState();
        int colorArgb = blockState.getValue(TeleporterBlock.COLOR).argb();
        boolean disabled = blockEntity.getCooldownTicks() > 0;
        float ageInTicks = blockEntity.tickCount + partialTick;
        int maxHeight = blockEntity.maxHeight;
        int cooldown = blockEntity.getCooldownTicks();

        if (disabled) {

            Font font = Minecraft.getInstance().font;
            float height = 1.5F + Mth.sin(ageInTicks * 0.1F) * 0.1F;
            float scale = 0.05F;
            int light = 0xF000F0;

            poseStack.pushPose();
            poseStack.translate(0.5D, height, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(-Minecraft.getInstance().gameRenderer.getMainCamera().getYRot() + 180));
            poseStack.translate(0.0D, 0.0D, 0.01D);
            poseStack.scale(scale, -scale, scale);

            DecimalFormat format = new DecimalFormat("0.0");
            MutableComponent input = Component.literal(format.format(cooldown / 20F) + "s");
            float x = -font.width(input) / 2.0F;
            font.drawInBatch8xOutline(input.getVisualOrderText(), x, 0, colorArgb, 0xff023238, poseStack.last().pose(), buffer, light);

            poseStack.popPose();

        } else {
            float beamRadius = 0.25F + Mth.cos(ageInTicks * 0.1F) * 0.05F;
            float beamGlowRadius = 0.3F + Mth.sin(ageInTicks * 0.1F) * 0.1F;
            BeaconRenderer.renderBeaconBeam(poseStack, buffer, BEAM_LOCATION, 1.0F, ageInTicks * 2, 0L, 0, maxHeight, colorArgb, beamRadius, beamGlowRadius);
        }
    }

    @Override
    public boolean shouldRenderOffScreen(TeleporterBlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 128;
    }

    @Override
    public boolean shouldRender(TeleporterBlockEntity blockEntity, Vec3 cameraPosition) {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(TeleporterBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX() - 1.0, pos.getY(), pos.getZ() - 1.0, pos.getX() + 2.0, pos.getY() + blockEntity.maxHeight + 1.0, pos.getZ() + 2.0);
    }
}
