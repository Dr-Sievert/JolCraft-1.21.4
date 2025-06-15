package net.sievert.jolcraft.item;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.capability.JolCraftCapabilities;
import net.sievert.jolcraft.entity.JolCraftEntities;
import net.minecraft.network.chat.Component;
import net.sievert.jolcraft.item.custom.DwarvenLexiconItem;
import net.sievert.jolcraft.item.custom.QuillItem;

import java.util.List;

public class JolCraftItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(JolCraft.MOD_ID);



    //Core Items

    // public static final DeferredItem<Item> GOLD_COIN = ITEMS.registerSimpleItem("gold_coin");

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

                            tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_lexicon.unlocked"));
                        } else {
                            tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_lexicon.locked"));
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
                    pTooltipComponents.add(Component.translatable("tooltip.jolcraft.contract_guard"));
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




    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
