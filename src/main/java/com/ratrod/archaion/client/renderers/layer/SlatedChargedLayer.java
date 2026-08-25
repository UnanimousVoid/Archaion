package com.ratrod.archaion.client.renderers.layer;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.entities.Slated;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

public class SlatedChargedLayer extends EnergySwirlLayer<Slated, ZombieModel<Slated>> {

    private static final ResourceLocation POWER_LOCATION = Archaion.prefix("textures/entity/brave_charged.png");
    private final ZombieModel<Slated> model;

    public SlatedChargedLayer(RenderLayerParent<Slated, ZombieModel<Slated>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new ZombieModel<>(modelSet.bakeLayer(ModelLayers.ZOMBIE));
    }

    protected float xOffset(float t) {
        return t * 0.01F;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected ZombieModel<Slated> model() {
        return this.model;
    }
}
