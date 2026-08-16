package com.ratrod.archaion.client.renderers.layer;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.renderers.renderstate.BraveRenderState;
import com.ratrod.archaion.client.models.BraveModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.Identifier;

public class BraveChargedLayer extends EnergySwirlLayer<BraveRenderState, BraveModel<BraveRenderState>> {

    private static final Identifier POWER_LOCATION = Archaion.prefix("textures/entity/brave_charged.png");
    private final BraveModel<BraveRenderState> model;

    public BraveChargedLayer(RenderLayerParent<BraveRenderState, BraveModel<BraveRenderState>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new BraveModel<>(modelSet.bakeLayer(BraveModel.CHARGED_LAYER_LOCATION));
    }

    protected boolean isPowered(BraveRenderState state) {
        return state.isCharged;
    }

    protected float xOffset(float t) {
        return t * 0.01F;
    }

    protected Identifier getTextureLocation() {
        return POWER_LOCATION;
    }

    protected BraveModel<BraveRenderState> model() {
        return this.model;
    }
}
