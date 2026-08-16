package com.ratrod.archaion.client.models;// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.client.renderers.renderstate.BraveRenderState;
import com.ratrod.archaion.client.animations.BraveAnimations;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class BraveModel<S extends BraveRenderState> extends ACAnimatedModel<S> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Archaion.prefix("brave"), "main");
	public static final ModelLayerLocation CHARGED_LAYER_LOCATION = new ModelLayerLocation(Archaion.prefix("chargedbrave"), "main");
	private final ModelPart base;
	private final ModelPart head;
	private final ModelPart rods;
	private final ModelPart rod2;
	private final ModelPart rod1;
	private final ModelPart rodgroup;
	private final ModelPart rod3;
	private final ModelPart rod4;
	private final ModelPart ring;

	public BraveModel(ModelPart root) {
		super(root);
		this.base = root.getChild("base");
		this.head = this.base.getChild("head");
		this.rods = this.base.getChild("rods");
		this.rod2 = this.rods.getChild("rod2");
		this.rod1 = this.rods.getChild("rod1");
		this.rodgroup = this.rods.getChild("rodgroup");
		this.rod3 = this.rodgroup.getChild("rod3");
		this.rod4 = this.rodgroup.getChild("rod4");
		this.ring = this.base.getChild("ring");
	}

	public static LayerDefinition createBodyLayer(CubeDeformation deform) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 0.0F));

		PartDefinition head = base.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 30).addBox(-6.0F, -26.0F, -6.0F, 12.0F, 26.0F, 12.0F, deform), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition rods = base.addOrReplaceChild("rods", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition rod2 = rods.addOrReplaceChild("rod2", CubeListBuilder.create().texOffs(48, 30).addBox(-2.6F, -5.0F, -2.6F, 5.0F, 10.0F, 5.0F, deform), PartPose.offset(0.0F, -1.0F, 14.6F));

		PartDefinition rod1 = rods.addOrReplaceChild("rod1", CubeListBuilder.create().texOffs(48, 30).addBox(-2.6F, -5.0F, -2.4F, 5.0F, 10.0F, 5.0F, deform), PartPose.offset(0.0F, -1.0F, -14.6F));

		PartDefinition rodgroup = rods.addOrReplaceChild("rodgroup", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -1.0F, -0.4F, 0.0F, -1.5708F, 0.0F));

		PartDefinition rod3 = rodgroup.addOrReplaceChild("rod3", CubeListBuilder.create().texOffs(48, 30).addBox(-2.6F, -5.0F, -2.6F, 5.0F, 10.0F, 5.0F, deform), PartPose.offset(0.0F, 0.0F, 15.0F));

		PartDefinition rod4 = rodgroup.addOrReplaceChild("rod4", CubeListBuilder.create().texOffs(48, 30).addBox(-2.6F, -5.0F, -2.4F, 5.0F, 10.0F, 5.0F, deform), PartPose.offset(0.0F, 0.0F, -14.2F));

		PartDefinition ring = base.addOrReplaceChild("ring", CubeListBuilder.create().texOffs(1, 0).addBox(-15.0F, 0.0F, -15.0F, 30.0F, 0.0F, 30.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(S state) {
		super.setupAnim(state);

		this.ring.yRot = state.ageInTicks * 0.2F;
		this.ring.zRot = Mth.sin(state.ageInTicks * 0.2F) * 15 * Mth.DEG_TO_RAD;

		this.rods.yRot = -state.ageInTicks * 0.2F;
		this.rod1.y = -3 + Mth.sin(state.ageInTicks * 0.1F) * 3;
		this.rod2.y = -3 + Mth.sin((state.ageInTicks + 20) * 0.1F) * 3;
		this.rod3.y = -3 + Mth.sin((state.ageInTicks + 40) * 0.1F) * 3;
		this.rod4.y = -3 + Mth.sin((state.ageInTicks + 80) * 0.1F) * 3;

		this.animateScaled(BraveAnimations.IDLE_HEAD, state.ageInTicks, 1.0F, 1.0F);
		this.animateManager(state, state.ageInTicks);
	}
}