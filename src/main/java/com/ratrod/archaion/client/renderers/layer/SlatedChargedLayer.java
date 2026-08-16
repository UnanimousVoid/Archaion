package com.ratrod.archaion.client.renderers.layer;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.renderers.renderstate.SlatedRenderState;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.Identifier;

public class SlatedChargedLayer extends EnergySwirlLayer<SlatedRenderState, ZombieModel<SlatedRenderState>> {

    private static final Identifier POWER_LOCATION = Archaion.prefix("textures/entity/brave_charged.png");
    private final ZombieModel<SlatedRenderState> model;

    public SlatedChargedLayer(RenderLayerParent<SlatedRenderState, ZombieModel<SlatedRenderState>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new ZombieModel<>(modelSet.bakeLayer(ModelLayers.ZOMBIE));
    }

    protected boolean isPowered(SlatedRenderState state) {
        return state.isCharged;
    }

    protected float xOffset(float t) {
        return t * 0.01F;
    }

    protected Identifier getTextureLocation() {
        return POWER_LOCATION;
    }

    protected ZombieModel<SlatedRenderState> model() {
        return this.model;
    }
}
