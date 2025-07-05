package net.sievert.jolcraft.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sievert.jolcraft.JolCraft;

public class JolCraftEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, JolCraft.MOD_ID);

    public static final Holder<MobEffect> HOMESTEAD = MOB_EFFECTS.register("homestead",
            () -> new HomesteadEffect(MobEffectCategory.BENEFICIAL, 0x6e6d6d));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
