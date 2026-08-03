package com.ratrod.archaion.client.animations;

import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.world.entity.EntityType;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ClientAnimationRegistry {
    private static final Map<EntityType<?>, List<AnimationDefinition>> DEFINITIONS = new HashMap<>();

    private ClientAnimationRegistry() {
    }

    public static void register(EntityType<?> type, List<AnimationDefinition> definitions) {
        DEFINITIONS.put(type, definitions);
    }

    @Nullable
    public static AnimationDefinition get(EntityType<?> type, int id) {
        List<AnimationDefinition> definitions = DEFINITIONS.get(type);
        if (definitions == null || id < 0 || id >= definitions.size()) {
            return null;
        }
        return definitions.get(id);
    }
}
