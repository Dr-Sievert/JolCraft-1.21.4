package net.sievert.jolcraft.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.custom.DwarfGuardEntity;

public class DwarfGuardRenderer extends MobRenderer<DwarfGuardEntity, DwarfRenderState, DwarfGuardModel> {

    public DwarfGuardRenderer(EntityRendererProvider.Context context) {
        super(context, new DwarfGuardModel(context.bakeLayer(DwarfGuardModel.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(DwarfRenderState entity) {
        return ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/entity/dwarf/dwarf_guard.png");
    }

    @Override
    public void render(DwarfRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.scale(1.0f, 1.0f, 1.0f);
        super.render(renderState, poseStack, bufferSource, packedLight);
    }

    @Override
    public DwarfRenderState createRenderState() {
        return new DwarfRenderState();
    }

    @Override
    public void extractRenderState(DwarfGuardEntity entity, DwarfRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.idleAnimationState.copyFrom(entity.idleAnimationState);
        reusedState.attackAnimationState.copyFrom(entity.attackAnimationState);
        reusedState.blockAnimationState.copyFrom(entity.blockAnimationState);
    }
}
