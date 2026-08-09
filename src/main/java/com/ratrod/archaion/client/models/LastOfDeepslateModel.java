package com.ratrod.archaion.client.models;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.LastOfDeepslateRenderState;
import com.ratrod.archaion.client.animations.LastOfDeepslateAnimations;
import com.ratrod.archaion.entities.ai.SleepingState;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class LastOfDeepslateModel<S extends LastOfDeepslateRenderState> extends ACAnimatedModel<S> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Archaion.prefix("lastofdeepslatemodel"), "main");
    private final ModelPart root;
    private final ModelPart bod;
    private final ModelPart upperbod;
    private final ModelPart centeredbod;
    private final ModelPart core;
    private final ModelPart armr;
    private final ModelPart palmr;
    private final ModelPart arml;
    private final ModelPart palml;
    private final ModelPart legr;
    private final ModelPart legl;

    private final KeyframeAnimation walkUpperAnimation;
    private final KeyframeAnimation walkLowerAnimation;

    public LastOfDeepslateModel(ModelPart root) {
        super(root);
        this.root = root.getChild("root");

        this.walkUpperAnimation = LastOfDeepslateAnimations.WALK_UPPER.bake(root);
        this.walkLowerAnimation = LastOfDeepslateAnimations.WALK_LOWER.bake(root);

        this.bod = this.root.getChild("bod");
        this.upperbod = this.bod.getChild("upperbod");
        this.centeredbod = this.upperbod.getChild("centeredbod");
        this.core = this.centeredbod.getChild("core");
        this.armr = this.centeredbod.getChild("armr");
        this.palmr = this.armr.getChild("palmr");
        this.arml = this.centeredbod.getChild("arml");
        this.palml = this.arml.getChild("palml");
        this.legr = this.root.getChild("legr");
        this.legl = this.root.getChild("legl");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(3.0F, -33.75F, -2.4167F));

        PartDefinition bod = root.addOrReplaceChild("bod", CubeListBuilder.create().texOffs(290, 200).addBox(-19.0F, -14.0F, -10.0F, 38.0F, 15.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 31.75F, 2.4167F));

        PartDefinition upperbod = bod.addOrReplaceChild("upperbod", CubeListBuilder.create(), PartPose.offset(0.0F, -13.0F, 0.0F));

        PartDefinition centeredbod = upperbod.addOrReplaceChild("centeredbod", CubeListBuilder.create().texOffs(130, 0).addBox(-32.0F, -34.0F, -32.0F, 16.0F, 64.0F, 64.0F, new CubeDeformation(0.0F))
                .texOffs(130, 128).addBox(16.0F, -34.0F, -32.0F, 16.0F, 64.0F, 64.0F, new CubeDeformation(0.0F))
                .texOffs(290, 0).addBox(-16.0F, 14.0F, -32.0F, 32.0F, 16.0F, 42.0F, new CubeDeformation(0.0F))
                .texOffs(0, 256).addBox(-16.0F, -34.0F, -32.0F, 32.0F, 16.0F, 64.0F, new CubeDeformation(0.0F))
                .texOffs(0, 161).addBox(-16.0F, -18.0F, 1.0F, 32.0F, 48.0F, 31.0F, new CubeDeformation(0.0F))
                .texOffs(292, 335).addBox(-16.0F, -46.0F, -30.0F, 32.0F, 12.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -30.0F, 0.0F));

        PartDefinition core = centeredbod.addOrReplaceChild("core", CubeListBuilder.create().texOffs(290, 58).addBox(-22.0F, -20.0F, -14.0F, 44.0F, 43.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -22.0F));

        PartDefinition armr = centeredbod.addOrReplaceChild("armr", CubeListBuilder.create().texOffs(192, 256).addBox(-24.0F, -20.0F, -14.0F, 22.0F, 102.0F, 28.0F, new CubeDeformation(0.0F))
                .texOffs(290, 123).addBox(-37.0F, -4.0F, -14.0F, 13.0F, 49.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offset(-38.0F, -26.0F, 0.0F));

        PartDefinition palmr = armr.addOrReplaceChild("palmr", CubeListBuilder.create(), PartPose.offset(-14.0F, 73.0F, 0.0F));

        PartDefinition arml = centeredbod.addOrReplaceChild("arml", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, -34.0F, -16.0F, 33.0F, 129.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(38.0F, -26.0F, 0.0F));

        PartDefinition palml = arml.addOrReplaceChild("palml", CubeListBuilder.create(), PartPose.offset(19.0F, 83.0F, 0.0F));

        PartDefinition legr = root.addOrReplaceChild("legr", CubeListBuilder.create().texOffs(292, 235).addBox(-17.0F, -2.0F, -12.0F, 22.0F, 28.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(-20.0F, 31.75F, 2.4167F));

        PartDefinition legl = root.addOrReplaceChild("legl", CubeListBuilder.create().texOffs(292, 235).addBox(-6.0F, -2.0F, -11.0F, 22.0F, 28.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(15.0F, 31.75F, 2.4167F));

        return LayerDefinition.create(meshdefinition, 512, 512);
    }

    @Override
    public void setupAnim(S state) {
        super.setupAnim(state);

        if (state.sleepingState == SleepingState.SLEEPING) {
            this.animateScaled(LastOfDeepslateAnimations.SLEEPING, state.ageInTicks, 1.0F, 1.0F);
        } else if (state.sleepingState == SleepingState.AWAKE) {
            this.animateScaled(LastOfDeepslateAnimations.IDLE_UPPER, state.ageInTicks, 1.0F, 1.0F);
        }
        walkUpperAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 2.0F, 2.5F);
        walkLowerAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 2.0F, 2.5F);
        this.animateManager(state, state.ageInTicks);
    }
}
