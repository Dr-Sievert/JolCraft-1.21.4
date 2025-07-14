package net.sievert.jolcraft.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.block.JolCraftBlocks;
import net.sievert.jolcraft.entity.JolCraftEntities;
import net.minecraft.network.chat.Component;
import net.sievert.jolcraft.item.armor.JolCraftArmorMaterials;
import net.sievert.jolcraft.item.custom.*;
import net.sievert.jolcraft.item.custom.equipment.armor.DeepslateArmorItem;
import net.sievert.jolcraft.item.custom.equipment.armor.MithrilArmorItem;
import net.sievert.jolcraft.item.custom.bounty.BountyCrateItem;
import net.sievert.jolcraft.item.custom.bounty.BountyItem;
import net.sievert.jolcraft.item.custom.bounty.ContractItem;
import net.sievert.jolcraft.item.custom.food.DwarvenBrewItem;
import net.sievert.jolcraft.item.custom.book.*;
import net.sievert.jolcraft.item.tool.JolCraftToolMaterials;
import net.sievert.jolcraft.item.food.JolCraftFoodProperties;

import java.util.List;

public class JolCraftItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(JolCraft.MOD_ID);

    //Core Items
    public static final DeferredItem<Item> DEV_KEY = ITEMS.registerItem("dev_key",
            Item::new, new Item.Properties().rarity(Rarity.EPIC));

    public static final DeferredItem<Item> GOLD_COIN = ITEMS.registerItem("gold_coin",
            Item::new, new Item.Properties().rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> DWARVEN_LEXICON =
            ITEMS.registerItem("dwarven_lexicon", DwarvenLexiconItem::new,
                    new Item.Properties().stacksTo(1).rarity(Rarity.RARE));

    public static final DeferredItem<Item> ANCIENT_DWARVEN_LEXICON =
            ITEMS.registerItem("ancient_dwarven_lexicon", AncientDwarvenLexiconItem::new,
                    new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    public static final DeferredItem<BlockItem> STRONGBOX_ITEM = JolCraftItems.ITEMS.registerItem("strongbox",
            props -> new StrongboxItem(JolCraftBlocks.STRONGBOX.get(), props
                    .stacksTo(1)
                    .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY))
    );

    public static final DeferredItem<Item> LOCKPICK = ITEMS.registerItem("lockpick",
            Item::new, new Item.Properties());


    //Materials, Armors, Trims, Tools and Weapons
    public static final DeferredItem<Item> IMPURE_MITHRIL = ITEMS.registerItem("impure_mithril",
            Item::new, new Item.Properties().fireResistant());

    public static final DeferredItem<Item> PURE_MITHRIL = ITEMS.registerItem("pure_mithril",
            Item::new, new Item.Properties().fireResistant());

    public static final DeferredItem<Item> MITHRIL_INGOT = ITEMS.registerItem("mithril_ingot",
            Item::new, new Item.Properties().fireResistant());

    public static final DeferredItem<Item> MITHRIL_NUGGET = ITEMS.registerItem("mithril_nugget",
            Item::new, new Item.Properties().fireResistant());

    public static final DeferredItem<Item> MITHRIL_HELMET = ITEMS.registerItem("mithril_helmet",
            props -> new MithrilArmorItem(JolCraftArmorMaterials.MITHRIL_ARMOR_MATERIAL, ArmorType.HELMET, props));

    public static final DeferredItem<Item> MITHRIL_CHESTPLATE = ITEMS.registerItem("mithril_chestplate",
            props -> new MithrilArmorItem(JolCraftArmorMaterials.MITHRIL_ARMOR_MATERIAL, ArmorType.CHESTPLATE, props));

    public static final DeferredItem<Item> MITHRIL_LEGGINGS = ITEMS.registerItem("mithril_leggings",
            props -> new MithrilArmorItem(JolCraftArmorMaterials.MITHRIL_ARMOR_MATERIAL, ArmorType.LEGGINGS, props));

    public static final DeferredItem<Item> MITHRIL_BOOTS = ITEMS.registerItem("mithril_boots",
            props -> new MithrilArmorItem(JolCraftArmorMaterials.MITHRIL_ARMOR_MATERIAL, ArmorType.BOOTS, props));


    public static final DeferredItem<Item> DEEPSLATE_PLATE = ITEMS.registerItem("deepslate_plate",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> FORGE_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.registerItem("forge_armor_trim_smithing_template",
            SmithingTemplateItem::createArmorTrimTemplate, new Item.Properties().rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> DEEPSLATE_SWORD = ITEMS.registerItem("deepslate_sword",
            (properties) -> new SwordItem(JolCraftToolMaterials.DEEPSLATE, 3.0F, -2.4F, properties));

    public static final DeferredItem<Item> DEEPSLATE_PICKAXE = ITEMS.registerItem("deepslate_pickaxe",
            (properties) -> new PickaxeItem(ToolMaterial.IRON, 1.0F, -2.8F, properties));

    public static final DeferredItem<ShovelItem> DEEPSLATE_SHOVEL = ITEMS.registerItem("deepslate_shovel",
            (properties) -> new ShovelItem(ToolMaterial.IRON, 1.5F, -3.0F, properties));

    public static final DeferredItem<AxeItem> DEEPSLATE_AXE = ITEMS.registerItem("deepslate_axe",
            (properties) -> new AxeItem(ToolMaterial.IRON, 6.0F, -3.1F, properties));

    public static final DeferredItem<HoeItem> DEEPSLATE_HOE = ITEMS.registerItem("deepslate_hoe",
            (properties) -> new HoeItem(ToolMaterial.IRON, -2.0F, -1.0F, properties));

    public static final DeferredItem<Item> DEEPSLATE_HELMET = ITEMS.registerItem("deepslate_helmet",
            props -> new DeepslateArmorItem(JolCraftArmorMaterials.DEEPSLATE_ARMOR_MATERIAL, ArmorType.HELMET, props));

    public static final DeferredItem<Item> DEEPSLATE_CHESTPLATE = ITEMS.registerItem("deepslate_chestplate",
            props -> new DeepslateArmorItem(JolCraftArmorMaterials.DEEPSLATE_ARMOR_MATERIAL, ArmorType.CHESTPLATE, props));

    public static final DeferredItem<Item> DEEPSLATE_LEGGINGS = ITEMS.registerItem("deepslate_leggings",
            props -> new DeepslateArmorItem(JolCraftArmorMaterials.DEEPSLATE_ARMOR_MATERIAL, ArmorType.LEGGINGS, props));

    public static final DeferredItem<Item> DEEPSLATE_BOOTS = ITEMS.registerItem("deepslate_boots",
            props -> new DeepslateArmorItem(JolCraftArmorMaterials.DEEPSLATE_ARMOR_MATERIAL, ArmorType.BOOTS, props));

    //Animal-related
    public static final DeferredItem<Item> MUFFHORN_FUR = ITEMS.registerItem("muffhorn_fur",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> MUFFHORN_MILK_BUCKET = ITEMS.registerItem("muffhorn_milk_bucket",
            Item::new, new Item.Properties().craftRemainder(Items.BUCKET).component(DataComponents.CONSUMABLE, Consumables.MILK_BUCKET).usingConvertsTo(Items.BUCKET).stacksTo(1));

    //Alchemy
    public static final DeferredItem<Item> INVERIX = ITEMS.registerItem("inverix",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> DEEPMARROW_DUST = ITEMS.registerItem("deepmarrow_dust",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> EARTHBLOOD_DUST = ITEMS.registerItem("earthblood_dust",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> VERDANT_DUST = ITEMS.registerItem("verdant_dust",
            Item::new, new Item.Properties());

    //Bounty
    public static final DeferredItem<Item> PARCHMENT = ITEMS.registerSimpleItem("parchment");

    public static final DeferredItem<Item> BOUNTY = ITEMS.registerItem("bounty",
            BountyItem::new, new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> BOUNTY_CRATE = ITEMS.registerItem("bounty_crate",
            BountyCrateItem::new, new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> RESTOCK_CRATE = ITEMS.registerItem("restock_crate",
            RestockCrateItem::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));

    //Contracts and Associated Items

    public static final DeferredItem<Item> CONTRACT_BLANK = ITEMS.registerItem("contract_blank",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> CONTRACT_WRITTEN = ITEMS.registerItem("contract_written",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> CONTRACT_SIGNED = ITEMS.registerItem("contract_signed",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> GUILD_SIGIL = ITEMS.registerItem("guild_sigil",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> CONTRACT_GUILDMASTER = ITEMS.registerItem("contract_guildmaster",
            Item::new, new Item.Properties().rarity(Rarity.UNCOMMON));


    //Tier 1
    public static final DeferredItem<Item> CONTRACT_MERCHANT = ITEMS.registerItem("contract_merchant",
            ContractItem::new, new Item.Properties().rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> CONTRACT_HISTORIAN = ITEMS.registerItem("contract_historian",
            ContractItem::new, new Item.Properties().rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> CONTRACT_SCRAPPER = ITEMS.registerItem("contract_scrapper",
            ContractItem::new, new Item.Properties().rarity(Rarity.UNCOMMON));

    //Tier 2
    public static final DeferredItem<Item> CONTRACT_GUARD = ITEMS.registerItem("contract_guard",
            ContractItem::new, new Item.Properties().rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> CONTRACT_BREWMASTER = ITEMS.registerItem("contract_brewmaster",
            ContractItem::new, new Item.Properties().rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> CONTRACT_KEEPER = ITEMS.registerItem("contract_keeper",
            ContractItem::new, new Item.Properties().rarity(Rarity.UNCOMMON));

    //Tier 3
    public static final DeferredItem<Item> CONTRACT_MINER = ITEMS.registerItem("contract_miner",
            ContractItem::new, new Item.Properties().rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> CONTRACT_EXPLORER = ITEMS.registerItem("contract_explorer",
            ContractItem::new, new Item.Properties().rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> CONTRACT_ALCHEMIST = ITEMS.registerItem("contract_alchemist",
            ContractItem::new, new Item.Properties().rarity(Rarity.UNCOMMON));

    //Tier 4
    public static final DeferredItem<Item> CONTRACT_ARCANIST = ITEMS.registerItem("contract_arcanist",
            ContractItem::new, new Item.Properties().rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> CONTRACT_PRIEST = ITEMS.registerItem("contract_priest",
            ContractItem::new, new Item.Properties().rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> CONTRACT_ARTISAN = ITEMS.registerItem("contract_artisan",
            ContractItem::new, new Item.Properties().rarity(Rarity.UNCOMMON));

    //Tier 5
    public static final DeferredItem<Item> CONTRACT_CHAMPION = ITEMS.registerItem("contract_champion",
            ContractItem::new, new Item.Properties().rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> CONTRACT_BLACKSMITH = ITEMS.registerItem("contract_blacksmith",
            ContractItem::new, new Item.Properties().rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> CONTRACT_SMELTER = ITEMS.registerItem("contract_smelter",
            ContractItem::new, new Item.Properties().rarity(Rarity.UNCOMMON));


    public static final DeferredItem<Item> QUILL_EMPTY =
            ITEMS.registerItem("quill_empty", QuillItem::new, new Item.Properties().stacksTo(16));

    public static final DeferredItem<Item> QUILL_SMALL = ITEMS.registerItem("quill_small",
            props -> new QuillItem(props.craftRemainder(JolCraftItems.QUILL_EMPTY.get()).stacksTo(1))
    );

    public static final DeferredItem<Item> QUILL_HALF = ITEMS.registerItem("quill_half",
            props -> new QuillItem(props.craftRemainder(JolCraftItems.QUILL_SMALL.get()).stacksTo(1))
    );

    public static final DeferredItem<Item> QUILL_FULL = ITEMS.registerItem("quill_full",
            props -> new QuillItem(props.craftRemainder(JolCraftItems.QUILL_HALF.get()).stacksTo(1))
    );

    //Eggs

    public static final DeferredItem<Item> DWARF_SPAWN_EGG = ITEMS.registerItem("dwarf_spawn_egg",
            (properties) -> new SpawnEggItem(JolCraftEntities.DWARF.get(), properties));

    public static final DeferredItem<Item> DWARF_GUILDMASTER_SPAWN_EGG = ITEMS.registerItem("dwarf_guildmaster_spawn_egg",
            (properties) -> new SpawnEggItem(JolCraftEntities.DWARF_GUILDMASTER.get(), properties));

    public static final DeferredItem<Item> DWARF_HISTORIAN_SPAWN_EGG = ITEMS.registerItem("dwarf_historian_spawn_egg",
            (properties) -> new SpawnEggItem(JolCraftEntities.DWARF_HISTORIAN.get(), properties));

    public static final DeferredItem<Item> DWARF_MERCHANT_SPAWN_EGG = ITEMS.registerItem("dwarf_merchant_spawn_egg",
            (properties) -> new SpawnEggItem(JolCraftEntities.DWARF_MERCHANT.get(), properties));

    public static final DeferredItem<Item> DWARF_SCRAPPER_SPAWN_EGG = ITEMS.registerItem("dwarf_scrapper_spawn_egg",
            (properties) -> new SpawnEggItem(JolCraftEntities.DWARF_SCRAPPER.get(), properties));

    public static final DeferredItem<Item> DWARF_BREWMASTER_SPAWN_EGG = ITEMS.registerItem("dwarf_brewmaster_spawn_egg",
            (properties) -> new SpawnEggItem(JolCraftEntities.DWARF_BREWMASTER.get(), properties));

    public static final DeferredItem<Item> DWARF_GUARD_SPAWN_EGG = ITEMS.registerItem("dwarf_guard_spawn_egg",
            (properties) -> new SpawnEggItem(JolCraftEntities.DWARF_GUARD.get(), properties));

    public static final DeferredItem<Item> DWARF_KEEPER_SPAWN_EGG = ITEMS.registerItem("dwarf_keeper_spawn_egg",
            (properties) -> new SpawnEggItem(JolCraftEntities.DWARF_KEEPER.get(), properties));

    public static final DeferredItem<Item> MUFFHORN_SPAWN_EGG = ITEMS.registerItem("muffhorn_spawn_egg",
            (properties) -> new SpawnEggItem(JolCraftEntities.MUFFHORN.get(), properties));



    //Gems
    public static final DeferredItem<Item> AEGISCORE = ITEMS.registerItem("aegiscore",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> ASHFANG = ITEMS.registerItem("ashfang",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> DEEPMARROW = ITEMS.registerItem("deepmarrow",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> EARTHBLOOD = ITEMS.registerItem("earthblood",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> EMBERGLASS = ITEMS.registerItem("emberglass",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> FROSTVEIN = ITEMS.registerItem("frostvein",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> GRIMSTONE = ITEMS.registerItem("grimstone",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> IRONHEART = ITEMS.registerItem("ironheart",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> LUMIERE = ITEMS.registerItem("lumiere",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> MOONSHARD = ITEMS.registerItem("moonshard",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> RUSTAGATE = ITEMS.registerItem("rustagate",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> SKYBURROW = ITEMS.registerItem("skyburrow",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> SUNGLEAM = ITEMS.registerItem("sungleam",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> VERDANITE = ITEMS.registerItem("verdanite",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> WOECRYSTAL = ITEMS.registerItem("woecrystal",
            Item::new, new Item.Properties());

    //Crops, food and brewing

    public static final DeferredItem<Item> BARLEY_SEEDS = ITEMS.registerItem("barley_seeds",
            properties -> new BlockItem(JolCraftBlocks.BARLEY_CROP.get(), properties));

    public static final DeferredItem<Item> BARLEY =
            ITEMS.registerItem("barley", Item::new,
                    new Item.Properties());

    public static final DeferredItem<Item> BARLEY_MALT =
            ITEMS.registerItem("barley_malt", Item::new,
                    new Item.Properties());

    public static final DeferredItem<Item> ASGARNIAN_SEEDS = ITEMS.registerItem("asgarnian_seeds",
            properties -> new BlockItem(JolCraftBlocks.ASGARNIAN_CROP_BOTTOM.get(), properties));

    public static final DeferredItem<Item> ASGARNIAN_HOPS =
            ITEMS.registerItem("asgarnian_hops", Item::new,
                    new Item.Properties());

    public static final DeferredItem<Item> DUSKHOLD_SEEDS = ITEMS.registerItem("duskhold_seeds",
            properties -> new BlockItem(JolCraftBlocks.DUSKHOLD_CROP_BOTTOM.get(), properties));

    public static final DeferredItem<Item> DUSKHOLD_HOPS =
            ITEMS.registerItem("duskhold_hops", Item::new,
                    new Item.Properties());

    public static final DeferredItem<Item> KRANDONIAN_SEEDS = ITEMS.registerItem("krandonian_seeds",
            properties -> new BlockItem(JolCraftBlocks.KRANDONIAN_CROP_BOTTOM.get(), properties));

    public static final DeferredItem<Item> KRANDONIAN_HOPS =
            ITEMS.registerItem("krandonian_hops", Item::new,
                    new Item.Properties());

    public static final DeferredItem<Item> YANILLIAN_SEEDS = ITEMS.registerItem("yanillian_seeds",
            properties -> new BlockItem(JolCraftBlocks.YANILLIAN_CROP_BOTTOM.get(), properties));

    public static final DeferredItem<Item> YANILLIAN_HOPS =
            ITEMS.registerItem("yanillian_hops", Item::new,
                    new Item.Properties());

    public static final DeferredItem<Item> YEAST =
            ITEMS.registerItem("yeast", Item::new,
                    new Item.Properties().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE));

    public static final DeferredItem<Item> GLASS_MUG =
            ITEMS.registerItem("glass_mug", Item::new,
                    new Item.Properties().stacksTo(16));

    public static final DeferredItem<Item> DWARVEN_BREW =
            ITEMS.registerItem("dwarven_brew",  (properties) -> new DwarvenBrewItem(properties.food(JolCraftFoodProperties.DWARVEN_BREW, JolCraftFoodProperties.DWARVEN_BREW_EFFECT).usingConvertsTo(JolCraftItems.GLASS_MUG.get()).stacksTo(1)));

    public static final DeferredItem<Item> DEEPSLATE_BULBS = ITEMS.registerItem("deepslate_bulbs",
            properties -> new BlockItem(JolCraftBlocks.DEEPSLATE_BULBS_CROP.get(), properties.food(JolCraftFoodProperties.DWARVEN_BREW, JolCraftFoodProperties.DEEPSLATE_BULBS_EFFECT)));

    //Reputation
    public static final DeferredItem<Item> REPUTATION_TABLET_0 =
            ITEMS.registerItem("reputation_tablet_0", ReputationTabletItem::new,
                    new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));

    public static final DeferredItem<Item> REPUTATION_TABLET_1 =
            ITEMS.registerItem("reputation_tablet_1", ReputationTabletItem::new,
                    new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));

    public static final DeferredItem<Item> REPUTATION_TABLET_2 =
            ITEMS.registerItem("reputation_tablet_2", ReputationTabletItem::new,
                    new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> REPUTATION_TABLET_3 =
            ITEMS.registerItem("reputation_tablet_3", ReputationTabletItem::new,
                    new Item.Properties().stacksTo(1).rarity(Rarity.RARE));

    public static final DeferredItem<Item> REPUTATION_TABLET_4 =
            ITEMS.registerItem("reputation_tablet_4", ReputationTabletItem::new,
                    new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    // Tomes
    public static final DeferredItem<Item> DWARVEN_TOME = ITEMS.registerSimpleItem("dwarven_tome");

    public static final DeferredItem<Item> UNIDENTIFIED_DWARVEN_TOME =
            ITEMS.registerItem("unidentified_dwarven_tome", properties -> new UnidentifiedDwarvenTomeItem(properties) {
            }, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON));

    public static final DeferredItem<Item> DWARVEN_TOME_COMMON =
            ITEMS.registerItem("dwarven_tome_common", properties -> new DwarvenTomeItem(properties) {
            }, new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));

    public static final DeferredItem<Item> DWARVEN_TOME_UNCOMMON =
            ITEMS.registerItem("dwarven_tome_uncommon", properties -> new DwarvenTomeItem(properties) {
            }, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> DWARVEN_TOME_RARE =
            ITEMS.registerItem("dwarven_tome_rare", properties -> new DwarvenTomeItem(properties) {
            }, new Item.Properties().stacksTo(1).rarity(Rarity.RARE));

    public static final DeferredItem<Item> DWARVEN_TOME_EPIC =
            ITEMS.registerItem("dwarven_tome_epic", properties -> new DwarvenTomeItem(properties) {
            }, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    public static final DeferredItem<Item> ANCIENT_DWARVEN_TOME = ITEMS.registerSimpleItem("ancient_dwarven_tome");

    public static final DeferredItem<Item> ANCIENT_UNIDENTIFIED_DWARVEN_TOME =
            ITEMS.registerItem("unidentified_ancient_dwarven_tome", properties -> new AncientUnidentifiedTomeItem(properties) {
            }, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON));

    public static final DeferredItem<Item> ANCIENT_DWARVEN_TOME_COMMON =
            ITEMS.registerItem("ancient_dwarven_tome_common", properties -> new AncientDwarvenTomeItem(properties) {
            }, new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));

    public static final DeferredItem<Item> ANCIENT_DWARVEN_TOME_UNCOMMON =
            ITEMS.registerItem("ancient_dwarven_tome_uncommon", properties -> new AncientDwarvenTomeItem(properties) {
            }, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> ANCIENT_DWARVEN_TOME_RARE =
            ITEMS.registerItem("ancient_dwarven_tome_rare", properties -> new AncientDwarvenTomeItem(properties) {
            }, new Item.Properties().stacksTo(1).rarity(Rarity.RARE));

    public static final DeferredItem<Item> ANCIENT_DWARVEN_TOME_EPIC =
            ITEMS.registerItem("ancient_dwarven_tome_epic", properties -> new AncientDwarvenTomeItem(properties) {
            }, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    public static final DeferredItem<Item> LEGENDARY_PAGE = ITEMS.registerItem("legendary_page",
            LegendaryItem::new, new Item.Properties());

    public static final DeferredItem<Item> LEGENDARY_ANCIENT_UNIDENTIFIED_DWARVEN_TOME =
            ITEMS.registerItem("legendary_unidentified_ancient_dwarven_tome", properties -> new LegendaryAncientUnidentifiedTomeItem(properties) {
            }, new Item.Properties().stacksTo(16));

    public static final DeferredItem<Item> ANCIENT_DWARVEN_TOME_LEGENDARY =
            ITEMS.registerItem("ancient_dwarven_tome_legendary", properties -> new LegendaryAncientDwarvenTomeItem(properties) {
            }, new Item.Properties().stacksTo(1));


    //Tools
    public static final DeferredItem<Item> COPPER_SPANNER =
            ITEMS.registerItem("copper_spanner", properties -> new SpannerItem(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.spanner").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties().durability(64).stacksTo(1).enchantable(10).repairable(Items.COPPER_INGOT));

    public static final DeferredItem<Item> IRON_SPANNER =
            ITEMS.registerItem("iron_spanner", properties -> new SpannerItem(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.spanner").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties().durability(64).stacksTo(1).enchantable(10).repairable(Items.IRON_INGOT));


    //Scrap
    public static final DeferredItem<Item> SCRAP = ITEMS.registerSimpleItem("scrap");

    public static final DeferredItem<Item> SCRAP_HEAP = ITEMS.registerSimpleItem("scrap_heap");

    public static final DeferredItem<Item> BROKEN_PICKAXE =
            ITEMS.registerItem("broken_pickaxe", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.salvage").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> BROKEN_AMULET =
            ITEMS.registerItem("broken_amulet", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.salvage").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> BROKEN_BELT =
            ITEMS.registerItem("broken_belt", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.salvage").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> BROKEN_COINS =
            ITEMS.registerItem("broken_coins", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.salvage").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties());

    public static final DeferredItem<Item> DEEPSLATE_MUG =
            ITEMS.registerItem("deepslate_mug", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.salvage").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties().stacksTo(16));

    public static final DeferredItem<Item> EXPIRED_POTION =
            ITEMS.registerItem("expired_potion", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.salvage").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> INGOT_MOULD =
            ITEMS.registerItem("ingot_mould", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.salvage").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> MITHRIL_SALVAGE =
            ITEMS.registerItem("mithril_salvage", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.salvage").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties());

    public static final DeferredItem<Item> OLD_FABRIC =
            ITEMS.registerItem("old_fabric", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.salvage").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties());

    public static final DeferredItem<Item> RUSTY_TONGS =
            ITEMS.registerItem("rusty_tongs", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.salvage").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties().stacksTo(1));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
