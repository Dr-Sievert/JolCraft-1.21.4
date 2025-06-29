package net.sievert.jolcraft.entity.client.animal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.client.model.animal.MuffhornModel;

@OnlyIn(Dist.CLIENT)
public class MuffhornFurLayer extends RenderLayer<MuffhornRenderState, MuffhornModel> {
    private static final ResourceLocation FUR_TEXTURE = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/entity/animal/muffhorn_fur.png");

    private final MuffhornModel adultModel;
    private final MuffhornModel babyModel;

    public MuffhornFurLayer(RenderLayerParent<MuffhornRenderState, MuffhornModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.adultModel = new MuffhornModel(modelSet.bakeLayer(MuffhornModel.LAYER_LOCATION));
        this.babyModel = new MuffhornModel(modelSet.bakeLayer(MuffhornModel.BABY_LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       MuffhornRenderState state, float yRot, float xRot) {
        if (state.isSheared) return;

        MuffhornModel model = state.isBaby ? babyModel : adultModel;
        model.setupAnim(state);
        model.setFurVisible(true);

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(FUR_TEXTURE));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);

        model.setFurVisible(false); // Reset so other renders (e.g. shadow) don't accidentally keep fur on
    }
}
