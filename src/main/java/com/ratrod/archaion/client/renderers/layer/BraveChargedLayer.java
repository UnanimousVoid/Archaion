package com.ratrod.archaion.client.renderers.layer;

import com.ratrod.archaion.client.BraveRenderState;
import com.ratrod.archaion.client.models.BraveModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.Identifier;

public class BraveChargedLayer extends EnergySwirlLayer<BraveRenderState, BraveModel<BraveRenderState>> {

    private static final Identifier POWER_LOCATION = Identifier.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");
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
