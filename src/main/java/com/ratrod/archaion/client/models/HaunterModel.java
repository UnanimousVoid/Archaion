package com.ratrod.archaion.client.models;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.animations.HaunterAnimations;
import com.ratrod.archaion.entities.Haunter;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

import java.util.List;

public class HaunterModel extends ACHierarchicalModel<Haunter> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Archaion.prefix("hauntermodel"), "main");
    public static final ModelLayerLocation CHARGED_LAYER_LOCATION = new ModelLayerLocation(Archaion.prefix("hauntermodel"), "charged");

    private final ModelPart mob;
    private final ModelPart head;
    private final ModelPart horn2;
    private final ModelPart horn;
    private final ModelPart left_leg;
    private final ModelPart right_leg;

    public HaunterModel(ModelPart root) {
        super(root);

        this.mob = root.getChild("mob");
        this.head = this.mob.getChild("head");
        this.horn2 = this.head.getChild("horn2");
        this.horn = this.head.getChild("horn");
        this.left_leg = this.mob.getChild("left_leg");
        this.right_leg = this.mob.getChild("right_leg");
    }

    public static LayerDefinition createBodyLayer() {
        return createBodyLayer(CubeDeformation.NONE);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition mob = partdefinition.addOrReplaceChild("mob", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = mob.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -29.0F, -4.0F, 10.0F, 29.0F, 8.0F, deformation), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition horn2 = head.addOrReplaceChild("horn2", CubeListBuilder.create().texOffs(36, 15).addBox(-15.0F, -6.0F, 0.0F, 15.0F, 15.0F, 0.0F, deformation), PartPose.offset(-5.0F, -27.0F, 0.0F));

        PartDefinition horn = head.addOrReplaceChild("horn", CubeListBuilder.create().texOffs(36, 0).addBox(0.0F, -6.0F, 0.0F, 15.0F, 15.0F, 0.0F, deformation), PartPose.offset(5.0F, -27.0F, 0.0F));

        PartDefinition left_leg = mob.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(36, 30).addBox(-1.0F, 0.0F, -1.1F, 2.0F, 12.0F, 2.0F, deformation), PartPose.offset(3.0F, -12.0F, 0.1F));

        PartDefinition right_leg = mob.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 37).addBox(-1.0F, 0.0F, -1.1F, 2.0F, 12.0F, 2.0F, deformation), PartPose.offset(-3.0F, -12.0F, 0.1F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Haunter entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.parts().forEach(ModelPart::resetPose);

        this.animateScaled(HaunterAnimations.IDLE, ageInTicks, 1.0F, 1.0F);
        this.animateWalk(HaunterAnimations.WALK, limbSwing, limbSwingAmount, 3.0F, 100.0F);

        this.animateManager(entity, ageInTicks);
    }

    @Override
    public ModelPart root() {
        return this.mob;
    }

    public List<ModelPart> parts() {
        return ObjectArrayList.of(mob, head, horn2, horn, left_leg, right_leg);
    }
}