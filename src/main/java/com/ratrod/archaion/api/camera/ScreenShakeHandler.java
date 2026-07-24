package com.ratrod.archaion.api.camera;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.neoforged.neoforge.client.event.ViewportEvent;

import java.util.Iterator;
import java.util.List;

public class ScreenShakeHandler {
    public static List<Instance> shakesList = new ObjectArrayList<>();

    public static void shakeLocal(float intensity, int duration, float frequency) {
        shakesList.add(new Instance(intensity, duration, frequency));
    }

    public static void clientTick() {
        Iterator<Instance> iterator = shakesList.iterator();
        while (iterator.hasNext()) {
            Instance instance = iterator.next();
            if (instance.isDone()) {
                iterator.remove();
            } else {
                instance.tick();
            }
        }
    }

    public static void modifyCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        for (Instance instance : shakesList) {
            float[] offsets = instance.getOffsets(event.getPartialTick());
            event.setPitch(event.getPitch() + offsets[0]);
            event.setYaw(event.getYaw() + offsets[1]);
        }
    }

    public static class Instance {
        public float intensity;
        public float maxDuration;
        public float duration;
        public float frequency;

        public Instance(float intensity, int duration, float frequency) {
            this.intensity = intensity;
            this.maxDuration = duration;
            this.duration = duration;
            this.frequency = frequency;
        }

        public boolean isDone() {
            return duration <= 0;
        }

        public void tick() {
            duration--;
        }

        public float[] getOffsets(double partialTicks) {
            float currentTime = (maxDuration - duration + (float) partialTicks);
            float fadeOut = duration / maxDuration;
            float pitchOffset = (float)(Math.sin(currentTime * frequency * 0.1) * intensity * fadeOut);
            float yawOffset = (float)(Math.cos(currentTime * frequency * 0.1 + Math.PI / 4) * intensity * fadeOut);
            return new float[]{pitchOffset, yawOffset};
        }
    }
}
