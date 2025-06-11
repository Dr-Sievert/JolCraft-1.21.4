package net.sievert.jolcraft.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.JolCraftEntities;

public class JolCraftItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(JolCraft.MOD_ID);

    //Simple Items
    public static final DeferredItem<Item> GOLD_COIN = ITEMS.registerSimpleItem("gold_coin");

    public static final DeferredItem<Item> CONTRACT_BLANK = ITEMS.registerItem("contract_blank",
            Item::new, new Item.Properties());

    //Eggs

    public static final DeferredItem<Item> DWARF_SPAWN_EGG = ITEMS.registerItem("dwarf_spawn_egg",
            (properties) -> new SpawnEggItem(JolCraftEntities.DWARF.get(), properties));

    public static final DeferredItem<Item> DWARF_GUARD_SPAWN_EGG = ITEMS.registerItem("dwarf_guard_spawn_egg",
            (properties) -> new SpawnEggItem(JolCraftEntities.DWARF_GUARD.get(), properties));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
