package com.ratrod.archaion.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ratrod.archaion.entities.ThrownEchoMace;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;

public class ThrownEchoMaceRenderer extends EntityRenderer<ThrownEchoMace, ThrownEchoMaceRenderState> {

    private final ItemModelResolver itemModelResolver;

    public ThrownEchoMaceRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    @Override
    public ThrownEchoMaceRenderState createRenderState() {
        return new ThrownEchoMaceRenderState();
    }

    @Override
    public void extractRenderState(ThrownEchoMace entity, ThrownEchoMaceRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.velocity = (float) entity.getDeltaMovement().length();
        this.itemModelResolver.updateForNonLiving(state.item, entity.getThrownStack(), ItemDisplayContext.FIXED, entity);
    }

    @Override
    public void submit(ThrownEchoMaceRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.item.isEmpty()) {
            poseStack.pushPose();
            poseStack.scale(2.0F, 2.0F, 2.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(-state.ageInTicks * 30));
            state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
        }
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
