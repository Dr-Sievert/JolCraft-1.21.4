package net.sievert.jolcraft.entity.client;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.custom.DwarfGuardEntity;

public class DwarfGuardModel extends HumanoidModel<DwarfRenderState> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "dwarf_guard"), "main");
    private final ModelPart body;
    private final ModelPart bodywear;
    private final ModelPart left_arm;
    private final ModelPart left_armwear;
    private final ModelPart shield;
    private final ModelPart left_leg;
    private final ModelPart right_arm;
    private final ModelPart right_armwear;
    private final ModelPart right_leg;
    private final ModelPart legwear;
    private final ModelPart right_footwear;
    private final ModelPart left_footwear;
    private final ModelPart head;
    private final ModelPart beard;
    private final ModelPart right_eyebrow;
    private final ModelPart left_eyebrow;
    private final ModelPart hat;

    public DwarfGuardModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");
        this.right_leg = root.getChild("right_leg");
        this.left_leg = root.getChild("left_leg");
        this.bodywear = this.body.getChild("bodywear");
        this.right_armwear = this.right_arm.getChild("right_armwear");
        this.left_armwear = this.left_arm.getChild("left_armwear");
        this.legwear = this.body.getChild("legwear");
        this.right_footwear = this.right_leg.getChild("right_footwear");
        this.left_footwear = this.left_leg.getChild("left_footwear");
        this.shield = this.left_arm.getChild("shield");
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

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 51).addBox(-3.8F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 5.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(48, 51).addBox(-0.2F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 5.0F, 0.0F, -0.7418F, 0.0F, 0.0F));

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 39).addBox(-2.0F, -0.5F, -2.0F, 5.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 17.0F, 0.0F));

        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(14, 47).addBox(-3.0F, -0.5F, -2.0F, 5.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 17.0F, 0.0F));

        body.addOrReplaceChild("legwear", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 16.25F, -3.0F, 12.0F, 3.75F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        body.addOrReplaceChild("bodywear", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 5.0F, -3.0F, 12.0F, 10.75F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        right_arm.addOrReplaceChild("right_armwear", CubeListBuilder.create().texOffs(2, 54).addBox(-3.8F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        left_arm.addOrReplaceChild("left_armwear", CubeListBuilder.create().texOffs(20, 60).addBox(-0.2F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        right_leg.addOrReplaceChild("right_footwear", CubeListBuilder.create().texOffs(20, 69).addBox(-2.0F, -0.5F, -2.0F, 5.0F, 7.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        left_leg.addOrReplaceChild("left_footwear", CubeListBuilder.create().texOffs(0, 63).addBox(-3.0F, -0.5F, -2.0F, 5.0F, 7.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        left_arm.addOrReplaceChild("shield", CubeListBuilder.create().texOffs(0, 105).addBox(-2.0F, -20.0F, -1.0F, 12.0F, 22.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(27, 115).addBox(3.75F, -14.0F, -0.75F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.25F, 11.5F, 10.0F, 1.5708F, 0.0F, -1.5708F));

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
        super.setupAnim(state);
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(state.yRot, state.xRot);
        this.animateWalk(DwarfAnimations.DWARF_WALK, state.walkAnimationPos, state.walkAnimationSpeed, 2f, 2.5f);
        this.animate(state.idleAnimationState, DwarfAnimations.DWARF_IDLE, state.ageInTicks, 1f);
        this.animate(state.attackAnimationState, DwarfAnimations.DWARF_ATTACK_WEAPON, state.ageInTicks, 1f);
        this.animate(state.blockAnimationState, DwarfAnimations.DWARF_BLOCK, state.ageInTicks, 1f);
        this.animate(state.drinkAnimationState, DwarfAnimations.DWARF_DRINK, state.ageInTicks, 1f);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);
        this.head.yRot = headYaw * ((float)Math.PI / 180f);
        this.head.xRot = headPitch *  ((float)Math.PI / 180f);
    }

    @Override
    public void translateToHand(HumanoidArm side, PoseStack poseStack) {
        this.root.translateAndRotate(poseStack);
        this.getArm(side).translateAndRotate(poseStack);
    }

    protected ModelPart getArm(HumanoidArm side) {
        return side == HumanoidArm.RIGHT ? this.right_arm : this.left_arm;
    }

    public void showOnlyArmor(EquipmentSlot slot) {
        // Hide everything by default
        this.head.visible = false;
        this.hat.visible = false;
        this.body.visible = false;
        this.right_arm.visible = false;
        this.left_arm.visible = false;
        this.right_leg.visible = false;
        this.left_leg.visible = false;
        this.beard.visible = false;
        this.right_eyebrow.visible = false;
        this.left_eyebrow.visible = false;
        this.shield.visible = false;

        this.bodywear.visible = false;
        this.right_armwear.visible = false;
        this.left_armwear.visible = false;
        this.legwear.visible = false;
        this.right_footwear.visible = false;
        this.left_footwear.visible = false;

        // Show only the relevant armor layer parts for the given slot
        switch (slot) {
            case HEAD -> this.hat.visible = true;

            case CHEST -> {
                this.bodywear.visible = true;
                this.right_armwear.visible = true;
                this.left_armwear.visible = true;
            }

            case LEGS -> {
                this.bodywear.visible = true;
                this.legwear.visible = true;
            }

            case FEET -> {
                this.right_footwear.visible = true;
                this.left_footwear.visible = true;
            }
        }
    }

}
