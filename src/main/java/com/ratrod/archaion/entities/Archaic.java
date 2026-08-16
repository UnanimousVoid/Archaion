package com.ratrod.archaion.entities;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface Archaic {

    boolean isCharged();

    void setCharged(boolean charged);

    @Nullable UUID getOwnerUUID();

    void setOwnerUUID(@Nullable UUID ownerUUID);
}
