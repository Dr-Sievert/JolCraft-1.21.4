package net.sievert.jolcraft.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.item.JolCraftItems;

public class JolCraftModelProvider extends ModelProvider {
    public JolCraftModelProvider(PackOutput output) {
        super(output, JolCraft.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        //Core
        itemModels.generateFlatItem(JolCraftItems.GOLD_COIN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DWARVEN_LEXICON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.PARCHMENT.get(), ModelTemplates.FLAT_ITEM);

        //Bounty
        itemModels.generateFlatItem(JolCraftItems.BOUNTY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.BOUNTY_CRATE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.RESTOCK_CRATE.get(), ModelTemplates.FLAT_ITEM);

        //Contracts and related
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_BLANK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_WRITTEN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_SIGNED.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.GUILD_SIGIL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_GUILDMASTER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_MERCHANT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_HISTORIAN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_SCRAPPER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_GUARD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_BREWMASTER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_KEEPER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_MINER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_EXPLORER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_ALCHEMIST.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_ARCANIST.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_PRIEST.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_ARTISAN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_CHAMPION.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_BLACKSMITH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_SMELTER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.QUILL_EMPTY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.QUILL_SMALL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.QUILL_HALF.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.QUILL_FULL.get(), ModelTemplates.FLAT_ITEM);


        //Gems
        itemModels.generateFlatItem(JolCraftItems.AEGISCORE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.ASHFANG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DEEPMARROW.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.EARTHBLOOD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.EMBERGLASS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.FROSTVEIN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.GRIMSTONE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.IRONHEART.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.LUMIERE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.MOONSHARD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.RUSTAGATE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.SKYBURROW.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.SUNGLEAM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.VERDANITE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.WOECRYSTAL.get(), ModelTemplates.FLAT_ITEM);

        //Reputation
        itemModels.generateFlatItem(JolCraftItems.REPUTATION_TABLET_0.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.REPUTATION_TABLET_1.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.REPUTATION_TABLET_2.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.REPUTATION_TABLET_3.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.REPUTATION_TABLET_4.get(), ModelTemplates.FLAT_ITEM);


        //Tomes
        itemModels.generateFlatItem(JolCraftItems.DWARVEN_TOME.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DWARVEN_TOME_COMMON.get(), JolCraftItems.DWARVEN_TOME.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DWARVEN_TOME_UNCOMMON.get(), JolCraftItems.DWARVEN_TOME.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DWARVEN_TOME_RARE.get(), JolCraftItems.DWARVEN_TOME.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DWARVEN_TOME_EPIC.get(), JolCraftItems.DWARVEN_TOME.get(), ModelTemplates.FLAT_ITEM);


        //Tools and weapons
        itemModels.generateFlatItem(JolCraftItems.COPPER_SPANNER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(JolCraftItems.IRON_SPANNER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);


        //Scrap
        itemModels.generateFlatItem(JolCraftItems.SCRAP.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.SCRAP_HEAP.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.BROKEN_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(JolCraftItems.BROKEN_AMULET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.BROKEN_BELT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.BROKEN_COINS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DEEPSLATE_MUG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.EXPIRED_POTION.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.INGOT_MOULD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.MITHRIL_SALVAGE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.OLD_FABRIC.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.RUSTY_TONGS.get(), ModelTemplates.FLAT_ITEM);

        //Eggs
        itemModels.generateSpawnEgg(
                JolCraftItems.DWARF_SPAWN_EGG.get(),
                -4355214,
                -11125709
        );
        itemModels.generateSpawnEgg(
                JolCraftItems.DWARF_GUILDMASTER_SPAWN_EGG.get(),
                -4355214,
                -11716526
        );
        itemModels.generateSpawnEgg(
                JolCraftItems.DWARF_HISTORIAN_SPAWN_EGG.get(),
                -4355214,
                -8807323
        );
        itemModels.generateSpawnEgg(
                JolCraftItems.DWARF_MERCHANT_SPAWN_EGG.get(),
                -4355214,
                -1399760
        );
        itemModels.generateSpawnEgg(
                JolCraftItems.DWARF_SCRAPPER_SPAWN_EGG.get(),
                -4355214,
                -3380960
        );

        itemModels.generateSpawnEgg(
                JolCraftItems.DWARF_BREWMASTER_SPAWN_EGG.get(),
                -4355214,
                -396380
        );
        itemModels.generateSpawnEgg(
                JolCraftItems.DWARF_GUARD_SPAWN_EGG.get(),
                -4355214,
                -2233622
        );
        itemModels.generateSpawnEgg(
                JolCraftItems.DWARF_KEEPER_SPAWN_EGG.get(),
                -4355214,
                -7799040
        );



    }

}
