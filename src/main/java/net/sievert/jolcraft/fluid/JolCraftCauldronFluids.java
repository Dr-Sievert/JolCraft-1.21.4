package net.sievert.jolcraft.fluid;

import net.neoforged.neoforge.fluids.RegisterCauldronFluidContentEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.neoforged.neoforge.fluids.FluidType;
import net.sievert.jolcraft.block.JolCraftBlocks;

import java.util.function.Supplier;

@EventBusSubscriber(modid = "jolcraft", bus = EventBusSubscriber.Bus.MOD)
public class JolCraftCauldronFluids {
    private static final IntegerProperty LEVEL = LayeredCauldronBlock.LEVEL;

    @SubscribeEvent
    public static void registerCauldronFluids(RegisterCauldronFluidContentEvent event) {
        Block cauldronBlock = JolCraftBlocks.FERMENTING_CAULDRON.get();   // Call get() here at runtime
        Fluid customFluid = JolCraftFluids.FERMENTING_FLUID_SOURCE.get(); // Also call get() here

        event.register(
                cauldronBlock,
                customFluid,
                FluidType.BUCKET_VOLUME,
                LEVEL
        );
    }
}
