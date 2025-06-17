package net.sievert.jolcraft.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.custom.AbstractDwarfEntity;
import net.sievert.jolcraft.entity.custom.variation.DwarfVariant;

import java.util.Map;

public class DwarfRenderer<T extends AbstractDwarfEntity> extends HumanoidMobRenderer<T, DwarfRenderState, DwarfModel> {

    public DwarfRenderer(EntityRendererProvider.Context context) {
        super(context, new DwarfModel(context.bakeLayer(DwarfModel.LAYER_LOCATION)), 0.4f);
        this.addLayer(new DwarfArmorLayer(this));
        this.addLayer(new DwarfBeardLayer(this));
        this.addLayer(new DwarfEyeLayer(this));
    }

    public DwarfRenderer(EntityRendererProvider.Context context, DwarfModel model) {
        super(context, model, 0.4f);
    }

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
    public void extractRenderState(AbstractDwarfEntity entity, DwarfRenderState reusedState, float partialTick) {
        super.extractRenderState((T) entity, reusedState, partialTick);
        reusedState.idleAnimationState.copyFrom(entity.idleAnimationState);
        reusedState.attackAnimationState.copyFrom(entity.attackAnimationState);
        reusedState.dwarf = entity;
        reusedState.variant = entity.getVariant();
        reusedState.beard = entity.getBeard();
        reusedState.eye = entity.getEye();
        reusedState.useItemHand = entity.getUsedItemHand();
        reusedState.ticksUsingItem = entity.getTicksUsingItem();
        reusedState.isUsingItem = entity.isUsingItem();
        reusedState.headEquipment = entity.getItemBySlot(EquipmentSlot.HEAD);
        reusedState.chestEquipment = entity.getItemBySlot(EquipmentSlot.CHEST);
        reusedState.legsEquipment = entity.getItemBySlot(EquipmentSlot.LEGS);
        reusedState.feetEquipment = entity.getItemBySlot(EquipmentSlot.FEET);
    }
}
