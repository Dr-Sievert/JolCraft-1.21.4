package net.sievert.jolcraft.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.custom.DwarfGuardEntity;

public class DwarfGuardRenderer extends DwarfRenderer<DwarfGuardEntity> {

    public DwarfGuardRenderer(EntityRendererProvider.Context context) {
        super(context, new DwarfGuardModel(context.bakeLayer(DwarfGuardModel.LAYER_LOCATION)));
        addLayer(new DwarfArmorLayer(this));
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
        reusedState.drinkAnimationState.copyFrom(entity.drinkAnimationState);
        reusedState.useItemHand = entity.getUsedItemHand();
        reusedState.ticksUsingItem = entity.getTicksUsingItem();
        reusedState.isUsingItem = entity.isUsingItem();
    }

}
