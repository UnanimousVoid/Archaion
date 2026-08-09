package com.ratrod.archaion.registry;

import com.ratrod.archaion.Archaion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ACSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENT = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Archaion.MODID);

    // Brave
    public static final DeferredHolder<SoundEvent, SoundEvent> BRAVE_AMBIENT = register("brave_ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> BRAVE_HURT = register("brave_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> BRAVE_DEATH = register("brave_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> BRAVE_JUMP = register("brave_jump");

    // Echo Mace
    public static final DeferredHolder<SoundEvent, SoundEvent> ECHO_MACE_THROW = register("echo_mace_throw");

    // Echo Star projectile
    public static final DeferredHolder<SoundEvent, SoundEvent> ECHO_STAR_BLAST = register("echo_star_blast");

    // Last of Deepslate
    public static final DeferredHolder<SoundEvent, SoundEvent> LOD_ACTION_START = register("lod_action_start");
    public static final DeferredHolder<SoundEvent, SoundEvent> LOD_ACTIVATE = register("lod_activate");
    public static final DeferredHolder<SoundEvent, SoundEvent> LOD_ACTIVATE_SMASH = register("lod_activate_smash");
    public static final DeferredHolder<SoundEvent, SoundEvent> LOD_AMBIENT = register("lod_ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> LOD_DEATH = register("lod_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> LOD_HURT = register("lod_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> LOD_SHOOT = register("lod_shoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> LOD_SMASH = register("lod_smash");
    public static final DeferredHolder<SoundEvent, SoundEvent> LOD_SPIN = register("lod_spin");
    public static final DeferredHolder<SoundEvent, SoundEvent> LOD_STEP = register("lod_step");

    // Deepslate Sentinel
    public static final DeferredHolder<SoundEvent, SoundEvent> SENTINEL_AMBIENT = register("sentinel_ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> SENTINEL_HURT = register("sentinel_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> SENTINEL_DEATH = register("sentinel_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> SENTINEL_START_CHARGING = register("sentinel_start_charging");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENT.register(name, () -> SoundEvent.createVariableRangeEvent(Archaion.prefix(name)));
    }
}
