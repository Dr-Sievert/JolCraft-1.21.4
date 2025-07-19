package net.sievert.jolcraft.entity.attribute;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.sievert.jolcraft.JolCraft;

public class JolCraftAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(Registries.ATTRIBUTE, JolCraft.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> XP_BOOST =
            ATTRIBUTES.register("xp_boost", () ->
                    new PercentageAttribute("attribute.jolcraft.xp_boost", 0.0D, 0.0D, 10.0D)
                            .setSyncable(true)
            );

    public static final DeferredHolder<Attribute, Attribute> SLOW_RESIST =
            ATTRIBUTES.register("slow_resist", () ->
                    new PercentageAttribute("attribute.jolcraft.slow_resist", 0.0D, 0.0D, 1.0D)
                            .setSyncable(true)
            );

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}
