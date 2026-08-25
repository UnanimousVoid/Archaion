package com.ratrod.archaion.api.gui;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class AnimatedScreen extends Screen {
    public final ObjectArrayList<UIElement<?>> uiElements = new ObjectArrayList<>();

    protected AnimatedScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.uiElements.clear();
        this.initUIElements();
    }

    public abstract void initUIElements();

    public <T extends UIElement<?>> T addUIElement(T element) {
        this.uiElements.add(element);
        return element;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (UIElement<?> element : uiElements) {
            element.setupRender(guiGraphics, mouseX, mouseY, partialTick);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void tick() {
        super.tick();
        for (UIElement<?> element : uiElements) {
            element.tick();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (UIElement<?> element : uiElements) {
            element.setupClick((int) mouseX, (int) mouseY);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
