package net.sievert.jolcraft.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.block.custom.BarleyCropBlock;
import net.sievert.jolcraft.block.custom.FermentingCauldronBlock;
import net.sievert.jolcraft.block.custom.HopsCropBottomBlock;
import net.sievert.jolcraft.block.custom.HopsCropTopBlock;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.fluid.JolCraftFluids;

import java.util.function.Function;

public class JolCraftBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(JolCraft.MOD_ID);

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

    // 🔼 Top (visual only) — no item
    public static final DeferredBlock<Block> ASGARNIAN_CROP_TOP = BLOCKS.registerBlock("asgarnian_crop_top",
            (properties) -> new HopsCropTopBlock(
                    properties
                            .mapColor(MapColor.PLANT)
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
                            .mapColor(MapColor.PLANT)
                            .noCollission()
                            .noLootTable()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.CROP)
                            .pushReaction(PushReaction.DESTROY),
                    () -> JolCraftItems.ASGARNIAN_SEEDS.get(),         // Supplier for seeds
                    () -> JolCraftBlocks.ASGARNIAN_CROP_TOP.get()      // Supplier for top block
            )
    );

    public static final DeferredBlock<FermentingCauldronBlock> FERMENTING_CAULDRON = registerBlock(
            "fermenting_cauldron",
            props -> new FermentingCauldronBlock(props
                    .mapColor(MapColor.METAL)
                    .strength(2.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion() // Required for cauldron shape
                    .requiresCorrectToolForDrops()
            )
    );

    // You need to register the fluid itself elsewhere as FERMENTING_FLUID (see note below)
    public static final DeferredBlock<LiquidBlock> FERMENTING_FLUID = BLOCKS.registerBlock(
            "fermenting_fluid",
            props -> new LiquidBlock(
                    JolCraftFluids.FERMENTING_FLUID_SOURCE.get(), // <--- .get() to pass the instance!
                    props
                            .mapColor(MapColor.WATER)
                            .replaceable()
                            .noCollission()
                            .strength(100.0F)
                            .pushReaction(PushReaction.DESTROY)
                            .noLootTable()
                            .liquid()
                            .sound(SoundType.EMPTY)
                            .requiresCorrectToolForDrops()
            )
    );

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        JolCraftItems.ITEMS.registerItem(name, (properties) -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
