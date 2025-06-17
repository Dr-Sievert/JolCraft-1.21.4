package net.sievert.jolcraft.advancement;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegisterEvent;

public class JolCraftCriteriaTriggers {

    public static final ResourceKey<CriterionTrigger<?>> DWARVEN_LANGUAGE_KEY =
            ResourceKey.create(Registries.TRIGGER_TYPE, HasDwarvenLanguageTrigger.ID);

    public static HasDwarvenLanguageTrigger HAS_DWARVEN_LANGUAGE;

    public static final TradeWithDwarfTrigger TRADE_WITH_DWARF = TradeWithDwarfTrigger.INSTANCE;

    public static void register(RegisterEvent event) {
        event.register(Registries.TRIGGER_TYPE, helper -> {
            HAS_DWARVEN_LANGUAGE = new HasDwarvenLanguageTrigger();
            helper.register(HasDwarvenLanguageTrigger.ID, HAS_DWARVEN_LANGUAGE);
        });
    }
}
