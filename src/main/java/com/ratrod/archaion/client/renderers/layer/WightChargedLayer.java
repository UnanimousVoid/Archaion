package com.ratrod.archaion.client.renderers.layer;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.entities.Wight;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

public class WightChargedLayer extends EnergySwirlLayer<Wight, SkeletonModel<Wight>> {

    private static final ResourceLocation POWER_LOCATION = Archaion.prefix("textures/entity/brave_charged.png");
    private final SkeletonModel<Wight> model;

    public WightChargedLayer(RenderLayerParent<Wight, SkeletonModel<Wight>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new SkeletonModel<>(modelSet.bakeLayer(ModelLayers.SKELETON));
    }

    protected float xOffset(float t) {
        return t * 0.01F;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected SkeletonModel<Wight> model() {
        return this.model;
    }
}
