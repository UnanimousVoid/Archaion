package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.entities.ai.SleepingState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ACEntityDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, Archaion.MODID);

    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<SleepingState>> SLEEPING_STATE = ENTITY_DATA_SERIALIZERS.register("sleeping_state",
            () -> EntityDataSerializer.forValueType(StreamCodec.of(FriendlyByteBuf::writeEnum, buf -> buf.readEnum(SleepingState.class)))
    );
}