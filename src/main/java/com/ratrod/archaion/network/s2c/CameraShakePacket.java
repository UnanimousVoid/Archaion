package com.ratrod.archaion.network.s2c;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.api.camera.ScreenShakeHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CameraShakePacket(float intensity, int duration, float frequency) implements CustomPacketPayload {
    public static final Type<CameraShakePacket> TYPE = new Type<>(Archaion.prefix("camera_shake"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CameraShakePacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            CameraShakePacket::intensity,
            ByteBufCodecs.INT,
            CameraShakePacket::duration,
            ByteBufCodecs.FLOAT,
            CameraShakePacket::frequency,
            CameraShakePacket::new
    );

    public static void handle(CameraShakePacket packet, IPayloadContext context) {
        ScreenShakeHandler.shakeLocal(packet.intensity(), packet.duration(), packet.frequency());
    }

    @Override
    public Type<CameraShakePacket> type() {
        return TYPE;
    }
}
