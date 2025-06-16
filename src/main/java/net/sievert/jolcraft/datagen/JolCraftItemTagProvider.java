package net.sievert.jolcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.data.JolCraftTags;
import net.sievert.jolcraft.item.JolCraftItems;

import java.util.concurrent.CompletableFuture;

public class JolCraftItemTagProvider extends ItemTagsProvider {
    public JolCraftItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                  CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags, JolCraft.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(JolCraftTags.Items.INK_AND_QUILLS)
                .add(JolCraftItems.QUILL_FULL.get())
                .add(JolCraftItems.QUILL_HALF.get())
                .add(JolCraftItems.QUILL_SMALL.get());
    }


}