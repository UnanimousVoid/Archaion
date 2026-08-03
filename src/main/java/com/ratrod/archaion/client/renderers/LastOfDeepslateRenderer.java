package com.ratrod.archaion.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.LastOfDeepslateRenderState;
import com.ratrod.archaion.client.models.LastOfDeepslateModel;
import com.ratrod.archaion.entities.LastOfDeepslateEntity;
import com.ratrod.archaion.entities.ai.SleepingState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LivingEntityEmissiveLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class LastOfDeepslateRenderer extends MobRenderer<LastOfDeepslateEntity, LastOfDeepslateRenderState, LastOfDeepslateModel<LastOfDeepslateRenderState>> {

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
    }

    @Override
    public void submit(LastOfDeepslateRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
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
    public void extractRenderState(LastOfDeepslateEntity entity, LastOfDeepslateRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.animationManager = entity.getAnimationManager();
        state.sleepingState = entity.getEntityData().get(LastOfDeepslateEntity.SLEEPING_STATE);
    }

    @Override
    public Identifier getTextureLocation(LastOfDeepslateRenderState state) {
        return TEXTURE;
    }
}
