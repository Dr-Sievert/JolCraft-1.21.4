package net.sievert.jolcraft.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.WolfArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.custom.DwarfEntity;
import net.sievert.jolcraft.entity.custom.DwarfVariant;

import java.util.Map;

public class DwarfRenderer extends MobRenderer<DwarfEntity, DwarfRenderState, DwarfModel> {
    private static final Map<DwarfVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(DwarfVariant.class), map -> {
                map.put(DwarfVariant.GREY,
                        ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/entity/dwarf/dwarf_grey.png"));
                map.put(DwarfVariant.BLUE,
                        ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/entity/dwarf/dwarf_blue.png"));
                map.put(DwarfVariant.GREEN,
                        ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/entity/dwarf/dwarf_green.png"));
                map.put(DwarfVariant.RED,
                        ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/entity/dwarf/dwarf_red.png"));
                map.put(DwarfVariant.PURPLE,
                        ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/entity/dwarf/dwarf_purple.png"));
                map.put(DwarfVariant.WHITE,
                        ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/entity/dwarf/dwarf_white.png"));
                map.put(DwarfVariant.YELLOW,
                        ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/entity/dwarf/dwarf_yellow.png"));
            });

    public DwarfRenderer(EntityRendererProvider.Context context) {
        super(context, new DwarfModel(context.bakeLayer(DwarfModel.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(DwarfRenderState entity) {
        return LOCATION_BY_VARIANT.get(entity.variant);
    }

    @Override
    public void render(DwarfRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if(renderState.isBaby) {
            poseStack.scale(0.45f, 0.45f, 0.45f);
        } else {
            poseStack.scale(0.9f, 0.9f, 0.9f);
        }

        super.render(renderState, poseStack, bufferSource, packedLight);
    }

    @Override
    public DwarfRenderState createRenderState() {
        return new DwarfRenderState();
    }

    @Override
    public void extractRenderState(DwarfEntity entity, DwarfRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.idleAnimationState.copyFrom(entity.idleAnimationState);
        reusedState.attackAnimationState.copyFrom(entity.attackAnimationState);
        reusedState.variant = entity.getVariant();
    }
}
