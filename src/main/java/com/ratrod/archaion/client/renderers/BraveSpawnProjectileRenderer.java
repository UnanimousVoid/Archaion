package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.entities.BraveSpawnProjectile;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class BraveSpawnProjectileRenderer extends EntityRenderer<BraveSpawnProjectile, EntityRenderState> {

    public BraveSpawnProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public boolean shouldRender(BraveSpawnProjectile entity, Frustum culler, double camX, double camY, double camZ) {
        return false;
    }
}
