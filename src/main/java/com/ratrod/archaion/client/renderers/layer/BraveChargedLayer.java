package com.ratrod.archaion.client.renderers.layer;

import com.ratrod.archaion.client.ACLivingEntityRenderState;
import com.ratrod.archaion.client.models.BraveModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.Identifier;

public class BraveChargedLayer extends EnergySwirlLayer<ACLivingEntityRenderState, BraveModel<ACLivingEntityRenderState>> {

    private static final Identifier POWER_LOCATION = Identifier.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");
    private final BraveModel<ACLivingEntityRenderState> model;

    public BraveChargedLayer(RenderLayerParent<ACLivingEntityRenderState, BraveModel<ACLivingEntityRenderState>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new BraveModel<>(modelSet.bakeLayer(BraveModel.CHARGED_LAYER_LOCATION));
    }

    protected boolean isPowered(ACLivingEntityRenderState state) {
        return false;
    }

    protected float xOffset(float t) {
        return t * 0.01F;
    }

    protected Identifier getTextureLocation() {
        return POWER_LOCATION;
    }

    protected BraveModel<ACLivingEntityRenderState> model() {
        return this.model;
    }
}