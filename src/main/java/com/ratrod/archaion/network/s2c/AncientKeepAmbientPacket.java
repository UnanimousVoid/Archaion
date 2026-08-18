package com.ratrod.archaion.network.s2c;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.misc.AncientKeepClientData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AncientKeepAmbientPacket(BoundingBox box) implements CustomPacketPayload {
    public static final Type<AncientKeepAmbientPacket> TYPE = new Type<>(Archaion.prefix("ancient_keep_ambient"));

    public static final StreamCodec<RegistryFriendlyByteBuf, AncientKeepAmbientPacket> CODEC = StreamCodec.composite(
            BoundingBox.STREAM_CODEC,
            AncientKeepAmbientPacket::box,
            AncientKeepAmbientPacket::new
    );

    public static void handle(AncientKeepAmbientPacket packet, IPayloadContext context) {
        AncientKeepClientData.setBox(packet.box());
    }

    @Override
    public Type<AncientKeepAmbientPacket> type() {
        return TYPE;
    }
}
