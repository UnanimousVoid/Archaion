package com.ratrod.archaion.effect;

import com.ratrod.archaion.Archaion;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ArmorBreakEffect extends MobEffect {

    private static final double REDUCTION_PER_LEVEL = 0.05;

    public ArmorBreakEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(Attributes.ARMOR, Archaion.prefix("armor_break_armor"), -REDUCTION_PER_LEVEL, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, Archaion.prefix("armor_break_toughness"), -REDUCTION_PER_LEVEL, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
