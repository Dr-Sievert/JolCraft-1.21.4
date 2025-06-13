package net.sievert.jolcraft.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.structure.Structure;

import static net.sievert.jolcraft.JolCraft.locate;

public final class JolCraftTags {
    public interface Items {
        private static TagKey<Item> create(String name) {
            return TagKey.create(Registries.ITEM, locate(name));
        }
    }

    public interface Structures {
        public static final TagKey<Structure> ON_FORGE_EXPLORER_MAPS = create("on_forge_explorer_maps");

        private static TagKey<Structure> create(String name) {
            return TagKey.create(Registries.STRUCTURE, locate(name));
        }
    }
}