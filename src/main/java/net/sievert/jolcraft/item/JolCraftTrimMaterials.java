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


    //Material-based
    public static final ResourceKey<TrimMaterial> DEEPSLATE =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "deepslate"));

    //Gems
    public static final ResourceKey<TrimMaterial> AEGISCORE =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "aegiscore"));
    public static final ResourceKey<TrimMaterial> ASHFANG =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "ashfang"));
    public static final ResourceKey<TrimMaterial> DEEPMARROW =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "deepmarrow"));
    public static final ResourceKey<TrimMaterial> EARTHBLOOD =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "earthblood"));
    public static final ResourceKey<TrimMaterial> EMBERGLASS =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "emberglass"));
    public static final ResourceKey<TrimMaterial> FROSTVEIN =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "frostvein"));
    public static final ResourceKey<TrimMaterial> GRIMSTONE =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "grimstone"));
    public static final ResourceKey<TrimMaterial> IRONHEART =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "ironheart"));
    public static final ResourceKey<TrimMaterial> LUMIERE =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "lumiere"));
    public static final ResourceKey<TrimMaterial> MOONSHARD =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "moonshard"));
    public static final ResourceKey<TrimMaterial> RUSTAGATE =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "rustagate"));
    public static final ResourceKey<TrimMaterial> SKYBURROW =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "skyburrow"));
    public static final ResourceKey<TrimMaterial> SUNGLEAM =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "sungleam"));
    public static final ResourceKey<TrimMaterial> VERDANITE =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "verdanite"));
    public static final ResourceKey<TrimMaterial> WOECRYSTAL =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "woecrystal"));


    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(
                context,
                DEEPSLATE,
                JolCraftItems.DEEPSLATE_PLATE.get(),
                Style.EMPTY.withColor(TextColor.parseColor("#595959").getOrThrow()),
                Map.of(JolCraftEquipmentAssets.DEEPSLATE.getKey(), "deepslate_darker")
        );
        register(
                context,
                AEGISCORE,
                JolCraftItems.AEGISCORE.get(),
                Style.EMPTY.withColor(TextColor.parseColor("#8397bf").getOrThrow()),
                Map.of()
        );
        register(
                context,
                ASHFANG,
                JolCraftItems.ASHFANG.get(),
                Style.EMPTY.withColor(TextColor.parseColor("#fe8301").getOrThrow()),
                Map.of()
        );
        register(
                context,
                DEEPMARROW,
                JolCraftItems.DEEPMARROW.get(),
                Style.EMPTY.withColor(TextColor.parseColor("#bbb2ac").getOrThrow()),
                Map.of()
        );
        register(
                context,
                EARTHBLOOD,
                JolCraftItems.EARTHBLOOD.get(),
                Style.EMPTY.withColor(TextColor.parseColor("#3e4206").getOrThrow()),
                Map.of()
        );
        register(
                context,
                EMBERGLASS,
                JolCraftItems.EMBERGLASS.get(),
                Style.EMPTY.withColor(TextColor.parseColor("#9c584b").getOrThrow()),
                Map.of()
        );
        register(
                context,
                FROSTVEIN,
                JolCraftItems.FROSTVEIN.get(),
                Style.EMPTY.withColor(TextColor.parseColor("#067da8").getOrThrow()),
                Map.of()
        );
        register(
                context,
                GRIMSTONE,
                JolCraftItems.GRIMSTONE.get(),
                Style.EMPTY.withColor(TextColor.parseColor("#b50002").getOrThrow()),
                Map.of()
        );
        register(
                context,
                IRONHEART,
                JolCraftItems.IRONHEART.get(),
                Style.EMPTY.withColor(TextColor.parseColor("#5c2320").getOrThrow()),
                Map.of()
        );
        register(
                context,
                LUMIERE,
                JolCraftItems.LUMIERE.get(),
                Style.EMPTY.withColor(TextColor.parseColor("#f8f338").getOrThrow()),
                Map.of()
        );
        register(
                context,
                MOONSHARD,
                JolCraftItems.MOONSHARD.get(),
                Style.EMPTY.withColor(TextColor.parseColor("#a5a6ff").getOrThrow()),
                Map.of()
        );
        register(
                context,
                RUSTAGATE,
                JolCraftItems.RUSTAGATE.get(),
                Style.EMPTY.withColor(TextColor.parseColor("#c95d38").getOrThrow()),
                Map.of()
        );
        register(
                context,
                SKYBURROW,
                JolCraftItems.SKYBURROW.get(),
                Style.EMPTY.withColor(TextColor.parseColor("#5bc9dc").getOrThrow()),
                Map.of()
        );
        register(
                context,
                SUNGLEAM,
                JolCraftItems.SUNGLEAM.get(),
                Style.EMPTY.withColor(TextColor.parseColor("#efd03c").getOrThrow()),
                Map.of()
        );
        register(
                context,
                VERDANITE,
                JolCraftItems.VERDANITE.get(),
                Style.EMPTY.withColor(TextColor.parseColor("#6de775").getOrThrow()),
                Map.of()
        );
        register(
                context,
                WOECRYSTAL,
                JolCraftItems.WOECRYSTAL.get(),
                Style.EMPTY.withColor(TextColor.parseColor("#737296").getOrThrow()),
                Map.of()
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
