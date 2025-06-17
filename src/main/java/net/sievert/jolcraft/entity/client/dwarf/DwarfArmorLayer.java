package net.sievert.jolcraft.entity.client.dwarf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.Equippable;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.client.model.DwarfModel;

public class DwarfArmorLayer extends RenderLayer<DwarfRenderState, DwarfModel> {

    public DwarfArmorLayer(RenderLayerParent<DwarfRenderState, DwarfModel> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       DwarfRenderState state, float yRot, float xRot) {
        if (state.dwarf == null) return;

        DwarfModel model = this.getParentModel();
        model.setupAnim(state);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.isArmor()) continue;

            ItemStack stack = state.dwarf.getItemBySlot(slot);
            if (stack.isEmpty()) continue;

            Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
            if (equippable == null || equippable.slot() != slot) continue;

            ResourceKey<EquipmentAsset> assetKey = equippable.assetId().orElse(null);
            if (assetKey == null) continue;

            String material = assetKey.location().getPath()
                    .replace("_layer_1", "")
                    .replace("_layer_2", "");

            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                    JolCraft.MOD_ID,
                    "textures/entity/dwarf/armor/dwarf_" + material + "_armor.png"
            );

            // Hide all armor-wear parts only
            model.getHead().getChild("hat").visible = slot == EquipmentSlot.HEAD;
            model.body.getChild("bodywear").visible = slot == EquipmentSlot.CHEST;
            model.right_arm.getChild("right_armwear").visible = slot == EquipmentSlot.CHEST;
            model.left_arm.getChild("left_armwear").visible = slot == EquipmentSlot.CHEST;
            model.body.getChild("legwear").visible = slot == EquipmentSlot.LEGS;
            model.right_leg.getChild("right_footwear").visible = slot == EquipmentSlot.FEET;
            model.left_leg.getChild("left_footwear").visible = slot == EquipmentSlot.FEET;

            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
            model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
        }

        // Re-enable everything after all slots rendered
        model.setAllVisible(true);
    }


}
