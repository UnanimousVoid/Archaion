package com.ratrod.archaion.network;

import java.util.HashMap;
import java.util.Map;

public class BossBarDataOutput {

    private final Map<String, Integer> values = new HashMap<>();

    public BossBarDataOutput add(String key, int value) {
        this.values.put(key, value);
        return this;
    }

    public Map<String, Integer> build() {
        return Map.copyOf(this.values);
    }
}
