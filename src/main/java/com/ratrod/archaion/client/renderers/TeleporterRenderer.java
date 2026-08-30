package com.ratrod.archaion.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.block.TeleporterBlock;
import com.ratrod.archaion.block.TeleporterBlockEntity;
import com.ratrod.archaion.client.renderers.renderstate.TeleporterRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.text.DecimalFormat;

public class TeleporterRenderer implements BlockEntityRenderer<TeleporterBlockEntity, TeleporterRenderState> {

    public static final Identifier BEAM_LOCATION = Archaion.prefix("textures/entity/teleporter_beam.png");

    public TeleporterRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public TeleporterRenderState createRenderState() {
        return new TeleporterRenderState();
    }

    @Override
    public void extractRenderState(TeleporterBlockEntity blockEntity, TeleporterRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        BlockState blockState = blockEntity.getBlockState();
        state.colorArgb = blockState.getValue(TeleporterBlock.COLOR).argb();
        state.disabled = blockEntity.getCooldownTicks() > 0;
        state.ageInTicks = blockEntity.tickCount + partialTicks;
        state.maxHeight = blockEntity.maxHeight;
        state.cooldown = blockEntity.getCooldownTicks();
    }

    @Override
    public void submit(TeleporterRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.disabled) {

            Font font = Minecraft.getInstance().font;
            float height = 1.5F + Mth.sin(state.ageInTicks * 0.1F) * 0.1F;
            float scale = 0.05F;
            int light = 0xF000F0;

            poseStack.pushPose();
            poseStack.translate(0.5D, height, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(-camera.yRot + 180));
            poseStack.translate(0.0D, 0.0D, 0.01D);
            poseStack.scale(scale, -scale, scale);

            DecimalFormat format = new DecimalFormat("0.0");
            MutableComponent input = Component.literal(format.format(state.cooldown / 20F) + "s");
            float x = -font.width(input) / 2.0F;
            collector.submitText(poseStack, x, 0, input.getVisualOrderText(), true, Font.DisplayMode.SEE_THROUGH, light, state.colorArgb, 0, 0xff023238);

            poseStack.popPose();

        } else {
            float beamRadius = 0.25F + Mth.cos(state.ageInTicks * 0.1F) * 0.05F;
            float beamGlowRadius = 0.3F + Mth.sin(state.ageInTicks * 0.1F) * 0.1F;
            BeaconRenderer.submitBeaconBeam(poseStack, collector, BEAM_LOCATION, 1.0F, state.ageInTicks * 2, 0, state.maxHeight, state.colorArgb, beamRadius, beamGlowRadius);
        }
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
    public boolean shouldRender(TeleporterBlockEntity blockEntity, Vec3 cameraPosition) {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(TeleporterBlockEntity blockEntity) {
        net.minecraft.core.BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX() - 1.0, pos.getY(), pos.getZ() - 1.0, pos.getX() + 2.0, pos.getY() + blockEntity.maxHeight + 1.0, pos.getZ() + 2.0);
    }
}
