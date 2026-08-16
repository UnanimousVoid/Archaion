package com.ratrod.archaion.client.renderers.layer;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.renderers.renderstate.DeepslateSentinelRenderState;
import com.ratrod.archaion.client.models.DeepslateSentinelModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.Identifier;

public class DeepslateSentinelChargedLayer extends EnergySwirlLayer<DeepslateSentinelRenderState, DeepslateSentinelModel<DeepslateSentinelRenderState>> {

    private static final Identifier POWER_LOCATION = Archaion.prefix("textures/entity/last_of_deepslate_charged.png");
    private final DeepslateSentinelModel<DeepslateSentinelRenderState> model;

    public DeepslateSentinelChargedLayer(RenderLayerParent<DeepslateSentinelRenderState, DeepslateSentinelModel<DeepslateSentinelRenderState>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new DeepslateSentinelModel<>(modelSet.bakeLayer(DeepslateSentinelModel.CHARGED_LAYER_LOCATION));
    }

    protected boolean isPowered(DeepslateSentinelRenderState state) {
        return state.isCharged;
    }

    protected float xOffset(float t) {
        return t * 0.01F;
    }

    protected Identifier getTextureLocation() {
        return POWER_LOCATION;
    }

    protected DeepslateSentinelModel<DeepslateSentinelRenderState> model() {
        return this.model;
    }
}
