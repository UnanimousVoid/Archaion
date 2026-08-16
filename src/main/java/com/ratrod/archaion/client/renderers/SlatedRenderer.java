package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.renderers.renderstate.SlatedRenderState;
import com.ratrod.archaion.client.renderers.layer.SlatedChargedLayer;
import com.ratrod.archaion.entities.Slated;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.zombie.BabyZombieModel;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.LivingEntityEmissiveLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class SlatedRenderer extends AbstractZombieRenderer<Slated, SlatedRenderState, ZombieModel<SlatedRenderState>> {
    private static final Identifier TEXTURE = Archaion.prefix("textures/entity/slated.png");
    private static final Identifier GLOW_TEXTURE = Archaion.prefix("textures/entity/slated_glow.png");

    public SlatedRenderer(EntityRendererProvider.Context context) {
        super(context,
                new ZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE)),
                new BabyZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE_BABY)),
                ArmorModelSet.bake(ModelLayers.ZOMBIE_ARMOR, context.getModelSet(), ZombieModel::new),
                ArmorModelSet.bake(ModelLayers.ZOMBIE_BABY_ARMOR, context.getModelSet(), BabyZombieModel::new)
        );
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
        this.addLayer(new SlatedChargedLayer(this, context.getModelSet()));
    }

    public Identifier getTextureLocation(SlatedRenderState state) {
        return TEXTURE;
    }

    public SlatedRenderState createRenderState() {
        return new SlatedRenderState();
    }

    @Override
    public void extractRenderState(Slated entity, SlatedRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isCharged = entity.isCharged();
    }
}
