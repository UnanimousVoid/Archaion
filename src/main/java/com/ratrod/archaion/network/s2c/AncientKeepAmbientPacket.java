package com.ratrod.archaion.network.s2c;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.misc.AncientKeepClientData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AncientKeepAmbientPacket(AABB box) implements CustomPacketPayload {
    public static final Type<AncientKeepAmbientPacket> TYPE = new Type<>(Archaion.prefix("ancient_keep_ambient"));

    private static final StreamCodec<RegistryFriendlyByteBuf, AABB> AABB_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, a -> a.minX,
            ByteBufCodecs.DOUBLE, a -> a.minY,
            ByteBufCodecs.DOUBLE, a -> a.minZ,
            ByteBufCodecs.DOUBLE, a -> a.maxX,
            ByteBufCodecs.DOUBLE, a -> a.maxY,
            ByteBufCodecs.DOUBLE, a -> a.maxZ,
            AABB::new
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, AncientKeepAmbientPacket> CODEC = StreamCodec.composite(
            AABB_CODEC,
            AncientKeepAmbientPacket::box,
            AncientKeepAmbientPacket::new
    );

    public static void handle(AncientKeepAmbientPacket packet, IPayloadContext context) {
        AncientKeepClientData.ANCIENT_KEEP_BOX = packet.box();
    }

    @Override
    public Type<AncientKeepAmbientPacket> type() {
        return TYPE;
    }
}
