package com.ratrod.archaion.entities;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface Archaic {

    int CHARGED_XP_MULTIPLIER = 20;

    boolean isCharged();

    void setCharged(boolean charged);

    @Nullable UUID getOwnerUUID();

    void setOwnerUUID(@Nullable UUID ownerUUID);

    default int archaicXpReward(int baseXp) {
        return baseXp * (this.isCharged() ? CHARGED_XP_MULTIPLIER : 1);
    }
}
