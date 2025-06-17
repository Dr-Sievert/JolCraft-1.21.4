package net.sievert.jolcraft.datagen;

import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.advancement.TradeWithDwarfTrigger;
import net.sievert.jolcraft.item.JolCraftItems;
import net.minecraft.advancements.AdvancementHolder;
import net.sievert.jolcraft.advancement.HasDwarvenLanguageTrigger;

import java.util.Optional;
import java.util.function.Consumer;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementSubProvider;

public class JolCraftAdvancementProvider implements AdvancementSubProvider {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer) {
        // Root advancement (dummy) — sets the tab name
        ResourceLocation rootId = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "story/root");
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(
                        Items.CHISELED_DEEPSLATE,
                        Component.translatable("advancement.jolcraft.root.title"),
                        Component.translatable("advancement.jolcraft.root.description"),
                        ResourceLocation.withDefaultNamespace("textures/block/deepslate_bricks.png"),
                        AdvancementType.TASK,
                        false, false, false
                )
                .addCriterion("tick", CriteriaTriggers.TICK.createCriterion(new PlayerTrigger.TriggerInstance(Optional.of(ContextAwarePredicate.create()))))
                .save(consumer, rootId);

        //Language
        ResourceLocation readLexiconId = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "story/read_lexicon");
        AdvancementHolder readLexiconAdv = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        JolCraftItems.DWARVEN_LEXICON.get(),
                        Component.translatable("advancement.jolcraft.read_lexicon.title"),
                        Component.translatable("advancement.jolcraft.read_lexicon.description"),
                        null, // null means no override background; inherits from root
                        AdvancementType.CHALLENGE,
                        true, true, false
                )
                .addCriterion("knows_dwarvish", HasDwarvenLanguageTrigger.hasLanguage())
                .save(consumer, readLexiconId);

        //Trade with dwarf
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
