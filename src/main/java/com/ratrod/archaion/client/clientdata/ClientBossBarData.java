package com.ratrod.archaion.client.clientdata;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

import java.util.Map;
import java.util.UUID;

public class ClientBossBarData {
    private static final Map<UUID, DynamicData> BOSS_INDICES = new Object2ObjectArrayMap<>();

    public static void setBossIdx(UUID bossBarId, int idx) {
        BOSS_INDICES.put(bossBarId, new DynamicData(idx, Map.of()));
    }

    public static int getBossIdx(UUID bossBarId) {
        DynamicData data = BOSS_INDICES.get(bossBarId);
        return (data != null) ? data.id() : -1;
    }

    public static void removeBossBar(UUID bossBarId) {
        BOSS_INDICES.remove(bossBarId);
    }

    public record DynamicData(int id, Map<String, Integer> values) {
    }
}
