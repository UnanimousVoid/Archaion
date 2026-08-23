package com.ratrod.archaion.api.gui;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
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
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (UIElement<?> element : uiElements) {
            element.setupRender(guiGraphics, mouseX, mouseY, partialTick);
        }
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void tick() {
        super.tick();
        for (UIElement<?> element : uiElements) {
            element.tick();
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        for (UIElement<?> element : uiElements) {
            element.setupClick((int) event.x(), (int) event.y());
        }
        return super.mouseClicked(event, doubleClick);
    }
}
