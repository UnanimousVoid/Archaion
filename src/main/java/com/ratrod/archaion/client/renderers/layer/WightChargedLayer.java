package com.ratrod.archaion.client.renderers.layer;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.renderers.renderstate.WightRenderState;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.skeleton.SkeletonModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.Identifier;

public class WightChargedLayer extends EnergySwirlLayer<WightRenderState, SkeletonModel<WightRenderState>> {

    private static final Identifier POWER_LOCATION = Archaion.prefix("textures/entity/brave_charged.png");
    private final SkeletonModel<WightRenderState> model;

    public WightChargedLayer(RenderLayerParent<WightRenderState, SkeletonModel<WightRenderState>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new SkeletonModel<>(modelSet.bakeLayer(ModelLayers.SKELETON));
    }

    protected boolean isPowered(WightRenderState state) {
        return state.isCharged;
    }

    protected float xOffset(float t) {
        return t * 0.01F;
    }

    protected Identifier getTextureLocation() {
        return POWER_LOCATION;
    }

    protected SkeletonModel<WightRenderState> model() {
        return this.model;
    }
}
