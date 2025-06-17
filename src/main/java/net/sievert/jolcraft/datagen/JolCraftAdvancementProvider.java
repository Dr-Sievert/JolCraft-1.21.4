package net.sievert.jolcraft.datagen;

import net.minecraft.advancements.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.advancement.TradeWithDwarfTrigger;
import net.sievert.jolcraft.item.JolCraftItems;
import net.minecraft.advancements.AdvancementHolder;
import net.sievert.jolcraft.advancement.HasDwarvenLanguageTrigger;

import java.util.function.Consumer;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementSubProvider;

public class JolCraftAdvancementProvider implements AdvancementSubProvider {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer) {
        ResourceLocation parentId = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "story/read_lexicon");
        AdvancementHolder readLexiconAdv = Advancement.Builder.advancement()
                .display(
                        JolCraftItems.DWARVEN_LEXICON.get(),
                        Component.translatable("advancement.jolcraft.read_lexicon.title"),
                        Component.translatable("advancement.jolcraft.read_lexicon.description"),
                        ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/gui/advancements/backgrounds/jolcraft.png"),
                        AdvancementType.CHALLENGE,
                        true, true, false
                )
                .addCriterion("knows_dwarvish", HasDwarvenLanguageTrigger.hasLanguage())
                .save(consumer, parentId);

        ResourceLocation tradeId = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "story/trade_with_dwarf");
        Advancement.Builder.advancement()
                .parent(readLexiconAdv)
                .display(
                        JolCraftItems.GOLD_COIN.get(),
                        Component.translatable("advancement.jolcraft.trade_with_dwarf.title"),
                        Component.translatable("advancement.jolcraft.trade_with_dwarf.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("trade_with_dwarf", TradeWithDwarfTrigger.tradedWithDwarf())
                .save(consumer, tradeId);
    }
}
