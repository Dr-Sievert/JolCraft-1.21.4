package net.sievert.jolcraft.block;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.block.custom.*;
import net.sievert.jolcraft.item.JolCraftItems;

import java.util.function.Function;

public class JolCraftBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(JolCraft.MOD_ID);

    public static final DeferredBlock<DuskcapBlock> DUSKCAP = registerBlock(
            "duskcap",
            (properties) -> new DuskcapBlock(
                    properties
                            .mapColor(MapColor.COLOR_MAGENTA)
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.GRASS)
                            .lightLevel(state -> 4)
                            .pushReaction(PushReaction.DESTROY)
            ),
            BlockBehaviour.Properties.of()
    );

    public static final DeferredBlock<FlowerPotBlock> POTTED_DUSKCAP = registerBlock(
            "potted_duskcap",
            (properties) -> new FlowerPotBlock(DUSKCAP.get(), properties),
            flowerPotProperties()
    );

    public static final DeferredBlock<Block> FESTERLING_CROP = BLOCKS.registerBlock("festerling_crop",
            (properties) -> new FesterlingCropBlock(properties
                    .mapColor(MapColor.TERRACOTTA_LIGHT_GREEN)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<FesterlingBlock> FESTERLING = registerBlock(
            "festerling",
            (properties) -> new FesterlingBlock(
                    properties
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GREEN)
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.GRASS)
                            .pushReaction(PushReaction.DESTROY)
            ),
            BlockBehaviour.Properties.of()
    );

    public static final DeferredBlock<FlowerPotBlock> POTTED_FESTERLING = registerBlock(
            "potted_festerling",
            (properties) -> new FlowerPotBlock(FESTERLING.get(), properties),
            flowerPotProperties()
    );

    public static final DeferredBlock<HayBlock> BARLEY_BLOCK = registerBlock("barley_block",
            (properties) -> new HayBlock(properties
                    .mapColor(MapColor.COLOR_YELLOW)
                    .strength(0.5F)
                    .sound(SoundType.GRASS)
                    .instrument(NoteBlockInstrument.BANJO)
                    .ignitedByLava()
            ),
            BlockBehaviour.Properties.of()
    );

    public static final DeferredBlock<Block> MUFFHORN_FUR_BLOCK = registerBlock("muffhorn_fur_block",
            (properties) -> new Block(properties
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.8F)
                    .sound(SoundType.WOOL)
                    .instrument(NoteBlockInstrument.GUITAR)
                    .ignitedByLava()
            ),
            BlockBehaviour.Properties.of() // use the same props passed to the block
    );


    // Register barley crop WITHOUT a block item!
    public static final DeferredBlock<Block> BARLEY_CROP = BLOCKS.registerBlock("barley_crop",
            (properties) -> new BarleyCropBlock(properties
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> DEEPSLATE_BULBS_CROP = BLOCKS.registerBlock("deepslate_bulbs_crop",
            (properties) -> new DeepslateBulbsCropBlock(properties
                    .mapColor(MapColor.DEEPSLATE)
                    .noCollission()
                    .randomTicks()
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)
                    .sound(SoundType.DEEPSLATE)
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    // 🔼 Top (visual only) — no item
    public static final DeferredBlock<Block> ASGARNIAN_CROP_TOP = BLOCKS.registerBlock("asgarnian_crop_top",
            (properties) -> new HopsCropTopBlock(
                    properties
                            .mapColor(MapColor.TERRACOTTA_MAGENTA)
                            .noCollission()
                            .instabreak()
                            .randomTicks()
                            .sound(SoundType.CROP)
                            .pushReaction(PushReaction.DESTROY),
                    () -> JolCraftItems.ASGARNIAN_SEEDS.get()  // <<-- This makes block pick give seeds!
            )
    );

    // 🔽 Bottom (logic + growth) — no item
    public static final DeferredBlock<Block> ASGARNIAN_CROP_BOTTOM = BLOCKS.registerBlock("asgarnian_crop_bottom",
            (properties) -> new HopsCropBottomBlock(
                    properties
                            .mapColor(MapColor.TERRACOTTA_MAGENTA)
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.CROP)
                            .pushReaction(PushReaction.DESTROY),
                    () -> JolCraftItems.ASGARNIAN_SEEDS.get(),         // Supplier for seeds
                    () -> JolCraftBlocks.ASGARNIAN_CROP_TOP.get()      // Supplier for top block
            )
    );

    public static final DeferredBlock<Block> DUSKHOLD_CROP_TOP = BLOCKS.registerBlock("duskhold_crop_top",
            (properties) -> new HopsCropTopBlock(
                    properties
                            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
                            .noCollission()
                            .instabreak()
                            .randomTicks()
                            .sound(SoundType.CROP)
                            .pushReaction(PushReaction.DESTROY),
                    () -> JolCraftItems.DUSKHOLD_SEEDS.get()
            )
    );

    public static final DeferredBlock<Block> DUSKHOLD_CROP_BOTTOM = BLOCKS.registerBlock("duskhold_crop_bottom",
            (properties) -> new HopsCropBottomBlock(
                    properties
                            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.CROP)
                            .pushReaction(PushReaction.DESTROY),
                    () -> JolCraftItems.DUSKHOLD_SEEDS.get(),
                    () -> JolCraftBlocks.DUSKHOLD_CROP_TOP.get()
            )
    );

    public static final DeferredBlock<Block> KRANDONIAN_CROP_TOP = BLOCKS.registerBlock("krandonian_crop_top",
            (properties) -> new HopsCropTopBlock(
                    properties
                            .mapColor(MapColor.WARPED_STEM)
                            .noCollission()
                            .instabreak()
                            .randomTicks()
                            .sound(SoundType.CROP)
                            .pushReaction(PushReaction.DESTROY),
                    () -> JolCraftItems.KRANDONIAN_SEEDS.get()
            )
    );

    public static final DeferredBlock<Block> KRANDONIAN_CROP_BOTTOM = BLOCKS.registerBlock("krandonian_crop_bottom",
            (properties) -> new HopsCropBottomBlock(
                    properties
                            .mapColor(MapColor.WARPED_STEM)
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.CROP)
                            .pushReaction(PushReaction.DESTROY),
                    () -> JolCraftItems.KRANDONIAN_SEEDS.get(),
                    () -> JolCraftBlocks.KRANDONIAN_CROP_TOP.get()
            )
    );


    public static final DeferredBlock<Block> YANILLIAN_CROP_TOP = BLOCKS.registerBlock("yanillian_crop_top",
            (properties) -> new HopsCropTopBlock(
                    properties
                            .mapColor(MapColor.COLOR_GREEN)
                            .noCollission()
                            .instabreak()
                            .randomTicks()
                            .sound(SoundType.CROP)
                            .pushReaction(PushReaction.DESTROY),
                    () -> JolCraftItems.YANILLIAN_SEEDS.get()
            )
    );

    public static final DeferredBlock<Block> YANILLIAN_CROP_BOTTOM = BLOCKS.registerBlock("yanillian_crop_bottom",
            (properties) -> new HopsCropBottomBlock(
                    properties
                            .mapColor(MapColor.COLOR_GREEN)
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.CROP)
                            .pushReaction(PushReaction.DESTROY),
                    () -> JolCraftItems.YANILLIAN_SEEDS.get(),
                    () -> JolCraftBlocks.YANILLIAN_CROP_TOP.get()
            )
    );

    public static final DeferredBlock<FermentingCauldronBlock> FERMENTING_CAULDRON = BLOCKS.registerBlock(
            "fermenting_cauldron",
            props -> new FermentingCauldronBlock(
                    Biome.Precipitation.NONE,
                    CauldronInteraction.EMPTY,
                    props
            ),
            BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAULDRON)
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F)
                    .noOcclusion()
    );


    // Registers a block and its corresponding item
    private static <B extends Block> DeferredBlock<B> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> builder, BlockBehaviour.Properties properties) {
        DeferredBlock<B> block = BLOCKS.registerBlock(name, builder);
        registerBlockItem(name, block);
        return block;
    }

    // Registers a BlockItem for an existing block
    private static <B extends Block> void registerBlockItem(String name, DeferredBlock<B> block) {
        JolCraftItems.ITEMS.registerItem(name, props -> new BlockItem(block.get(), props.useBlockDescriptionPrefix()));
    }

    private static BlockBehaviour.Properties flowerPotProperties() {
        return BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }


}
