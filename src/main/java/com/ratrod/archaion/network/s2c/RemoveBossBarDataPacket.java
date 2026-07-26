package com.ratrod.archaion.network.s2c;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.misc.ClientBossBarData;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record RemoveBossBarDataPacket(UUID bossBarId) implements CustomPacketPayload {
    public static final Type<RemoveBossBarDataPacket> TYPE = new Type<>(Archaion.prefix("remove_boss_bar_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RemoveBossBarDataPacket> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            RemoveBossBarDataPacket::bossBarId,
            RemoveBossBarDataPacket::new
    );

    @Override
    public Type<RemoveBossBarDataPacket> type() {
        return TYPE;
    }

    public static void handle(RemoveBossBarDataPacket packet, IPayloadContext context) {
        ClientBossBarData.removeBossBar(packet.bossBarId());
    }
}
