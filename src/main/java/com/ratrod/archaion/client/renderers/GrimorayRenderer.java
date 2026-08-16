package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.models.GrimorayModel;
import com.ratrod.archaion.client.renderers.renderstate.GrimorayRenderState;
import com.ratrod.archaion.entities.Grimoray;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class GrimorayRenderer extends MobRenderer<Grimoray, GrimorayRenderState, GrimorayModel<GrimorayRenderState>> {

    private static final Identifier TEXTURE_POISON = Archaion.prefix("textures/entity/grimoray_poison.png");
    private static final Identifier TEXTURE_HARMING = Archaion.prefix("textures/entity/grimoray_harming.png");
    private static final Identifier TEXTURE_HEALING = Archaion.prefix("textures/entity/grimoray_healing.png");

    public GrimorayRenderer(EntityRendererProvider.Context context) {
        super(context, new GrimorayModel<>(context.bakeLayer(GrimorayModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public Identifier getTextureLocation(GrimorayRenderState state) {
        return switch (state.grimorayType) {
            case POISON_CLOUD -> TEXTURE_POISON;
            case HARMING ->  TEXTURE_HARMING;
            default -> TEXTURE_HEALING;
        };
    }

    @Override
    public GrimorayRenderState createRenderState() {
        return new GrimorayRenderState();
    }

    @Override
    public void extractRenderState(Grimoray entity, GrimorayRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.animationManager = entity.getAnimationManager();
        state.grimorayType = entity.getGrimorayType();
    }
}
