package com.ratrod.archaion.client.particles;

import mod.chloeprime.aaaparticles.api.client.EffectDefinition;
import mod.chloeprime.aaaparticles.api.client.effekseer.ParticleEmitter;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * A custom particle emitter that attaches AAAParticles {@link ParticleEmitter} instances
 * to specific {@link ModelPart} targets and follows their world-space positions each frame.
 * <p>
 * The emitter walks the ModelPart hierarchy (after animation transforms have been applied)
 * and accumulates translation/rotation to compute the local-space position of each target part.
 * It then rotates by entity body yaw and offsets by entity world position.
 * <p>
 * Usage:
 * <pre>{@code
 * // In your model class:
 * ModelPartParticleEmitter palmEmitter = new ModelPartParticleEmitter(
 *     effect,
 *     this.root,
 *     new String[]{"bod", "upperbod", "armr", "palmr"},
 *     new String[]{"bod", "upperbod", "arml", "palml"}
 * );
 *
 * // In setupAnim, after super.setupAnim(state):
 * palmEmitter.update(state);
 * }</pre>
 */
public class ModelPartParticleEmitter {

    private final ParticleEmitter[] emitters;
    private final ModelPart rootPart;
    private final String[][] paths;
    private boolean active = true;

    /**
     * Creates a particle emitter attached to one or more ModelPart targets.
     *
     * @param effect   the loaded {@link EffectDefinition} to spawn particles from
     * @param rootPart the root {@link ModelPart} from which to walk the hierarchy
     * @param paths    one or more paths (arrays of child names) from rootPart to each target part.
     *                 Each path creates a separate {@link ParticleEmitter} instance.
     */
    public ModelPartParticleEmitter(EffectDefinition effect, ModelPart rootPart, String[]... paths) {
        this.rootPart = rootPart;
        this.paths = paths;
        this.emitters = new ParticleEmitter[paths.length];
        for (int i = 0; i < paths.length; i++) {
            this.emitters[i] = effect.play();
        }
    }

    /**
     * Update all particle emitter positions to match the current ModelPart world-space positions.
     * Call this after animations have been applied (in {@code setupAnim}).
     *
     * @param entityPos the entity's world-space position (e.g., {@code new Vec3(state.x, state.y, state.z)})
     * @param yBodyRot  the entity's interpolated body yaw in degrees
     */
    public void update(Vec3 entityPos, float yBodyRot) {
        if (!active) {
            return;
        }
        float yawRad = yBodyRot * (float) Math.PI / 180F;
        for (int i = 0; i < paths.length; i++) {
            Vec3 local = computeLocalPos(rootPart, paths[i]);
            // Apply entity body rotation and world offset
            Vec3 world = local.yRot(-yawRad).add(entityPos);
            emitters[i].setPosition((float) world.x, (float) world.y, (float) world.z);
        }
    }

    /**
     * Set visibility of all emitters.
     */
    public void setVisible(boolean visible) {
        for (ParticleEmitter emitter : emitters) {
            emitter.setVisibility(visible);
        }
    }

    /**
     * Stop and invalidate all emitters.
     */
    public void stop() {
        active = false;
        for (ParticleEmitter emitter : emitters) {
            emitter.stop();
        }
    }

    /**
     * Check if this emitter is still active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Get a specific emitter by index.
     */
    public ParticleEmitter getEmitter(int index) {
        return emitters[index];
    }

    /**
     * Compute the local-space position of a ModelPart by walking the hierarchy from root
     * and accumulating translation and rotation transforms. The result is in block-space
     * (ModelPart translations are divided by 16).
     */
    public static Vec3 computeLocalPos(ModelPart root, String[] path) {
        Matrix4f mat = new Matrix4f();
        ModelPart current = root;

        // Walk the path, applying each part's transform
        for (String name : path) {
            applyPartTransform(mat, current);
            current = current.getChild(name);
        }
        // Apply the final (leaf) part's transform
        applyPartTransform(mat, current);

        Vector3f pos = mat.transformPosition(new Vector3f(0, 0, 0));
        return new Vec3(pos.x, pos.y, pos.z);
    }

    /**
     * Apply a ModelPart's current translation and rotation to the given matrix.
     * Matches Minecraft's {@code ModelPart.translateAndRotate} order:
     * Z-rotation → Y-rotation → X-rotation → translation (in 1/16 units).
     */
    public static void applyPartTransform(Matrix4f mat, ModelPart part) {
        mat.rotateZ(part.zRot);
        mat.rotateY(part.yRot);
        mat.rotateX(part.xRot);
        mat.translate(part.x / 16F, part.y / 16F, part.z / 16F);
    }
}
