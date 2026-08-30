package com.ratrod.archaion.client.renderers.renderstate;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class TeleporterRenderState extends BlockEntityRenderState {
    public int colorArgb = 0xFFFFFFFF;
    public boolean disabled = false;
    public float ageInTicks = 0.0F;
    public int maxHeight = 35;
    public int cooldown = 0;
}