package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.models.LastOfDeepslateModel;
import com.ratrod.archaion.client.renderers.layer.ACEmissiveLayer;
import com.ratrod.archaion.client.renderers.layer.LastOfDeepslateChargedLayer;
import com.ratrod.archaion.entities.LastOfDeepslate;
import com.ratrod.archaion.entities.ai.SleepingState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class LastOfDeepslateRenderer extends MobRenderer<LastOfDeepslate, LastOfDeepslateModel> {

    private static final ResourceLocation TEXTURE = Archaion.prefix("textures/entity/last_of_deepslate.png");
    private static final ResourceLocation GLOW_TEXTURE = Archaion.prefix("textures/entity/last_of_deepslate_glow.png");

    private static final ResourceLocation TEXTURE_P1 = Archaion.prefix("textures/entity/last_of_deepslate_p1.png");
    private static final ResourceLocation GLOW_TEXTURE_P1 = Archaion.prefix("textures/entity/last_of_deepslate_glow_p1.png");

    private static final ResourceLocation TEXTURE_P2 = Archaion.prefix("textures/entity/last_of_deepslate_p2.png");
    private static final ResourceLocation GLOW_TEXTURE_P2 = Archaion.prefix("textures/entity/last_of_deepslate_glow_p2.png");

    public LastOfDeepslateRenderer(EntityRendererProvider.Context context) {
        super(context, new LastOfDeepslateModel(context.bakeLayer(LastOfDeepslateModel.LAYER_LOCATION)), 5F);
        this.addLayer(
                new ACEmissiveLayer<>(
                        this,
                        new LastOfDeepslateModel(context.bakeLayer(LastOfDeepslateModel.LAYER_LOCATION)),
                        entity -> switch (entity.getPhase()) {
                            case 2 -> GLOW_TEXTURE_P2;
                            case 1 -> GLOW_TEXTURE_P1;
                            default -> GLOW_TEXTURE;
                        },
                        entity -> entity.getSleepingState() == SleepingState.SLEEPING ? 0 : Mth.clamp(1F + Mth.sin(entity.tickCount * 0.25F), 0.0F, 1.0F)
                )
        );
        this.addLayer(new LastOfDeepslateChargedLayer(this, context.getModelSet()));
    }

    @Override
    protected float getFlipDegrees(LastOfDeepslate entity) {
        return 0;
    }

    @Override
    public ResourceLocation getTextureLocation(LastOfDeepslate entity) {
        return switch (entity.getPhase()) {
            case 2 -> TEXTURE_P2;
            case 1 -> TEXTURE_P1;
            default -> TEXTURE;
        };
    }
}
