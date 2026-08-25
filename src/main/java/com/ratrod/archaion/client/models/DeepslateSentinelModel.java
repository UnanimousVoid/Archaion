package com.ratrod.archaion.client.models;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.animations.DeepslateSentinelAnimations;
import com.ratrod.archaion.entities.DeepslateSentinel;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

import java.util.List;

public class DeepslateSentinelModel extends ACHierarchicalModel<DeepslateSentinel> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Archaion.prefix("deepslatesentinelmodel"), "main");

	private final ModelPart base;
	private final ModelPart head;
	private final ModelPart lower_beak;
	private final ModelPart body;
	private final ModelPart front_left_leg;
	private final ModelPart front_right_leg;
	private final ModelPart back_left_leg;
	private final ModelPart back_right_leg;

	public DeepslateSentinelModel(ModelPart root) {
		super(root);

		this.base = root.getChild("base");
		this.head = this.base.getChild("head");
		this.lower_beak = this.base.getChild("lower_beak");
		this.body = this.base.getChild("body");
		this.front_left_leg = this.base.getChild("front_left_leg");
		this.front_right_leg = this.base.getChild("front_right_leg");
		this.back_left_leg = this.base.getChild("back_left_leg");
		this.back_right_leg = this.base.getChild("back_right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = base.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 82).addBox(-20.1F, -26.1F, -4.3F, 40.0F, 39.0F, 4.0F, new CubeDeformation(0.5F))
				.texOffs(88, 92).addBox(-6.4F, -1.4F, -9.0F, 13.0F, 22.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(88, 82).addBox(-16.4F, -11.9F, -8.0F, 33.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -23.4F));

		PartDefinition lower_beak = base.addOrReplaceChild("lower_beak", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, -38.4F));

		PartDefinition body = base.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-16.1F, -17.1F, -24.1F, 32.0F, 34.0F, 48.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 1.2F, 0.0F));

		PartDefinition front_left_leg = base.addOrReplaceChild("front_left_leg", CubeListBuilder.create().texOffs(88, 121).addBox(-3.8F, -1.2F, -4.8F, 8.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, 13.2F, -18.0F));

		PartDefinition front_right_leg = base.addOrReplaceChild("front_right_leg", CubeListBuilder.create().texOffs(88, 121).addBox(-3.8F, -1.2F, -4.8F, 8.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 13.2F, -18.0F));

		PartDefinition back_left_leg = base.addOrReplaceChild("back_left_leg", CubeListBuilder.create().texOffs(88, 121).addBox(-3.8F, -1.2F, -4.8F, 8.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, 13.2F, 18.0F));

		PartDefinition back_right_leg = base.addOrReplaceChild("back_right_leg", CubeListBuilder.create().texOffs(88, 121).addBox(-3.8F, -1.2F, -4.8F, 8.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 13.2F, 18.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(DeepslateSentinel entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.parts().forEach(ModelPart::resetPose);

		this.animateManager(entity, ageInTicks);
		this.animateWalk(DeepslateSentinelAnimations.WALK, limbSwing, limbSwingAmount, 3.0F, 2.0F);
	}

	@Override
	public ModelPart root() {
		return this.base;
	}

	public List<ModelPart> parts() {
		return ObjectArrayList.of(base, head, lower_beak, body, front_left_leg, front_right_leg, back_left_leg, back_right_leg);
	}

}