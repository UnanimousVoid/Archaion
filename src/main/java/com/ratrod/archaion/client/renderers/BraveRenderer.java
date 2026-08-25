package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.models.BraveModel;
import com.ratrod.archaion.client.renderers.layer.ACEmissiveLayer;
import com.ratrod.archaion.client.renderers.layer.BraveChargedLayer;
import com.ratrod.archaion.entities.Brave;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BraveRenderer extends MobRenderer<Brave, BraveModel> {

    private static final ResourceLocation TEXTURE_LOCATION = Archaion.prefix("textures/entity/brave.png");
    private static final ResourceLocation GLOW_TEXTURE = Archaion.prefix("textures/entity/brave_glow.png");

    public BraveRenderer(EntityRendererProvider.Context context) {
        super(context, new BraveModel(context.bakeLayer(BraveModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(
                new ACEmissiveLayer<>(
                        this,
                        new BraveModel(context.bakeLayer(BraveModel.LAYER_LOCATION)),
                        GLOW_TEXTURE,
                        entity -> Mth.clamp(1F + Mth.sin(entity.tickCount * 0.3F), 0.0F, 1.0F)
                )
        );
        this.addLayer(new BraveChargedLayer(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(Brave entity) {
        return TEXTURE_LOCATION;
    }
}