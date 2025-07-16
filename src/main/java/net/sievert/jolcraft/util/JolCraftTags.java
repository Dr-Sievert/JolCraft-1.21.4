package net.sievert.jolcraft.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;
import net.sievert.jolcraft.JolCraft;

import static net.sievert.jolcraft.JolCraft.locate;

public class JolCraftTags {

    public final class Items {

        //Core

        public static final TagKey<Item> SPAWN_EGGS = createTag("spawn_eggs");

        public static final TagKey<Item> INK_AND_QUILLS = createTag("ink_and_quills");

        public static final TagKey<Item> GEMS = createTag("gems");

        public static final TagKey<Item> SIGNED_CONTRACTS = createTag("signed_contracts");

        public static final TagKey<Item> REPUTATION_TABLETS = createTag("reputation_tablets");

        public static final TagKey<Item> HOPS = createTag("hops");

        public static final TagKey<Item> REPAIRS_DEEPSLATE = createTag("repairs_deepslate");

        public static final TagKey<Item> REPAIRS_MITHRIL = createTag("repairs_mithril");

        public static final TagKey<Item> MITHRIL_ITEMS = createTag("mithril_items");

        public static final TagKey<Item> LEGENDARY_ITEMS = createTag("legendary_items");

        //Scrap
        public static final TagKey<Item> GLOBAL_SCRAP = createTag("global_scrap");
        public static final TagKey<Item> GENERAL_SCRAP = createTag("general_scrap");
        public static final TagKey<Item> TEXTILE_SCRAP = createTag("textile_scrap");
        public static final TagKey<Item> REDSTONE_SCRAP = createTag("redstone_scrap");
        public static final TagKey<Item> IRON_SCRAP = createTag("iron_scrap");
        public static final TagKey<Item> GOLD_SCRAP = createTag("gold_scrap");
        public static final TagKey<Item> MITHRIL_SCRAP = createTag("mithril_scrap");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, name));
        }
    }

    public final class Blocks {

        public static final TagKey<Block> NEEDS_NETHERITE_TOOL = createTag("needs_netherite_tool");

        public static final TagKey<Block> DEEPSLATE_BULBS_PLANTABLE = createTag("deepslate_bulbs_plantable");

        public static final TagKey<Block> HOPS_BOTTOM = createTag("hops_bottom");

        public static final TagKey<Block> HOPS_TOP = createTag("hops_top");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, name));
        }

    }

    public interface Structures {
        public static final TagKey<Structure> ON_FORGE_EXPLORER_MAPS = create("on_forge_explorer_maps");

        private static TagKey<Structure> create(String name) {
            return TagKey.create(Registries.STRUCTURE, locate(name));
        }
    }

}
