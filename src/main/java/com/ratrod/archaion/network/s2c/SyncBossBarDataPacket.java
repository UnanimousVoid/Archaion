package com.ratrod.archaion.network.s2c;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.clientdata.ClientBossBarData;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record SyncBossBarDataPacket(UUID bossBarId, int bossIdx) implements CustomPacketPayload {
    public static final Type<SyncBossBarDataPacket> TYPE = new Type<>(Archaion.prefix("sync_boss_bar_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncBossBarDataPacket> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            SyncBossBarDataPacket::bossBarId,
            ByteBufCodecs.INT,
            SyncBossBarDataPacket::bossIdx,
            SyncBossBarDataPacket::new
    );

    @Override
    public Type<SyncBossBarDataPacket> type() {
        return TYPE;
    }

    public static void handle(SyncBossBarDataPacket packet, IPayloadContext context) {
        ClientBossBarData.setBossIdx(packet.bossBarId(), packet.bossIdx());
    }
}
