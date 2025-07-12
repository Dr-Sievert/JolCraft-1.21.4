package net.sievert.jolcraft.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.block.JolCraftBlocks;

import java.util.List;

public class JolCraftConfiguredFeatures {

    //Vegetation
    public static final ResourceKey<ConfiguredFeature<?, ?>> DUSKCAP_PATCH_KEY = registerKey("duskcap_patch");

    //Ores
    public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_MITHRIL_ORE_KEY = registerKey("small_mithril_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MEDIUM_MITHRIL_ORE_KEY = registerKey("medium_mithril_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_MITHRIL_ORE_KEY = registerKey("large_mithril_ore");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        //Vegetation
        register(
                context,
                DUSKCAP_PATCH_KEY,
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        30, // tries
                        7,  // xz spread
                        3,  // y spread
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(BlockStateProvider.simple(JolCraftBlocks.DUSKCAP.get()))
                        )
                )
        );

        //Ores
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherrackReplaceables = new BlockMatchTest(Blocks.NETHERRACK);
        RuleTest endReplaceables = new BlockMatchTest(Blocks.END_STONE);

        List<OreConfiguration.TargetBlockState> overworldMithrilOres = List.of(
                OreConfiguration.target(deepslateReplaceables, JolCraftBlocks.DEEPSLATE_MITHRIL_ORE.get().defaultBlockState()));

        register(context, SMALL_MITHRIL_ORE_KEY, Feature.ORE, new OreConfiguration(overworldMithrilOres, 1));
        register(context, MEDIUM_MITHRIL_ORE_KEY, Feature.ORE, new OreConfiguration(overworldMithrilOres, 3));
        register(context, LARGE_MITHRIL_ORE_KEY, Feature.ORE, new OreConfiguration(overworldMithrilOres, 5));

    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
