package net.sievert.jolcraft.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.sievert.jolcraft.JolCraft;

import java.util.List;

public class JolCraftPlacedFeatures {

    //Vegetation
    public static final ResourceKey<PlacedFeature> DUSKCAP_PATCH_PLACED_KEY = registerKey("duskcap_patch_placed");

    //Ores
    public static final ResourceKey<PlacedFeature> SMALL_MITHRIL_ORE_PLACED_KEY = registerKey("small_mithril_ore_placed");
    public static final ResourceKey<PlacedFeature> MEDIUM_MITHRIL_ORE_PLACED_KEY = registerKey("medium_mithril_ore_placed");
    public static final ResourceKey<PlacedFeature> LARGE_MITHRIL_ORE_PLACED_KEY = registerKey("large_mithril_ore_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        //Vegetation
        register(
                context,
                DUSKCAP_PATCH_PLACED_KEY,
                configuredFeatures.getOrThrow(JolCraftConfiguredFeatures.DUSKCAP_PATCH_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(10), // matches rarity_filter, chance: 10
                        InSquarePlacement.spread(), // matches in_square
                        HeightRangePlacement.uniform(
                                VerticalAnchor.absolute(-64),
                                VerticalAnchor.absolute(0)
                        ), // matches height_range
                        BiomeFilter.biome() // always last
                )
        );

        //Ores
        register(context, SMALL_MITHRIL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(JolCraftConfiguredFeatures.SMALL_MITHRIL_ORE_KEY),
                JolCraftOreReplacement.rareOrePlacement(2, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(0))));
        register(context, MEDIUM_MITHRIL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(JolCraftConfiguredFeatures.MEDIUM_MITHRIL_ORE_KEY),
                JolCraftOreReplacement.rareOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(0))));
        register(context, LARGE_MITHRIL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(JolCraftConfiguredFeatures.LARGE_MITHRIL_ORE_KEY),
                JolCraftOreReplacement.rareOrePlacement(8, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(0))));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}