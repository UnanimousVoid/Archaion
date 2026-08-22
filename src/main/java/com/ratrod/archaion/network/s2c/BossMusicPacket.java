package com.ratrod.archaion.network.s2c;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.misc.LODSoundInstance;
import com.ratrod.archaion.misc.LODTheme;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BossMusicPacket(LODTheme theme) implements CustomPacketPayload {

    public static final Type<BossMusicPacket> TYPE = new Type<>(Archaion.prefix("boss_music"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BossMusicPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.idMapper(id -> LODTheme.values()[id], Enum::ordinal),
            BossMusicPacket::theme,
            BossMusicPacket::new
    );

    public static void handle(BossMusicPacket packet, IPayloadContext context) {
        switch (packet.theme()) {
            case PHASE_1, PHASE_2, PHASE_3 -> LODSoundInstance.startPhase(packet.theme());
            case STOP -> LODSoundInstance.fadeOut();
        }
    }

    @Override
    public Type<BossMusicPacket> type() {
        return TYPE;
    }
}
