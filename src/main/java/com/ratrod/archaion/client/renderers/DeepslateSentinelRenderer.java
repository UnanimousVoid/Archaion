package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.renderers.renderstate.DeepslateSentinelRenderState;
import com.ratrod.archaion.client.models.DeepslateSentinelModel;
import com.ratrod.archaion.client.renderers.layer.DeepslateSentinelChargedLayer;
import com.ratrod.archaion.entities.DeepslateSentinel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class DeepslateSentinelRenderer extends MobRenderer<DeepslateSentinel, DeepslateSentinelRenderState, DeepslateSentinelModel<DeepslateSentinelRenderState>> {

    private static final Identifier TEXTURE_LOCATION = Archaion.prefix("textures/entity/deepslate_sentinel.png");

    public DeepslateSentinelRenderer(EntityRendererProvider.Context context) {
        super(context, new DeepslateSentinelModel<>(context.bakeLayer(DeepslateSentinelModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new DeepslateSentinelChargedLayer(this, context.getModelSet()));
    }

    @Override
    public Identifier getTextureLocation(DeepslateSentinelRenderState state) {
        return TEXTURE_LOCATION;
    }

    @Override
    public DeepslateSentinelRenderState createRenderState() {
        return new DeepslateSentinelRenderState();
    }

    @Override
    public void extractRenderState(DeepslateSentinel entity, DeepslateSentinelRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.animationManager = entity.getAnimationManager();
        state.isCharged = entity.isCharged();
    }
}
