package net.sievert.jolcraft.entity.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.block.custom.StrongboxBlock;
import net.sievert.jolcraft.entity.client.model.block.StrongboxModel;

public class StrongboxRenderer<T extends BlockEntity & LidBlockEntity> implements BlockEntityRenderer<T> {
    private final BlockEntityRendererProvider.Context context;
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/entity/block/strongbox.png");
    private final StrongboxModel model;

    public StrongboxRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new StrongboxModel(context.bakeLayer(StrongboxModel.LAYER_LOCATION));
        this.context = context;

    }

    @Override
    public void render(T tileEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        // 1. Move to block center (so Y = 0.75 is the middle of a 16px tall block model, adjust if your model is taller/shorter)
        poseStack.translate(0.5F, 0.75F, 0.5F);

        // 2. Rotate around block center by facing
        BlockState state = tileEntity.getBlockState();
        Direction facing = state.getValue(StrongboxBlock.FACING);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));

        // 3. Flip upright for Blockbench
        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        // 4. Move *down* by 8 pixels (0.5), and *back* by 8px on Z and *right* by 8px on X
        poseStack.translate(0F, -0.75F, 0F);

        // --- Animate and render model ---
        float openness = tileEntity.getOpenNess(partialTicks);
        openness = 1.0F - openness;
        openness = 1.0F - openness * openness * openness;

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURE));
        StrongboxModel freshModel = new StrongboxModel(context.bakeLayer(StrongboxModel.LAYER_LOCATION));
        freshModel.setupAnim(openness);
        freshModel.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay);

        poseStack.popPose();
    }


    @Override
    public AABB getRenderBoundingBox(T blockEntity) {
        // Use vanilla-like bounding box to prevent culling lid animation
        return AABB.encapsulatingFullBlocks(blockEntity.getBlockPos().offset(-1, 0, -1), blockEntity.getBlockPos().offset(1, 1, 1));
    }
}
