package net.sievert.jolcraft.entity.client.render.animal;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.client.animal.MuffhornRenderState;
import net.sievert.jolcraft.entity.client.model.animal.MuffhornModel;
import net.sievert.jolcraft.entity.custom.animal.MuffhornEntity;

@OnlyIn(Dist.CLIENT)
public class MuffhornRenderer extends AgeableMobRenderer<MuffhornEntity, MuffhornRenderState, MuffhornModel> {

    public MuffhornRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new MuffhornModel(context.bakeLayer(MuffhornModel.LAYER_LOCATION)),         // adult model
                new MuffhornModel(context.bakeLayer(MuffhornModel.BABY_LAYER_LOCATION)),    // baby model
                0.6f // scale factor (adjust as needed)
        );
    }

    @Override
    public ResourceLocation getTextureLocation(MuffhornRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/entity/animal/muffhorn.png");
    }

    public MuffhornRenderState createRenderState() {
        return new MuffhornRenderState();
    }

    public void extractRenderState(MuffhornEntity entity, MuffhornRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.isSheared = entity.isSheared(); // <-- ADD THIS LINE
    }

    @Override
    public void render(MuffhornRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (this.model instanceof MuffhornModel muffhornModel) {
            muffhornModel.setFurVisible(!state.isSheared);
        }
        super.render(state, poseStack, bufferSource, packedLight);
    }
}

