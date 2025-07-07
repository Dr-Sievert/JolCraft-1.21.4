package net.sievert.jolcraft.potion;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.effect.JolCraftEffects;

public class JolCraftPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, JolCraft.MOD_ID);

    public static final Holder<Potion> ANCIENT_MEMORY = POTIONS.register("ancient_memory",
            () -> new Potion("ancient_memory", new MobEffectInstance(JolCraftEffects.ANCIENT_MEMORY, 1200, 0)));

    public static final Holder<Potion> LONG_ANCIENT_MEMORY = POTIONS.register("long_ancient_memory",
            () -> new Potion("long_ancient_memory", new MobEffectInstance(JolCraftEffects.ANCIENT_MEMORY, 2400, 0)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}