package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.layers.LivingEntityEmissiveLayer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class SlatedRenderer extends ZombieRenderer {
    private static final Identifier TEXTURE = Archaion.prefix("textures/entity/slated.png");
    private static final Identifier GLOW_TEXTURE = Archaion.prefix("textures/entity/slated_glow.png");

    public SlatedRenderer(EntityRendererProvider.Context context) {
        super(context, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_BABY, ModelLayers.ZOMBIE_ARMOR, ModelLayers.ZOMBIE_BABY_ARMOR);
        this.addLayer(
                new LivingEntityEmissiveLayer<>(
                        this,
                        renderState -> GLOW_TEXTURE,
                        (entity, ageInTicks) -> 1.0F,
                        new ZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE)),
                        RenderTypes::entityTranslucentEmissive,
                        false
                )
        );
    }

    public Identifier getTextureLocation(ZombieRenderState state) {
        return TEXTURE;
    }
}