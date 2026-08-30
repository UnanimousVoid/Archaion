package com.ratrod.archaion.block;

import net.minecraft.util.StringRepresentable;

public enum TeleporterColor implements StringRepresentable {
    WHITE("white", 0xFFFFFFFF),
    RED("red", 0xFFE05050),
    ORANGE("orange", 0xFFF2A33C),
    YELLOW("yellow", 0xFFF2D53C),
    GREEN("green", 0xFF5BD75B),
    CYAN("cyan", 0xFF4FD8D8),
    BLUE("blue", 0xFF4A7DF0),
    PURPLE("purple", 0xFFB56AF0);

    private final String name;
    private final int color;

    TeleporterColor(String name, int color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public TeleporterColor next() {
        TeleporterColor[] values = values();
        return values[(this.ordinal() + 1) % values.length];
    }

    /** The ARGB color (fully opaque) used for rendering the portal. */
    public int argb() {
        return this.color;
    }
}