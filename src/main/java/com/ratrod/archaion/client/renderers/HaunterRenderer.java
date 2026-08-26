package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.models.HaunterModel;
import com.ratrod.archaion.client.renderers.layer.HaunterChargedLayer;
import com.ratrod.archaion.client.renderers.renderstate.HaunterRenderState;
import com.ratrod.archaion.entities.Haunter;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LivingEntityEmissiveLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class HaunterRenderer extends MobRenderer<Haunter, HaunterRenderState, HaunterModel<HaunterRenderState>> {

    private static final Identifier TEXTURE = Archaion.prefix("textures/entity/haunter.png");

    public HaunterRenderer(EntityRendererProvider.Context context) {
        super(context, new HaunterModel<>(context.bakeLayer(HaunterModel.LAYER_LOCATION)), 0.4F);
        this.addLayer(
                new LivingEntityEmissiveLayer<>(
                        this,
                        (state) -> TEXTURE,
                        (entity, ageInTicks) -> 1.0F,
                        new HaunterModel<>(context.bakeLayer(HaunterModel.LAYER_LOCATION)),
                        RenderTypes::entityTranslucentEmissive,
                        false
                )
        );
        this.addLayer(new HaunterChargedLayer(this, context.getModelSet()));
    }

    @Override
    public Identifier getTextureLocation(HaunterRenderState state) {
        return TEXTURE;
    }

    @Override
    protected float getWhiteOverlayProgress(HaunterRenderState state) {
        float value = 0.75F + Mth.sin(state.ageInTicks * 1.0) * 0.25F;
        return state.swelling ? Mth.clamp(value, 0.5F, 1.0F) : 0.0F;
    }

    @Override
    public HaunterRenderState createRenderState() {
        return new HaunterRenderState();
    }

    @Override
    public void extractRenderState(Haunter entity, HaunterRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.animationManager = entity.getAnimationManager();
        state.isCharged = entity.isCharged();
        state.swelling = entity.isSwelling();
    }
}
