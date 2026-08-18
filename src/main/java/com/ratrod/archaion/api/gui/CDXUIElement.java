package com.ratrod.archaion.api.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;

public abstract class CDXUIElement<T extends Screen> {
    protected final Minecraft minecraft;
    protected final T screen;
    protected int x;
    protected int y;

    protected CDXUIElement(T screen) {
        this.screen = screen;
        this.minecraft = Minecraft.getInstance();
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public T getScreen() {
        return screen;
    }

    public abstract int getWidth();

    public abstract int getHeight();

    public int getRenderX() {
        return x;
    }

    public int getRenderY() {
        return y;
    }

    public abstract void render(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick);

    public void tick() {
    }

    public void onClick() {
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= getRenderX() && mouseX <= getRenderX() + getWidth() && mouseY >= getRenderY() && mouseY <= getRenderY() + getHeight();
    }

    public void setupClick(int mouseX, int mouseY) {
        if (isHovered(mouseX, mouseY)) {
            this.onClick();
        }
    }

    public void setupRender(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}