package net.sievert.jolcraft.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.block.JolCraftBlocks;

import java.util.function.Supplier;

public class JolCraftCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, JolCraft.MOD_ID);

    public static final Supplier<CreativeModeTab> JOLCRAFT_ITEMS =
            CREATIVE_MODE_TABS.register("jolcraft_items_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.jolcraft.jolcraft_items_tab"))
                    .icon(() -> new ItemStack(JolCraftItems.GOLD_COIN.get()))
                    .displayItems((pParameters, pOutput) -> {
                        //Testing
                        //pOutput.accept(PotionContents.createItemStack(Items.POTION, JolCraftPotions.CURSE));
                        pOutput.accept(JolCraftItems.DEV_KEY);

                        //Real
                        pOutput.accept(JolCraftItems.DWARVEN_LEXICON);
                        pOutput.accept(JolCraftItems.ANCIENT_DWARVEN_LEXICON);
                        pOutput.accept(JolCraftItems.GOLD_COIN);
                        pOutput.accept(JolCraftBlocks.HEARTH);
                        pOutput.accept(JolCraftItems.STRONGBOX_ITEM);
                        pOutput.accept(JolCraftItems.LOCKPICK);

                        pOutput.accept(JolCraftBlocks.DEEPSLATE_MITHRIL_ORE);
                        pOutput.accept(JolCraftBlocks.PURE_MITHRIL_BLOCK);
                        pOutput.accept(JolCraftBlocks.MITHRIL_BLOCK);
                        pOutput.accept(JolCraftItems.IMPURE_MITHRIL);
                        pOutput.accept(JolCraftItems.PURE_MITHRIL);
                        pOutput.accept(JolCraftItems.MITHRIL_INGOT);
                        pOutput.accept(JolCraftItems.MITHRIL_NUGGET);

                        pOutput.accept(JolCraftItems.MUFFHORN_MILK_BUCKET);
                        pOutput.accept(JolCraftItems.MUFFHORN_FUR);
                        pOutput.accept(JolCraftBlocks.MUFFHORN_FUR_BLOCK);

                        pOutput.accept(JolCraftBlocks.VERDANT_SOIL);
                        pOutput.accept(JolCraftBlocks.VERDANT_FARMLAND);
                        pOutput.accept(JolCraftItems.BARLEY_SEEDS);
                        pOutput.accept(JolCraftItems.BARLEY_SEEDS);
                        pOutput.accept(JolCraftItems.BARLEY);
                        pOutput.accept(JolCraftBlocks.BARLEY_BLOCK);
                        pOutput.accept(JolCraftItems.BARLEY_MALT);
                        pOutput.accept(JolCraftItems.ASGARNIAN_SEEDS);
                        pOutput.accept(JolCraftItems.DUSKHOLD_SEEDS);
                        pOutput.accept(JolCraftItems.KRANDONIAN_SEEDS);
                        pOutput.accept(JolCraftItems.YANILLIAN_SEEDS);
                        pOutput.accept(JolCraftItems.ASGARNIAN_HOPS);
                        pOutput.accept(JolCraftItems.DUSKHOLD_HOPS);
                        pOutput.accept(JolCraftItems.KRANDONIAN_HOPS);
                        pOutput.accept(JolCraftItems.YANILLIAN_HOPS);
                        pOutput.accept(JolCraftItems.YEAST);
                        pOutput.accept(JolCraftItems.GLASS_MUG);
                        pOutput.accept(JolCraftItems.DWARVEN_BREW);

                        pOutput.accept(JolCraftItems.INVERIX);
                        pOutput.accept(JolCraftItems.DEEPMARROW_DUST);
                        pOutput.accept(JolCraftItems.EARTHBLOOD_DUST);
                        pOutput.accept(JolCraftItems.VERDANT_DUST);

                        pOutput.accept(JolCraftBlocks.DUSKCAP);
                        pOutput.accept(JolCraftBlocks.FESTERLING);

                        pOutput.accept(JolCraftItems.DEEPSLATE_BULBS);
                        pOutput.accept(JolCraftItems.DEEPSLATE_PLATE);
                        pOutput.accept(JolCraftItems.DEEPSLATE_SWORD);
                        pOutput.accept(JolCraftItems.DEEPSLATE_PICKAXE);
                        pOutput.accept(JolCraftItems.DEEPSLATE_SHOVEL);
                        pOutput.accept(JolCraftItems.DEEPSLATE_AXE);
                        pOutput.accept(JolCraftItems.DEEPSLATE_HOE);
                        pOutput.accept(JolCraftItems.DEEPSLATE_HELMET);
                        pOutput.accept(JolCraftItems.DEEPSLATE_CHESTPLATE);
                        pOutput.accept(JolCraftItems.DEEPSLATE_LEGGINGS);
                        pOutput.accept(JolCraftItems.DEEPSLATE_BOOTS);

                        pOutput.accept(JolCraftItems.FORGE_ARMOR_TRIM_SMITHING_TEMPLATE);

                        pOutput.accept(JolCraftItems.QUILL_EMPTY);
                        pOutput.accept(JolCraftItems.QUILL_FULL);
                        pOutput.accept(JolCraftItems.PARCHMENT);
                        pOutput.accept(JolCraftItems.CONTRACT_BLANK);
                        pOutput.accept(JolCraftItems.CONTRACT_WRITTEN);
                        pOutput.accept(JolCraftItems.CONTRACT_SIGNED);
                        pOutput.accept(JolCraftItems.GUILD_SIGIL);
                        pOutput.accept(JolCraftItems.CONTRACT_GUILDMASTER);
                        pOutput.accept(JolCraftItems.CONTRACT_MERCHANT);
                        pOutput.accept(JolCraftItems.CONTRACT_HISTORIAN);
                        pOutput.accept(JolCraftItems.CONTRACT_SCRAPPER);
                        pOutput.accept(JolCraftItems.CONTRACT_GUARD);
                        pOutput.accept(JolCraftItems.CONTRACT_EXPLORER);
                        pOutput.accept(JolCraftItems.CONTRACT_KEEPER);
                        pOutput.accept(JolCraftItems.CONTRACT_MINER);
                        pOutput.accept(JolCraftItems.CONTRACT_BREWMASTER);
                        pOutput.accept(JolCraftItems.CONTRACT_ARTISAN);
                        pOutput.accept(JolCraftItems.CONTRACT_ALCHEMIST);
                        pOutput.accept(JolCraftItems.CONTRACT_ARCANIST);
                        pOutput.accept(JolCraftItems.CONTRACT_PRIEST);
                        pOutput.accept(JolCraftItems.CONTRACT_CHAMPION);
                        pOutput.accept(JolCraftItems.CONTRACT_BLACKSMITH);
                        pOutput.accept(JolCraftItems.CONTRACT_SMELTER);

                        pOutput.accept(JolCraftItems.REPUTATION_TABLET_0);
                        pOutput.accept(JolCraftItems.REPUTATION_TABLET_1);
                        pOutput.accept(JolCraftItems.REPUTATION_TABLET_2);
                        pOutput.accept(JolCraftItems.REPUTATION_TABLET_3);
                        pOutput.accept(JolCraftItems.REPUTATION_TABLET_4);

                        pOutput.accept(JolCraftItems.BOUNTY);
                        pOutput.accept(JolCraftItems.BOUNTY_CRATE);
                        pOutput.accept(JolCraftItems.RESTOCK_CRATE);

                        pOutput.accept(JolCraftItems.AEGISCORE);
                        pOutput.accept(JolCraftItems.ASHFANG);
                        pOutput.accept(JolCraftItems.DEEPMARROW);
                        pOutput.accept(JolCraftItems.EARTHBLOOD);
                        pOutput.accept(JolCraftItems.EMBERGLASS);
                        pOutput.accept(JolCraftItems.FROSTVEIN);
                        pOutput.accept(JolCraftItems.GRIMSTONE);
                        pOutput.accept(JolCraftItems.IRONHEART);
                        pOutput.accept(JolCraftItems.LUMIERE);
                        pOutput.accept(JolCraftItems.MOONSHARD);
                        pOutput.accept(JolCraftItems.RUSTAGATE);
                        pOutput.accept(JolCraftItems.SKYBURROW);
                        pOutput.accept(JolCraftItems.SUNGLEAM);
                        pOutput.accept(JolCraftItems.VERDANITE);
                        pOutput.accept(JolCraftItems.WOECRYSTAL);

                        pOutput.accept(JolCraftItems.UNIDENTIFIED_DWARVEN_TOME);
                        pOutput.accept(JolCraftItems.ANCIENT_UNIDENTIFIED_DWARVEN_TOME);
                        pOutput.accept(JolCraftItems.LEGENDARY_PAGE);
                        pOutput.accept(JolCraftItems.LEGENDARY_ANCIENT_UNIDENTIFIED_DWARVEN_TOME);
                        pOutput.accept(JolCraftItems.ANCIENT_DWARVEN_TOME_LEGENDARY);

                        pOutput.accept(JolCraftItems.COPPER_SPANNER);
                        pOutput.accept(JolCraftItems.IRON_SPANNER);

                        pOutput.accept(JolCraftItems.SCRAP);
                        pOutput.accept(JolCraftItems.SCRAP_HEAP);
                        pOutput.accept(JolCraftItems.BROKEN_PICKAXE);
                        pOutput.accept(JolCraftItems.BROKEN_AMULET);
                        pOutput.accept(JolCraftItems.BROKEN_BELT);
                        pOutput.accept(JolCraftItems.BROKEN_COINS);
                        pOutput.accept(JolCraftItems.DEEPSLATE_MUG);
                        pOutput.accept(JolCraftItems.EXPIRED_POTION);
                        pOutput.accept(JolCraftItems.INGOT_MOULD);
                        pOutput.accept(JolCraftItems.MITHRIL_SALVAGE);
                        pOutput.accept(JolCraftItems.OLD_FABRIC);
                        pOutput.accept(JolCraftItems.RUSTY_TONGS);

                    }).build());

    public static final Supplier<CreativeModeTab> JOLCRAFT_EGGS =
            CREATIVE_MODE_TABS.register("jolcraft_egg_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.jolcraft.jolcraft_egg_tab"))
                    .icon(() -> new ItemStack(JolCraftItems.DWARF_SPAWN_EGG.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "jolcraft_items_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(JolCraftItems.DWARF_SPAWN_EGG);
                        pOutput.accept(JolCraftItems.DWARF_GUILDMASTER_SPAWN_EGG);
                        pOutput.accept(JolCraftItems.DWARF_HISTORIAN_SPAWN_EGG);
                        pOutput.accept(JolCraftItems.DWARF_MERCHANT_SPAWN_EGG);
                        pOutput.accept(JolCraftItems.DWARF_SCRAPPER_SPAWN_EGG);
                        pOutput.accept(JolCraftItems.DWARF_BREWMASTER_SPAWN_EGG);
                        pOutput.accept(JolCraftItems.DWARF_GUARD_SPAWN_EGG);
                        pOutput.accept(JolCraftItems.DWARF_KEEPER_SPAWN_EGG);
                        pOutput.accept(JolCraftItems.MUFFHORN_SPAWN_EGG);
                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
