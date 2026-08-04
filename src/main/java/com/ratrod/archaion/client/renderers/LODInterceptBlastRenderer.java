package com.ratrod.archaion.client.renderers;

import com.ratrod.archaion.entities.LODInterceptBlast;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class LODInterceptBlastRenderer extends EntityRenderer<LODInterceptBlast, EntityRenderState> {

    public LODInterceptBlastRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public boolean shouldRender(LODInterceptBlast entity, Frustum culler, double camX, double camY, double camZ) {
        return false;
    }
}
