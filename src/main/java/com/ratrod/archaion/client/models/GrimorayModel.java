package com.ratrod.archaion.client.models;// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.animations.GrimorayAnimations;
import com.ratrod.archaion.entities.Grimoray;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

import java.util.List;

public class GrimorayModel extends ACHierarchicalModel<Grimoray> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Archaion.prefix("grimoraymodel"), "main");
	private final ModelPart base;
	private final ModelPart arml;
	private final ModelPart thin;
	private final ModelPart armr;
	private final ModelPart thin2;

	public GrimorayModel(ModelPart root) {
		super(root);
		this.base = root.getChild("base");
		this.arml = this.base.getChild("arml");
		this.thin = this.arml.getChild("thin");
		this.armr = this.base.getChild("armr");
		this.thin2 = this.armr.getChild("thin2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(10, 28).addBox(-1.0F, -6.0F, 0.0F, 2.0F, 13.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition arml = base.addOrReplaceChild("arml", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -7.0F, -0.4F, 9.0F, 13.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 24).addBox(9.6F, -7.0F, -2.4F, 0.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 1.0F, 0.4F));

		PartDefinition thin = arml.addOrReplaceChild("thin", CubeListBuilder.create().texOffs(20, 0).addBox(0.0F, -6.5F, 0.2F, 9.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, -0.6F, 0.0F, 0.0873F, 0.0F));

		PartDefinition armr = base.addOrReplaceChild("armr", CubeListBuilder.create().texOffs(0, 14).addBox(-9.0F, -7.0F, -0.4F, 9.0F, 13.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 28).addBox(-9.6F, -7.0F, -2.4F, 0.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 1.0F, 0.4F));

		PartDefinition thin2 = armr.addOrReplaceChild("thin2", CubeListBuilder.create().texOffs(20, 12).addBox(-9.0F, -6.5F, 0.2F, 9.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, -0.6F, 0.0F, -0.0873F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Grimoray entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.parts().forEach(ModelPart::resetPose);

		this.animateScaled(GrimorayAnimations.IDLE, ageInTicks, 1.0F, 1.0F);
		this.animateManager(entity, ageInTicks);
	}

	@Override
	public ModelPart root() {
		return this.base;
	}

	public List<ModelPart> parts() {
		return ObjectArrayList.of(base, arml, thin, armr, thin2);
	}
}
