package net.sievert.jolcraft.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.common.Tags;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.block.JolCraftBlocks;
import net.sievert.jolcraft.block.custom.crop.DeepslateBulbsCropBlock;

import java.util.List;

public class JolCraftConfiguredFeatures {

    //Vegetation
    public static final ResourceKey<ConfiguredFeature<?, ?>> DUSKCAP_PATCH_KEY = registerKey("duskcap_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DEEPSLATE_BULBS_PATCH_KEY = registerKey("deepslate_bulbs_patch");

    //Ores
    public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_MITHRIL_ORE_KEY = registerKey("small_mithril_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MEDIUM_MITHRIL_ORE_KEY = registerKey("medium_mithril_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_MITHRIL_ORE_KEY = registerKey("large_mithril_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPECIAL_MITHRIL_ORE_KEY = registerKey("special_mithril_ore");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        //Vegetation
        register(
                context,
                DUSKCAP_PATCH_KEY,
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        30,
                        7,
                        3,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(BlockStateProvider.simple(JolCraftBlocks.DUSKCAP.get()))
                        )
                )
        );

        // Build the WeightedStateProvider with all ages, using a loop
        SimpleWeightedRandomList.Builder<BlockState> builder = SimpleWeightedRandomList.builder();
        for (int i = 0; i <= 9; i++) {
            builder.add(
                    JolCraftBlocks.DEEPSLATE_BULBS_CROP.get().defaultBlockState().setValue(DeepslateBulbsCropBlock.AGE, i), 1
            );
        }
        WeightedStateProvider provider = new WeightedStateProvider(builder);

        register(
                context,
                DEEPSLATE_BULBS_PATCH_KEY,
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        25,
                        3,
                        2,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(provider)
                        )
                )
        );

        //Ores
        RuleTest stoneReplaceables = new TagMatchTest(Tags.Blocks.ORE_BEARING_GROUND_STONE);
        RuleTest deepslateReplaceables = new TagMatchTest(Tags.Blocks.ORE_BEARING_GROUND_DEEPSLATE);
        RuleTest netherrackReplaceables = new TagMatchTest(Tags.Blocks.ORE_BEARING_GROUND_NETHERRACK);
        RuleTest endReplaceables = new BlockMatchTest(Blocks.END_STONE);

        List<OreConfiguration.TargetBlockState> overworldMithrilOres = List.of(
                OreConfiguration.target(deepslateReplaceables, JolCraftBlocks.DEEPSLATE_MITHRIL_ORE.get().defaultBlockState()));

        register(context, SMALL_MITHRIL_ORE_KEY, Feature.ORE, new OreConfiguration(overworldMithrilOres, 1));
        register(context, MEDIUM_MITHRIL_ORE_KEY, Feature.ORE, new OreConfiguration(overworldMithrilOres, 3));
        register(context, LARGE_MITHRIL_ORE_KEY, Feature.ORE, new OreConfiguration(overworldMithrilOres, 5));
        register(context, SPECIAL_MITHRIL_ORE_KEY, Feature.ORE, new OreConfiguration(overworldMithrilOres, 5));

    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
