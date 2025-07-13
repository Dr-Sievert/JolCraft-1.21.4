package net.sievert.jolcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.util.JolCraftTags;
import net.sievert.jolcraft.item.JolCraftItems;

import java.util.concurrent.CompletableFuture;

public class JolCraftItemTagProvider extends ItemTagsProvider {
    public JolCraftItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                  CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags, JolCraft.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .add(JolCraftItems.COPPER_SPANNER.get())
                .add(JolCraftItems.IRON_SPANNER.get());

        tag(JolCraftTags.Items.INK_AND_QUILLS)
                .add(JolCraftItems.QUILL_FULL.get())
                .add(JolCraftItems.QUILL_HALF.get())
                .add(JolCraftItems.QUILL_SMALL.get());


        tag(JolCraftTags.Items.SPAWN_EGGS)
                .add(JolCraftItems.DWARF_SPAWN_EGG.get())
                .add(JolCraftItems.DWARF_GUARD_SPAWN_EGG.get())
                .add(JolCraftItems.DWARF_HISTORIAN_SPAWN_EGG.get())
                .add(JolCraftItems.DWARF_SCRAPPER_SPAWN_EGG.get())
                .add(JolCraftItems.DWARF_MERCHANT_SPAWN_EGG.get());

        tag(JolCraftTags.Items.GEMS)
                .add(JolCraftItems.AEGISCORE.get())
                .add(JolCraftItems.ASHFANG.get())
                .add(JolCraftItems.DEEPMARROW.get())
                .add(JolCraftItems.EARTHBLOOD.get())
                .add(JolCraftItems.EMBERGLASS.get())
                .add(JolCraftItems.FROSTVEIN.get())
                .add(JolCraftItems.GRIMSTONE.get())
                .add(JolCraftItems.IRONHEART.get())
                .add(JolCraftItems.LUMIERE.get())
                .add(JolCraftItems.MOONSHARD.get())
                .add(JolCraftItems.RUSTAGATE.get())
                .add(JolCraftItems.SKYBURROW.get())
                .add(JolCraftItems.SUNGLEAM.get())
                .add(JolCraftItems.VERDANITE.get())
                .add(JolCraftItems.WOECRYSTAL.get());

        tag(JolCraftTags.Items.GENERAL_SCRAP)
                .add(Items.TRIPWIRE_HOOK)
                .add(Items.FLINT_AND_STEEL)
                .add(Items.SHIELD)
                .add(Items.FILLED_MAP)
                .add(JolCraftItems.DEEPSLATE_MUG.get())
                .add(JolCraftItems.EXPIRED_POTION.get());

        tag(JolCraftTags.Items.TEXTILE_SCRAP)
                .add(Items.LEATHER_HELMET)
                .add(Items.LEATHER_CHESTPLATE)
                .add(Items.LEATHER_LEGGINGS)
                .add(Items.LEATHER_BOOTS)
                .add(JolCraftItems.OLD_FABRIC.get());

        tag(JolCraftTags.Items.REDSTONE_SCRAP)
                .add(Items.COMPASS)
                .add(Items.CLOCK)
                .add(Items.REPEATER)
                .add(Items.COMPARATOR);

        tag(JolCraftTags.Items.IRON_SCRAP)
                .add(Items.IRON_SWORD)
                .add(Items.IRON_PICKAXE)
                .add(Items.IRON_SHOVEL)
                .add(Items.IRON_AXE)
                .add(Items.IRON_HOE)
                .add(Items.IRON_HELMET)
                .add(Items.IRON_CHESTPLATE)
                .add(Items.IRON_LEGGINGS)
                .add(Items.IRON_BOOTS)
                .add(Items.IRON_HORSE_ARMOR)
                .add(Items.SHEARS)
                .add(JolCraftItems.BROKEN_PICKAXE.get())
                .add(JolCraftItems.BROKEN_AMULET.get())
                .add(JolCraftItems.RUSTY_TONGS.get())
                .add(JolCraftItems.INGOT_MOULD.get());

        tag(JolCraftTags.Items.GOLD_SCRAP)
                .add(Items.GOLDEN_SWORD)
                .add(Items.GOLDEN_PICKAXE)
                .add(Items.GOLDEN_SHOVEL)
                .add(Items.GOLDEN_AXE)
                .add(Items.GOLDEN_HOE)
                .add(Items.GOLDEN_HELMET)
                .add(Items.GOLDEN_CHESTPLATE)
                .add(Items.GOLDEN_LEGGINGS)
                .add(Items.GOLDEN_BOOTS)
                .add(Items.GOLDEN_HORSE_ARMOR)
                .add(JolCraftItems.BROKEN_BELT.get())
                .add(JolCraftItems.BROKEN_COINS.get());

