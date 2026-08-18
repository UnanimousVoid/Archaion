package com.ratrod.archaion.network.s2c;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.misc.LODSoundInstance;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BossMusicPacket(boolean start) implements CustomPacketPayload {
    public static final Type<BossMusicPacket> TYPE = new Type<>(Archaion.prefix("boss_music"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BossMusicPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            BossMusicPacket::start,
            BossMusicPacket::new
    );

    public static void handle(BossMusicPacket packet, IPayloadContext context) {
        if (packet.start()) {
            LODSoundInstance.start();
        } else {
            LODSoundInstance.fadeOut();
        }
    }

    @Override
    public Type<BossMusicPacket> type() {
        return TYPE;
    }
}
