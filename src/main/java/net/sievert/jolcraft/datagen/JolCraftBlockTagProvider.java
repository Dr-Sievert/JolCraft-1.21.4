package net.sievert.jolcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.block.JolCraftBlocks;
import net.sievert.jolcraft.util.JolCraftTags;

import java.util.concurrent.CompletableFuture;

public class JolCraftBlockTagProvider extends BlockTagsProvider {
    public JolCraftBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, JolCraft.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(JolCraftBlocks.DEEPSLATE_MITHRIL_ORE.get())
                .add(JolCraftBlocks.PURE_MITHRIL_BLOCK.get())
                .add(JolCraftBlocks.MITHRIL_BLOCK.get())
                .add(JolCraftBlocks.STRONGBOX.get())
                .add(JolCraftBlocks.DEEPSLATE_BULBS_CROP.get())
                .add(JolCraftBlocks.HEARTH.get())
                .add(JolCraftBlocks.FERMENTING_CAULDRON.get());

        tag(JolCraftTags.Blocks.NEEDS_NETHERITE_TOOL)
                .add(JolCraftBlocks.DEEPSLATE_MITHRIL_ORE.get())
                .add(JolCraftBlocks.PURE_MITHRIL_BLOCK.get())
                .add(JolCraftBlocks.MITHRIL_BLOCK.get());

        tag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)
                .addTags(JolCraftTags.Blocks.NEEDS_NETHERITE_TOOL);


        tag(BlockTags.MINEABLE_WITH_HOE)
                .add(JolCraftBlocks.BARLEY_BLOCK.get());


        tag(BlockTags.MUSHROOM_GROW_BLOCK)
                .add(JolCraftBlocks.VERDANT_SOIL.get());

        tag(JolCraftTags.Blocks.DEEPSLATE_BULBS_PLANTABLE)
                .add(JolCraftBlocks.VERDANT_SOIL.get())
                .add(Blocks.DEEPSLATE)
                .add(Blocks.POLISHED_DEEPSLATE);

        tag(JolCraftTags.Blocks.HOPS_BOTTOM)
                .add(JolCraftBlocks.ASGARNIAN_CROP_BOTTOM.get())
                .add(JolCraftBlocks.DUSKHOLD_CROP_BOTTOM.get())
                .add(JolCraftBlocks.KRANDONIAN_CROP_BOTTOM.get())
                .add(JolCraftBlocks.YANILLIAN_CROP_BOTTOM.get());

        tag(JolCraftTags.Blocks.HOPS_TOP)
                .add(JolCraftBlocks.ASGARNIAN_CROP_TOP.get())
                .add(JolCraftBlocks.DUSKHOLD_CROP_TOP.get())
                .add(JolCraftBlocks.KRANDONIAN_CROP_TOP.get())
                .add(JolCraftBlocks.YANILLIAN_CROP_TOP.get());
    }

}