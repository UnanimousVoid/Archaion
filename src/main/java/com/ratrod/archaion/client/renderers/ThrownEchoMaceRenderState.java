package com.ratrod.archaion.client.renderers;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class ThrownEchoMaceRenderState extends EntityRenderState {
    public final ItemStackRenderState item = new ItemStackRenderState();
    public float velocity;
    public float yRot;
    public float xRot;
}
