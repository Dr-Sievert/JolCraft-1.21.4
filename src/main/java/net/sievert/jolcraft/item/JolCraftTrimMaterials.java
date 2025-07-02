package net.sievert.jolcraft.item;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.sievert.jolcraft.JolCraft;

import java.util.Map;

public class JolCraftTrimMaterials {
    public static final ResourceKey<TrimMaterial> DEEPSLATE =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "deepslate"));

    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(
                context,
                DEEPSLATE,
                JolCraftItems.DEEPSLATE_PLATE.get(),
                Style.EMPTY.withColor(TextColor.parseColor("#595959").getOrThrow()),
                Map.of(JolCraftEquipmentAssets.DEEPSLATE.getKey(), "deepslate_darker")
        );
    }

    private static void register(
            BootstrapContext<TrimMaterial> context,
            ResourceKey<TrimMaterial> trimKey,
            Item item,
            Style style,
            Map<ResourceKey<EquipmentAsset>, String> overrideArmorAssets // NEW PARAM
    ) {
        TrimMaterial trimMaterial = TrimMaterial.create(
                trimKey.location().getPath(),  // asset_name
                item,                          // ingredient
                Component.translatable(Util.makeDescriptionId("trim_material", trimKey.location())).withStyle(style),
                overrideArmorAssets            // override armor asset names (can be Map.of())
        );
        context.register(trimKey, trimMaterial);
    }


}
