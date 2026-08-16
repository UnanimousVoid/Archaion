package com.ratrod.archaion.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.renderers.renderstate.LastOfDeepslateRenderState;
import com.ratrod.archaion.client.models.LastOfDeepslateModel;
import com.ratrod.archaion.client.renderers.layer.LastOfDeepslateChargedLayer;
import com.ratrod.archaion.entities.LastOfDeepslate;
import com.ratrod.archaion.entities.ai.SleepingState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LivingEntityEmissiveLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;

public class LastOfDeepslateRenderer extends MobRenderer<LastOfDeepslate, LastOfDeepslateRenderState, LastOfDeepslateModel<LastOfDeepslateRenderState>> {

    private static final Identifier TEXTURE = Archaion.prefix("textures/entity/last_of_deepslate.png");
    private static final Identifier GLOW_TEXTURE = Archaion.prefix("textures/entity/last_of_deepslate_glow.png");

    public LastOfDeepslateRenderer(EntityRendererProvider.Context context) {
        super(context, new LastOfDeepslateModel<>(context.bakeLayer(LastOfDeepslateModel.LAYER_LOCATION)), 5F);
        this.addLayer(
                new LivingEntityEmissiveLayer<>(
                        this,
                        renderState -> GLOW_TEXTURE,
                        (entity, ageInTicks) -> entity.sleepingState == SleepingState.SLEEPING ? 0 : Mth.clamp(1F + Mth.sin(ageInTicks * 0.25F), 0.0F, 1.0F),
                        new LastOfDeepslateModel<>(context.bakeLayer(LastOfDeepslateModel.LAYER_LOCATION)),
                        RenderTypes::entityTranslucentEmissive,
                        false
                )
        );
        this.addLayer(new LastOfDeepslateChargedLayer(this, context.getModelSet()));
    }

    @Override
    public void submit(LastOfDeepslateRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        if (state.sleepingState == SleepingState.SLEEPING) {
            Minecraft instance = Minecraft.getInstance();
            Font font = instance.font;
            int light = 0xF000F0;
            List<FormattedCharSequence> lines = font.split(Component.translatable("[ Click to overview boss ]"), 160);
            if (lines.isEmpty()) {
                return;
            }

            float distance = (float) instance.player.position().distanceTo(state.pos.add(0.0, 3, 0.0));
            double nearest = 6.0;
            double furthest = 12.0;
            float alpha = (float) Math.clamp((furthest - distance) / (furthest - nearest), 0.0, 1.0);

            int color = HologramRenderer.applyAlpha(0xFFa3ffe5, alpha);
            int outlineColor = HologramRenderer.applyAlpha(0x023238, alpha);


            if (alpha > 0.0F) {
                float scale = 0.05F;

                poseStack.pushPose();
                poseStack.translate(0.5D, 2, -0.5D);

                double dx = state.playerPos.x - state.pos.x;
                double dz = state.playerPos.z - state.pos.z;
                float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
                poseStack.mulPose(Axis.YP.rotationDegrees(-yaw));

                poseStack.translate(0.0D, 0.0D, 3.5D);
                poseStack.scale(scale, -scale, scale);

                float maxLineWidth = 0.0F;
                for (FormattedCharSequence line : lines) {
                    maxLineWidth = Math.max(maxLineWidth, font.width(line));
                }
                float totalHeight = lines.size() *font.lineHeight;
                float x = -maxLineWidth / 2.0F;
                float y = -totalHeight;

                for (FormattedCharSequence line : lines) {
                    float lineX = x + (maxLineWidth - font.width(line)) / 2.0F;
                    submitNodeCollector.submitText(poseStack, lineX, y, line, true, Font.DisplayMode.SEE_THROUGH, light, color, 0, outlineColor);
                    y += font.lineHeight;
                }
                poseStack.popPose();
            }
        }
    }

    @Override
    protected float getFlipDegrees() {
        return 0;
    }

    @Override
    public LastOfDeepslateRenderState createRenderState() {
        return new LastOfDeepslateRenderState();
    }

    @Override
    public void extractRenderState(LastOfDeepslate entity, LastOfDeepslateRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.animationManager = entity.getAnimationManager();
        state.sleepingState = entity.getSleepingState();
        state.hasChargedArchaics = entity.hasChargedArchaics();
        state.pos = entity.getPosition(partialTicks);
        state.playerPos = Minecraft.getInstance().player.getPosition(partialTicks);
    }

    @Override
    public Identifier getTextureLocation(LastOfDeepslateRenderState state) {
        return TEXTURE;
    }
}
