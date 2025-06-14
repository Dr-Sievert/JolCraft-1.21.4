package net.sievert.jolcraft.world.processor;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.IEventBus;
import net.sievert.jolcraft.JolCraft;

public class JolCraftProcessors {

    public static final DeferredRegister<StructureProcessorType<?>> PROCESSORS =
            DeferredRegister.create(BuiltInRegistries.STRUCTURE_PROCESSOR, JolCraft.MOD_ID);

    /* public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<DwarfSpawnProcessor>> DWARF_SPAWN =
            PROCESSORS.register("dwarf_spawn", () -> () -> DwarfSpawnProcessor.CODEC); */

    public static void register(IEventBus eventBus) {
        PROCESSORS.register(eventBus);
    }
}
