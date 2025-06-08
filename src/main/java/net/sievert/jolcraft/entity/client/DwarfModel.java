package net.sievert.jolcraft.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.sievert.jolcraft.JolCraft;

public class DwarfModel extends EntityModel<DwarfRenderState> implements ArmedModel, HeadedModel{

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "dwarf"), "main");
    private final ModelPart body;
    private final ModelPart bodywear;
    private final ModelPart left_arm;
    private final ModelPart left_leg;
    private final ModelPart left_legwear;
    private final ModelPart right_legwear;
    private final ModelPart right_arm;
    private final ModelPart right_leg;
    private final ModelPart left_armwear;
    private final ModelPart right_armwear;
    private final ModelPart head;
    private final ModelPart beard;
    private final ModelPart right_eyebrow;
    private final ModelPart left_eyebrow;
    private final ModelPart hat;

    public DwarfModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.bodywear = this.body.getChild("bodywear");
        this.left_arm = this.body.getChild("left_arm");
        this.left_leg = this.body.getChild("left_leg");
        this.left_legwear = this.body.getChild("left_legwear");
        this.right_legwear = this.body.getChild("right_legwear");
        this.right_arm = this.body.getChild("right_arm");
        this.right_leg = this.body.getChild("right_leg");
        this.left_armwear = this.body.getChild("left_armwear");
        this.right_armwear = this.body.getChild("right_armwear");
        this.head = root.getChild("head");
        this.beard = this.head.getChild("beard");
        this.right_eyebrow = this.head.getChild("right_eyebrow");
        this.left_eyebrow = this.head.getChild("left_eyebrow");
        this.hat = this.head.getChild("hat");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(30, 33).addBox(-6.0F, -7.0F, -3.0F, 12.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));

        body.addOrReplaceChild("bodywear", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 5.0F, -3.0F, 12.0F, 15.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(48, 51).addBox(-0.2F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -7.0F, 0.0F));

        body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(14, 47).addBox(-3.0F, -0.5F, -2.0F, 5.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 5.0F, 0.0F));

        body.addOrReplaceChild("left_legwear", CubeListBuilder.create().texOffs(0, 63).addBox(-3.0F, -0.5F, -2.0F, 5.0F, 7.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(3.0F, 5.0F, 0.0F));

        body.addOrReplaceChild("right_legwear", CubeListBuilder.create().texOffs(20, 69).addBox(-2.0F, -0.5F, -2.0F, 5.0F, 7.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-3.0F, 5.0F, 0.0F));

        body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 51).addBox(-3.8F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -7.0F, 0.0F));

        body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 39).addBox(-2.0F, -0.5F, -2.0F, 5.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 5.0F, 0.0F));

        body.addOrReplaceChild("left_armwear", CubeListBuilder.create().texOffs(20, 60).addBox(-0.2F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(6.0F, -7.0F, 0.0F));

        body.addOrReplaceChild("right_armwear", CubeListBuilder.create().texOffs(2, 54).addBox(-3.8F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-6.0F, -7.0F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(36, 0).addBox(-5.0F, -10.0F, -4.0F, 10.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(30, 20).addBox(-5.0F, -1.0F, -4.0F, 10.0F, 2.0F, 6.0F, new CubeDeformation(0.15F))
                .texOffs(0, 21).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

        head.addOrReplaceChild("beard", CubeListBuilder.create().texOffs(36, 28).addBox(-5.0F, -18.0F, -4.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(53, 31).addBox(-4.0F, -17.0F, -4.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(30, 2).addBox(-3.0F, -14.0F, -4.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 30).addBox(-4.0F, -16.0F, -4.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(30, 0).addBox(-3.0F, -15.0F, -4.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(30, 4).addBox(-2.0F, -13.0F, -4.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -12.0F, -4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 0.0F));

        head.addOrReplaceChild("right_eyebrow", CubeListBuilder.create().texOffs(0, 2).addBox(-3.0F, -1.0F, -0.01F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -5.0F, -4.0F));

        head.addOrReplaceChild("left_eyebrow", CubeListBuilder.create().texOffs(0, 2).addBox(0.0F, -1.0F, -0.01F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -5.0F, -4.0F));

        head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 21).addBox(-5.0F, -5.0F, -4.0F, 10.0F, 10.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -5.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(DwarfRenderState state) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(state.yRot, state.xRot);
        this.animateWalk(DwarfAnimations.DWARF_WALK, state.walkAnimationPos, state.walkAnimationSpeed, 2f, 2.5f);
        this.animate(state.idleAnimationState, DwarfAnimations.DWARF_IDLE, state.ageInTicks, 1f);
        this.animate(state.attackAnimationState, DwarfAnimations.DWARF_ATTACK, state.ageInTicks, 1f);
      }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);
        this.head.yRot = headYaw * ((float)Math.PI / 180f);
        this.head.xRot = headPitch *  ((float)Math.PI / 180f);
    }

    public ModelPart getArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? this.left_arm : this.right_arm;
    }

    public ModelPart getHead() {
        return this.head;
    }

    @Override
    public void translateToHand(HumanoidArm side, PoseStack poseStack) {
        this.root.translateAndRotate(poseStack);
        this.getArm(side).translateAndRotate(poseStack);
    }

}
