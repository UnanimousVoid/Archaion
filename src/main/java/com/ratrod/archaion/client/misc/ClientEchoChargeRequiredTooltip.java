package com.ratrod.archaion.client.misc;

import com.ratrod.archaion.registry.ACItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;

public class ClientEchoChargeRequiredTooltip implements ClientTooltipComponent {

    private final Component text;

    public ClientEchoChargeRequiredTooltip(EchoChargeRequiredTooltip component) {
        this.text = Component.translatable("misc.archaion.last_of_deepslate.echo_charge_required", component.requiredCharges()).withStyle(ChatFormatting.RED);
    }

    @Override
    public int getWidth(Font font) {
        return 16 + 4 + font.width(this.text);
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        graphics.renderItem(ACItems.ECHO_CHARGE.get().getDefaultInstance(), x, y);
        graphics.drawString(font, this.text, x + 20, y + 4, 0xFFFFFFFF);
    }
}
