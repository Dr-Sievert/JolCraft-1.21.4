package net.sievert.jolcraft.entity.client.render.object;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.client.model.object.RadiantModel;
import net.sievert.jolcraft.entity.client.object.RadiantRenderState;
import net.sievert.jolcraft.entity.custom.object.RadiantEntity;

public class RadiantRenderer extends EntityRenderer<RadiantEntity, RadiantRenderState> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            JolCraft.MOD_ID, "textures/entity/radiant/radiant.png"
    );
    private final RadiantModel model;

    public RadiantRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new RadiantModel(context.bakeLayer(RadiantModel.LAYER_LOCATION));
    }

    protected ResourceLocation getTextureLocation(RadiantEntity entity) {
        return TEXTURE;
    }

    @Override
    public RadiantRenderState createRenderState() {
        return new RadiantRenderState();
    }

    @Override
    public void extractRenderState(RadiantEntity entity, RadiantRenderState state, float partialTick) {
        // Copy the entity's animation state to the render state for model use
        state.idleAnimationState.copyFrom(entity.idleAnimationState);
        state.ageInTicks = entity.tickCount + partialTick;
    }

    @Override
    public void render(RadiantRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        // Vanilla mob: scale -1 on X and Y to flip upright
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(-0.01F, -1.501F, 0.0F);

        // ⬇️ Call setupAnim *here* before rendering the model!
        model.setupAnim(state);

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURE));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }

    @Override
    protected int getBlockLightLevel(RadiantEntity entity, BlockPos pos) {
        return 15;
    }

}
