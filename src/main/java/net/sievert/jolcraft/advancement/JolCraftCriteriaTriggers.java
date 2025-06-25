package net.sievert.jolcraft.advancement;

import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.RegisterEvent;

public class JolCraftCriteriaTriggers {

    public static final HasAdvancementTrigger HAS_ADVANCEMENT = new HasAdvancementTrigger(); // ← Add this

    public static final HasDwarvenLanguageTrigger HAS_DWARVEN_LANGUAGE = new HasDwarvenLanguageTrigger();

    public static final TradeWithDwarfTrigger TRADE_WITH_DWARF = TradeWithDwarfTrigger.INSTANCE;

    public static final EndorsementGainTrigger ENDORSEMENT_GAIN = new EndorsementGainTrigger();


    public static void register(RegisterEvent event) {
        event.register(Registries.TRIGGER_TYPE, helper -> {
            helper.register(HasAdvancementTrigger.ID, HAS_ADVANCEMENT);
            helper.register(HasDwarvenLanguageTrigger.ID, HAS_DWARVEN_LANGUAGE);
            helper.register(EndorsementGainTrigger.ID, ENDORSEMENT_GAIN);
        });
    }
}
