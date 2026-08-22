package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.models.LastOfDeepslateModel;
import com.ratrod.archaion.client.renderers.layer.LastOfDeepslateChargedLayer;
import com.ratrod.archaion.client.renderers.renderstate.LastOfDeepslateRenderState;
import com.ratrod.archaion.entities.LastOfDeepslate;
import com.ratrod.archaion.entities.ai.SleepingState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LivingEntityEmissiveLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class LastOfDeepslateRenderer extends MobRenderer<LastOfDeepslate, LastOfDeepslateRenderState, LastOfDeepslateModel<LastOfDeepslateRenderState>> {

    private static final Identifier TEXTURE = Archaion.prefix("textures/entity/last_of_deepslate.png");
    private static final Identifier GLOW_TEXTURE = Archaion.prefix("textures/entity/last_of_deepslate_glow.png");

    private static final Identifier TEXTURE_P1 = Archaion.prefix("textures/entity/last_of_deepslate_p1.png");
    private static final Identifier GLOW_TEXTURE_P1 = Archaion.prefix("textures/entity/last_of_deepslate_glow_p1.png");

    private static final Identifier TEXTURE_P2 = Archaion.prefix("textures/entity/last_of_deepslate_p2.png");
    private static final Identifier GLOW_TEXTURE_P2 = Archaion.prefix("textures/entity/last_of_deepslate_glow_p2.png");

    public LastOfDeepslateRenderer(EntityRendererProvider.Context context) {
        super(context, new LastOfDeepslateModel<>(context.bakeLayer(LastOfDeepslateModel.LAYER_LOCATION)), 5F);
        this.addLayer(
                new LivingEntityEmissiveLayer<>(
                        this,
                        this::getGlowTextureLocation,
                        (entity, ageInTicks) -> entity.sleepingState == SleepingState.SLEEPING ? 0 : Mth.clamp(1F + Mth.sin(ageInTicks * 0.25F), 0.0F, 1.0F),
                        new LastOfDeepslateModel<>(context.bakeLayer(LastOfDeepslateModel.LAYER_LOCATION)),
                        RenderTypes::entityTranslucentEmissive,
                        false
                )
        );
        this.addLayer(new LastOfDeepslateChargedLayer(this, context.getModelSet()));
    }

    @Override
    protected float getFlipDegrees() {
        return 0;
    }

    @Override
    protected int getBlockLightLevel(LastOfDeepslate entity, BlockPos blockPos) {
        return entity.getSleepingState() == SleepingState.SLEEPING ? 5 : 10;
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
        state.phase = entity.getPhase();
    }

    @Override
    public Identifier getTextureLocation(LastOfDeepslateRenderState state) {
        return switch (state.phase) {
            case 2 -> TEXTURE_P2;
            case 1 -> TEXTURE_P1;
            default -> TEXTURE;
        };
    }

    public Identifier getGlowTextureLocation(LastOfDeepslateRenderState state) {
        return switch (state.phase) {
            case 2 -> GLOW_TEXTURE_P2;
            case 1 -> GLOW_TEXTURE_P1;
            default -> GLOW_TEXTURE;
        };
    }
}
