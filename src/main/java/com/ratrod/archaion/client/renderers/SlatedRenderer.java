package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.renderers.layer.ACEmissiveLayer;
import com.ratrod.archaion.client.renderers.layer.SlatedChargedLayer;
import com.ratrod.archaion.entities.Slated;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SlatedRenderer extends AbstractZombieRenderer<Slated, ZombieModel<Slated>> {
    private static final ResourceLocation TEXTURE = Archaion.prefix("textures/entity/slated.png");
    private static final ResourceLocation GLOW_TEXTURE = Archaion.prefix("textures/entity/slated_glow.png");

    public SlatedRenderer(EntityRendererProvider.Context context) {
        super(context,
                new ZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE)),
                new ZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)),
                new ZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR))
        );
        this.addLayer(new ACEmissiveLayer<>(this, new ZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), GLOW_TEXTURE, entity -> 1.0F));
        this.addLayer(new SlatedChargedLayer(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(Slated entity) {
        return TEXTURE;
    }
}
