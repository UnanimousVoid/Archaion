package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.models.GrimorayModel;
import com.ratrod.archaion.entities.Grimoray;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GrimorayRenderer extends MobRenderer<Grimoray, GrimorayModel> {

    private static final ResourceLocation TEXTURE_POISON = Archaion.prefix("textures/entity/grimoray_poison.png");
    private static final ResourceLocation TEXTURE_HARMING = Archaion.prefix("textures/entity/grimoray_harming.png");
    private static final ResourceLocation TEXTURE_HEALING = Archaion.prefix("textures/entity/grimoray_healing.png");

    public GrimorayRenderer(EntityRendererProvider.Context context) {
        super(context, new GrimorayModel(context.bakeLayer(GrimorayModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(Grimoray entity) {
        return switch (entity.getGrimorayType()) {
            case POISON_CLOUD -> TEXTURE_POISON;
            case HARMING -> TEXTURE_HARMING;
            default -> TEXTURE_HEALING;
        };
    }
}
