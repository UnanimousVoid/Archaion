package com.ratrod.archaion.client.misc;

import com.ratrod.archaion.registry.ACItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;

public class ClientEchoChargeRequiredTooltip implements ClientTooltipComponent {

    private final Component text;

    public ClientEchoChargeRequiredTooltip(EchoChargeRequiredTooltip component) {
        this.text = Component.translatable("tooltip.archaion.last_of_deepslate.echo_charge_required", component.requiredCharges()).withStyle(ChatFormatting.RED);
    }

    @Override
    public int getWidth(Font font) {
        return 16 + 4 + font.width(this.text);
    }

    @Override
    public int getHeight(Font font) {
        return 18;
    }

    @Override
    public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
        graphics.item(ACItems.ECHO_CHARGE.get().getDefaultInstance(), x, y);
        graphics.text(font, this.text, x + 20, y + 4, 0xFFFFFFFF);
    }
}
