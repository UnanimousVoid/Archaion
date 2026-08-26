package com.ratrod.archaion.client.renderers.layer;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.models.HaunterModel;
import com.ratrod.archaion.client.renderers.renderstate.HaunterRenderState;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.Identifier;

public class HaunterChargedLayer extends EnergySwirlLayer<HaunterRenderState, HaunterModel<HaunterRenderState>> {

    private static final Identifier POWER_LOCATION = Archaion.prefix("textures/entity/brave_charged.png");
    private final HaunterModel<HaunterRenderState> model;

    public HaunterChargedLayer(RenderLayerParent<HaunterRenderState, HaunterModel<HaunterRenderState>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new HaunterModel<>(modelSet.bakeLayer(HaunterModel.CHARGED_LAYER_LOCATION));
    }

    protected boolean isPowered(HaunterRenderState state) {
        return state.isCharged;
    }

    protected float xOffset(float t) {
        return t * 0.01F;
    }

    protected Identifier getTextureLocation() {
        return POWER_LOCATION;
    }

    protected HaunterModel<HaunterRenderState> model() {
        return this.model;
    }
}
