package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.BraveRenderState;
import com.ratrod.archaion.client.models.BraveModel;
import com.ratrod.archaion.client.renderers.layer.BraveChargedLayer;
import com.ratrod.archaion.entities.BraveEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LivingEntityEmissiveLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class BraveRenderer extends MobRenderer<BraveEntity, BraveRenderState, BraveModel<BraveRenderState>> {

    private static final Identifier TEXTURE_LOCATION = Archaion.prefix("textures/entity/brave.png");
    private static final Identifier GLOW_TEXTURE = Archaion.prefix("textures/entity/brave_glow.png");

    public BraveRenderer(EntityRendererProvider.Context context) {
        super(context, new BraveModel<>(context.bakeLayer(BraveModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(
                new LivingEntityEmissiveLayer<>(
                        this,
                        renderState -> GLOW_TEXTURE,
                        (state, ageInTicks) -> Mth.clamp(1F + Mth.sin(ageInTicks * 0.3F), 0.0F, 1.0F),
                        new BraveModel<>(context.bakeLayer(BraveModel.LAYER_LOCATION)),
                        RenderTypes::entityTranslucentEmissive,
                        false
                )
        );
        this.addLayer(new BraveChargedLayer(this, context.getModelSet()));
    }

    @Override
    public Identifier getTextureLocation(BraveRenderState state) {
        return TEXTURE_LOCATION;
    }

    @Override
    public BraveRenderState createRenderState() {
        return new BraveRenderState();
    }

    @Override
    public void extractRenderState(BraveEntity entity, BraveRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.animationManager = entity.getAnimationManager();
        state.isCharged = entity.isCharged();
    }
}