        tag(JolCraftTags.Items.MITHRIL_SCRAP)
                .add(JolCraftItems.MITHRIL_SALVAGE.get());

        tag(JolCraftTags.Items.GLOBAL_SCRAP)
                .addTag(JolCraftTags.Items.GENERAL_SCRAP)
                .addTag(JolCraftTags.Items.TEXTILE_SCRAP)
                .addTag(JolCraftTags.Items.REDSTONE_SCRAP)
                .addTag(JolCraftTags.Items.IRON_SCRAP)
                .addTag(JolCraftTags.Items.GOLD_SCRAP)
                .addTag(JolCraftTags.Items.MITHRIL_SCRAP);

        tag(JolCraftTags.Items.SIGNED_CONTRACTS)
                .add(JolCraftItems.CONTRACT_GUILDMASTER.get())
                .add(JolCraftItems.CONTRACT_MERCHANT.get())
                .add(JolCraftItems.CONTRACT_HISTORIAN.get())
                .add(JolCraftItems.CONTRACT_SCRAPPER.get())
                .add(JolCraftItems.CONTRACT_GUARD.get())
                .add(JolCraftItems.CONTRACT_EXPLORER.get())
                .add(JolCraftItems.CONTRACT_KEEPER.get())
                .add(JolCraftItems.CONTRACT_MINER.get())
                .add(JolCraftItems.CONTRACT_BREWMASTER.get())
                .add(JolCraftItems.CONTRACT_ARTISAN.get())
                .add(JolCraftItems.CONTRACT_ALCHEMIST.get())
                .add(JolCraftItems.CONTRACT_ARCANIST.get())
                .add(JolCraftItems.CONTRACT_PRIEST.get())
                .add(JolCraftItems.CONTRACT_CHAMPION.get())
                .add(JolCraftItems.CONTRACT_BLACKSMITH.get())
                .add(JolCraftItems.CONTRACT_SMELTER.get());

        tag(JolCraftTags.Items.REPUTATION_TABLETS)
                .add(JolCraftItems.REPUTATION_TABLET_0.get())
                .add(JolCraftItems.REPUTATION_TABLET_1.get())
                .add(JolCraftItems.REPUTATION_TABLET_2.get())
                .add(JolCraftItems.REPUTATION_TABLET_3.get())
                .add(JolCraftItems.REPUTATION_TABLET_4.get());

        tag(JolCraftTags.Items.HOPS)
                .add(JolCraftItems.ASGARNIAN_HOPS.get())
                .add(JolCraftItems.DUSKHOLD_HOPS.get())
                .add(JolCraftItems.KRANDONIAN_HOPS.get())
                .add(JolCraftItems.YANILLIAN_HOPS.get());

        tag(JolCraftTags.Items.REPAIRS_DEEPSLATE)
                .add(JolCraftItems.DEEPSLATE_PLATE.get());

        tag(ItemTags.TRIMMABLE_ARMOR)
                .add(JolCraftItems.DEEPSLATE_HELMET.get())
                .add(JolCraftItems.DEEPSLATE_CHESTPLATE.get())
                .add(JolCraftItems.DEEPSLATE_LEGGINGS.get())
                .add(JolCraftItems.DEEPSLATE_BOOTS.get());

        tag(ItemTags.TRIM_MATERIALS)
                .add(JolCraftItems.DEEPSLATE_PLATE.get())
                .add(JolCraftItems.AEGISCORE.get())
                .add(JolCraftItems.ASHFANG.get())
                .add(JolCraftItems.DEEPMARROW.get())
                .add(JolCraftItems.EARTHBLOOD.get())
                .add(JolCraftItems.EMBERGLASS.get())
                .add(JolCraftItems.FROSTVEIN.get())
                .add(JolCraftItems.GRIMSTONE.get())
                .add(JolCraftItems.IRONHEART.get())
                .add(JolCraftItems.LUMIERE.get())
                .add(JolCraftItems.MOONSHARD.get())
                .add(JolCraftItems.RUSTAGATE.get())
                .add(JolCraftItems.SKYBURROW.get())
                .add(JolCraftItems.SUNGLEAM.get())
                .add(JolCraftItems.VERDANITE.get())
                .add(JolCraftItems.WOECRYSTAL.get());

        tag(JolCraftTags.Items.LEGENDARY_ITEMS)
                .add(JolCraftItems.DEEPSLATE_SWORD.get())
                .add(JolCraftItems.LEGENDARY_PAGE.get())
                .add(JolCraftItems.LEGENDARY_ANCIENT_UNIDENTIFIED_DWARVEN_TOME.get())
                .add(JolCraftItems.ANCIENT_DWARVEN_TOME_LEGENDARY.get());

    }


}