package com.ratrod.archaion.misc.mixinhelpers;

import it.unimi.dsi.fastutil.Pair;
import com.ratrod.archaion.entities.ai.ACEntity;

public interface IMixinMob {

    void ac$setDamageModifier(Pair<Float, ACEntity.Operation> modifier);

    Pair<Float, ACEntity.Operation> ac$getDamageModifier();
}
