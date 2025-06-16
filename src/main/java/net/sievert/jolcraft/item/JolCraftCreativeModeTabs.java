package net.sievert.jolcraft.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sievert.jolcraft.JolCraft;
import java.util.function.Supplier;

public class JolCraftCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, JolCraft.MOD_ID);

    public static final Supplier<CreativeModeTab> JOLCRAFT =
            CREATIVE_MODE_TABS.register("jolcraft_items_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.jolcraft.jolcraft_items_tab"))
                    .icon(() -> new ItemStack(JolCraftItems.GOLD_COIN.get()))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(JolCraftItems.DWARVEN_LEXICON);
                        pOutput.accept(JolCraftItems.GOLD_COIN);

                        pOutput.accept(JolCraftItems.QUILL_EMPTY);
                        pOutput.accept(JolCraftItems.QUILL_FULL);

                        pOutput.accept(JolCraftItems.CONTRACT_BLANK);
                        pOutput.accept(JolCraftItems.CONTRACT_WRITTEN);
                        pOutput.accept(JolCraftItems.CONTRACT_SIGNED);
                        pOutput.accept(JolCraftItems.CONTRACT_GUARD);

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
                        pOutput.accept(JolCraftItems.SKYBURROW);
                        pOutput.accept(JolCraftItems.SUNGLEAM);
                        pOutput.accept(JolCraftItems.VERDANITE);
                        pOutput.accept(JolCraftItems.WOECRYSTAL);

                    }).build());
/*
    public static final Supplier<CreativeModeTab> BLACK_OPAL_BLOCKS_TAB =
            CREATIVE_MODE_TABS.register("black_opal_blocks_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.mccourse.black_opal_blocks_tab"))
                    .icon(() -> new ItemStack(ModBlocks.BLACK_OPAL_BLOCK))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "black_opal_items_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.BLACK_OPAL_BLOCK);
                        pOutput.accept(ModBlocks.RAW_BLACK_OPAL_BLOCK);

                        pOutput.accept(ModBlocks.BLACK_OPAL_ORE);
                        pOutput.accept(ModBlocks.BLACK_OPAL_DEEPSLATE_ORE);
                        pOutput.accept(ModBlocks.BLACK_OPAL_END_ORE);
                        pOutput.accept(ModBlocks.BLACK_OPAL_NETHER_ORE);

                        pOutput.accept(ModBlocks.MAGIC_BLOCK);

                        pOutput.accept(ModBlocks.BLACK_OPAL_STAIRS);
                        pOutput.accept(ModBlocks.BLACK_OPAL_SLAB);

                        pOutput.accept(ModBlocks.BLACK_OPAL_PRESSURE_PLATE);
                        pOutput.accept(ModBlocks.BLACK_OPAL_BUTTON);

                        pOutput.accept(ModBlocks.BLACK_OPAL_FENCE);
                        pOutput.accept(ModBlocks.BLACK_OPAL_FENCE_GATE);
                        pOutput.accept(ModBlocks.BLACK_OPAL_WALL);

                        pOutput.accept(ModBlocks.BLACK_OPAL_DOOR);
                        pOutput.accept(ModBlocks.BLACK_OPAL_TRAPDOOR);

                    }).build());
*/

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
