package net.sievert.jolcraft.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.sievert.jolcraft.JolCraft;

import static net.sievert.jolcraft.JolCraft.locate;

public class JolCraftTags {

    public final class Items {
        public static final TagKey<Item> INK_AND_QUILLS = createTag("ink_and_quills");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, name));
        }
    }

    public interface Structures {
        public static final TagKey<Structure> ON_FORGE_EXPLORER_MAPS = create("on_forge_explorer_maps");

        private static TagKey<Structure> create(String name) {
            return TagKey.create(Registries.STRUCTURE, locate(name));
        }
    }

}
