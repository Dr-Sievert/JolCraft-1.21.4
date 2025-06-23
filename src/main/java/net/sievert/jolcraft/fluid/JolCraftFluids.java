package net.sievert.jolcraft.fluid;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sievert.jolcraft.JolCraft;

import java.util.function.Supplier;

public class JolCraftFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, JolCraft.MOD_ID);

    public static final Supplier<FlowingFluid> FERMENTING_FLUID_SOURCE = FLUIDS.register("fermenting_fluid",
            FermentingFluid.Source::new); // use method reference

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
