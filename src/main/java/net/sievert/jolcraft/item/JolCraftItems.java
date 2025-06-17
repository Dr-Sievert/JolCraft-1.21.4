package net.sievert.jolcraft.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.capability.JolCraftCapabilities;
import net.sievert.jolcraft.component.JolCraftDataComponents;
import net.sievert.jolcraft.entity.JolCraftEntities;
import net.minecraft.network.chat.Component;
import net.sievert.jolcraft.item.custom.DwarvenLexiconItem;
import net.sievert.jolcraft.item.custom.QuillItem;
import net.sievert.jolcraft.item.custom.SpannerItem;

import java.util.List;

public class JolCraftItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(JolCraft.MOD_ID);

    //Clutter
    // public static final DeferredItem<Item> GOLD_COIN = ITEMS.registerSimpleItem("gold_coin");

    //Core Items
    public static final DeferredItem<Item> GOLD_COIN = ITEMS.registerItem("gold_coin",
            Item::new, new Item.Properties().rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> DWARVEN_LEXICON =
            ITEMS.registerItem("dwarven_lexicon", properties -> new DwarvenLexiconItem(properties) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
                    if (context.level() != null && context.level().isClientSide()) {
                        if (Minecraft.getInstance().player != null &&
                                Minecraft.getInstance().player.getCapability(JolCraftCapabilities.DWARVEN_LANGUAGE) != null &&
                                Minecraft.getInstance().player.getCapability(JolCraftCapabilities.DWARVEN_LANGUAGE).knowsLanguage()) {

                            tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_lexicon.unlocked").withStyle(ChatFormatting.GRAY));
                        } else {
                            tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_lexicon.locked").withStyle(ChatFormatting.GRAY));
                        }
                    }

                    super.appendHoverText(stack, context, tooltip, flag);
                }
            }, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));

    //Contracts and Associated Items

    public static final DeferredItem<Item> CONTRACT_BLANK = ITEMS.registerItem("contract_blank",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> CONTRACT_WRITTEN = ITEMS.registerItem("contract_written",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> CONTRACT_SIGNED = ITEMS.registerItem("contract_signed",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> CONTRACT_GUARD =
            ITEMS.registerItem("contract_guard", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.contract_guard").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties().rarity(Rarity.UNCOMMON));


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

    public static final DeferredItem<Item> DWARF_GUARD_SPAWN_EGG = ITEMS.registerItem("dwarf_guard_spawn_egg",
            (properties) -> new SpawnEggItem(JolCraftEntities.DWARF_GUARD.get(), properties));

    public static final DeferredItem<Item> DWARF_HISTORIAN_SPAWN_EGG = ITEMS.registerItem("dwarf_historian_spawn_egg",
            (properties) -> new SpawnEggItem(JolCraftEntities.DWARF_HISTORIAN.get(), properties));

    public static final DeferredItem<Item> DWARF_SCRAPPER_SPAWN_EGG = ITEMS.registerItem("dwarf_scrapper_spawn_egg",
            (properties) -> new SpawnEggItem(JolCraftEntities.DWARF_SCRAPPER.get(), properties));

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

    //Tomes
    public static final DeferredItem<Item> DWARVEN_TOME = ITEMS.registerSimpleItem("dwarven_tome");

    public static final DeferredItem<Item> DWARVEN_TOME_COMMON =
            ITEMS.registerItem("dwarven_tome_common", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
                    if (context.level() != null && context.level().isClientSide()) {
                        Player player = Minecraft.getInstance().player;
                        if (player != null &&
                                player.getCapability(JolCraftCapabilities.DWARVEN_LANGUAGE) != null &&
                                player.getCapability(JolCraftCapabilities.DWARVEN_LANGUAGE).knowsLanguage()) {

                            String loreKey = stack.get(JolCraftDataComponents.LORE_LINE_ID.get());
                            if (loreKey != null && !loreKey.isEmpty()) {
                                tooltip.add(Component.translatable(loreKey).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                            } else {
                                tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_tome.unlocked").withStyle(ChatFormatting.GRAY));
                            }
                        } else {
                            tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_tome.locked").withStyle(ChatFormatting.GRAY));
                        }
                    }

                    super.appendHoverText(stack, context, tooltip, flag);
                }
            }, new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));

    public static final DeferredItem<Item> DWARVEN_TOME_UNCOMMON =
            ITEMS.registerItem("dwarven_tome_uncommon", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
                    if (context.level() != null && context.level().isClientSide()) {
                        Player player = Minecraft.getInstance().player;
                        if (player != null &&
                                player.getCapability(JolCraftCapabilities.DWARVEN_LANGUAGE) != null &&
                                player.getCapability(JolCraftCapabilities.DWARVEN_LANGUAGE).knowsLanguage()) {

                            String loreKey = stack.get(JolCraftDataComponents.LORE_LINE_ID.get());
                            if (loreKey != null && !loreKey.isEmpty()) {
                                tooltip.add(Component.translatable(loreKey).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                            } else {
                                tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_tome.unlocked").withStyle(ChatFormatting.GRAY));
                            }
                        } else {
                            tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_tome.locked").withStyle(ChatFormatting.GRAY));
                        }
                    }

                    super.appendHoverText(stack, context, tooltip, flag);
                }
            }, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> DWARVEN_TOME_RARE =
            ITEMS.registerItem("dwarven_tome_rare", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
                    if (context.level() != null && context.level().isClientSide()) {
                        Player player = Minecraft.getInstance().player;
                        if (player != null &&
                                player.getCapability(JolCraftCapabilities.DWARVEN_LANGUAGE) != null &&
                                player.getCapability(JolCraftCapabilities.DWARVEN_LANGUAGE).knowsLanguage()) {

                            String loreKey = stack.get(JolCraftDataComponents.LORE_LINE_ID.get());
                            if (loreKey != null && !loreKey.isEmpty()) {
                                tooltip.add(Component.translatable(loreKey).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                            } else {
                                tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_tome.unlocked").withStyle(ChatFormatting.GRAY));
                            }
                        } else {
                            tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_tome.locked").withStyle(ChatFormatting.GRAY));
                        }
                    }

                    super.appendHoverText(stack, context, tooltip, flag);
                }
            }, new Item.Properties().stacksTo(1).rarity(Rarity.RARE));

    public static final DeferredItem<Item> DWARVEN_TOME_EPIC =
            ITEMS.registerItem("dwarven_tome_epic", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
                    if (context.level() != null && context.level().isClientSide()) {
                        Player player = Minecraft.getInstance().player;
                        if (player != null &&
                                player.getCapability(JolCraftCapabilities.DWARVEN_LANGUAGE) != null &&
                                player.getCapability(JolCraftCapabilities.DWARVEN_LANGUAGE).knowsLanguage()) {

                            String loreKey = stack.get(JolCraftDataComponents.LORE_LINE_ID.get());
                            if (loreKey != null && !loreKey.isEmpty()) {
                                tooltip.add(Component.translatable(loreKey).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                            } else {
                                tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_tome.unlocked").withStyle(ChatFormatting.GRAY));
                            }
                        } else {
                            tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_tome.locked").withStyle(ChatFormatting.GRAY));
                        }
                    }

                    super.appendHoverText(stack, context, tooltip, flag);
                }
            }, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

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
            }, new Item.Properties());

    public static final DeferredItem<Item> BROKEN_AMULET =
            ITEMS.registerItem("broken_amulet", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.salvage").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties());

    public static final DeferredItem<Item> BROKEN_BELT =
            ITEMS.registerItem("broken_belt", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.salvage").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties());

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
            }, new Item.Properties());

    public static final DeferredItem<Item> EXPIRED_POTION =
            ITEMS.registerItem("expired_potion", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.salvage").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties());

    public static final DeferredItem<Item> INGOT_MOULD =
            ITEMS.registerItem("ingot_mould", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.salvage").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties());

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
            }, new Item.Properties());


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
