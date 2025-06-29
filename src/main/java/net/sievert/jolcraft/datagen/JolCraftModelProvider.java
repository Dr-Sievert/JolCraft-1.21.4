package net.sievert.jolcraft.datagen;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.block.JolCraftBlocks;
import net.sievert.jolcraft.block.custom.BarleyCropBlock;
import net.sievert.jolcraft.block.custom.HopsCropBottomBlock;
import net.sievert.jolcraft.block.custom.HopsCropTopBlock;
import net.sievert.jolcraft.item.JolCraftItems;

import java.nio.file.Path;

public class JolCraftModelProvider extends ModelProvider {

    private final PackOutput packOutput;
    public JolCraftModelProvider(PackOutput output) {
        super(output, JolCraft.MOD_ID);
        this.packOutput = output; // <-- save it

    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        //Core
        itemModels.generateFlatItem(JolCraftItems.GOLD_COIN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DWARVEN_LEXICON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.PARCHMENT.get(), ModelTemplates.FLAT_ITEM);

        //Animal-related
        itemModels.generateFlatItem(JolCraftItems.MUFFHORN_MILK_BUCKET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.MUFFHORN_FUR.get(), ModelTemplates.FLAT_ITEM);
        blockModels.createTrivialCube(JolCraftBlocks.MUFFHORN_FUR_BLOCK.get());

        //Brewing
        itemModels.generateFlatItem(JolCraftItems.BARLEY.get(), ModelTemplates.FLAT_ITEM);
        blockModels.createRotatedPillarWithHorizontalVariant(
                JolCraftBlocks.BARLEY_BLOCK.get(),
                TexturedModel.COLUMN,
                TexturedModel.COLUMN_HORIZONTAL
        );
        itemModels.generateFlatItem(JolCraftItems.BARLEY_MALT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.ASGARNIAN_HOPS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DUSKHOLD_HOPS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.KRANDONIAN_HOPS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.YANILLIAN_HOPS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.YEAST.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.GLASS_MUG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DWARVEN_BREW.get(), ModelTemplates.FLAT_ITEM);

        //Bounty
        itemModels.generateFlatItem(JolCraftItems.BOUNTY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.BOUNTY_CRATE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.RESTOCK_CRATE.get(), ModelTemplates.FLAT_ITEM);

        //Contracts and related
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_BLANK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_WRITTEN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_SIGNED.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.GUILD_SIGIL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_GUILDMASTER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_MERCHANT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_HISTORIAN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_SCRAPPER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_GUARD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_BREWMASTER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_KEEPER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_MINER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_EXPLORER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_ALCHEMIST.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_ARCANIST.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_PRIEST.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_ARTISAN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_CHAMPION.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_BLACKSMITH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.CONTRACT_SMELTER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.QUILL_EMPTY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.QUILL_SMALL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.QUILL_HALF.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.QUILL_FULL.get(), ModelTemplates.FLAT_ITEM);


        //Gems
        itemModels.generateFlatItem(JolCraftItems.AEGISCORE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.ASHFANG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DEEPMARROW.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.EARTHBLOOD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.EMBERGLASS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.FROSTVEIN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.GRIMSTONE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.IRONHEART.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.LUMIERE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.MOONSHARD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.RUSTAGATE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.SKYBURROW.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.SUNGLEAM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.VERDANITE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.WOECRYSTAL.get(), ModelTemplates.FLAT_ITEM);

        //Reputation
        itemModels.generateFlatItem(JolCraftItems.REPUTATION_TABLET_0.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.REPUTATION_TABLET_1.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.REPUTATION_TABLET_2.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.REPUTATION_TABLET_3.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.REPUTATION_TABLET_4.get(), ModelTemplates.FLAT_ITEM);


        //Tomes
        itemModels.generateFlatItem(JolCraftItems.DWARVEN_TOME.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DWARVEN_TOME_COMMON.get(), JolCraftItems.DWARVEN_TOME.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DWARVEN_TOME_UNCOMMON.get(), JolCraftItems.DWARVEN_TOME.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DWARVEN_TOME_RARE.get(), JolCraftItems.DWARVEN_TOME.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DWARVEN_TOME_EPIC.get(), JolCraftItems.DWARVEN_TOME.get(), ModelTemplates.FLAT_ITEM);


        //Tools and weapons
        itemModels.generateFlatItem(JolCraftItems.COPPER_SPANNER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(JolCraftItems.IRON_SPANNER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);


        //Scrap
        itemModels.generateFlatItem(JolCraftItems.SCRAP.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.SCRAP_HEAP.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.BROKEN_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(JolCraftItems.BROKEN_AMULET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.BROKEN_BELT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.BROKEN_COINS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DEEPSLATE_MUG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.EXPIRED_POTION.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.INGOT_MOULD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.MITHRIL_SALVAGE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.OLD_FABRIC.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.RUSTY_TONGS.get(), ModelTemplates.FLAT_ITEM);

        //Eggs
        itemModels.generateSpawnEgg(
                JolCraftItems.DWARF_SPAWN_EGG.get(),
                -4355214,
                -11125709
        );
        itemModels.generateSpawnEgg(
                JolCraftItems.DWARF_GUILDMASTER_SPAWN_EGG.get(),
                -4355214,
                -11716526
        );
        itemModels.generateSpawnEgg(
                JolCraftItems.DWARF_HISTORIAN_SPAWN_EGG.get(),
                -4355214,
                -8807323
        );
        itemModels.generateSpawnEgg(
                JolCraftItems.DWARF_MERCHANT_SPAWN_EGG.get(),
                -4355214,
                -1399760
        );
        itemModels.generateSpawnEgg(
                JolCraftItems.DWARF_SCRAPPER_SPAWN_EGG.get(),
                -4355214,
                -3380960
        );

        itemModels.generateSpawnEgg(
                JolCraftItems.DWARF_BREWMASTER_SPAWN_EGG.get(),
                -4355214,
                -396380
        );
        itemModels.generateSpawnEgg(
                JolCraftItems.DWARF_GUARD_SPAWN_EGG.get(),
                -4355214,
                -2233622
        );
        itemModels.generateSpawnEgg(
                JolCraftItems.DWARF_KEEPER_SPAWN_EGG.get(),
                -4355214,
                -7799040
        );

        itemModels.generateFlatItem(JolCraftItems.MUFFHORN_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);


        //Blocks

        //Crops
        blockModels.createCropBlock(JolCraftBlocks.BARLEY_CROP.get(), BarleyCropBlock.AGE,  0, 1, 2, 3, 4, 5, 6, 7);

        //Hops
        createTopCropBlock(
                blockModels,
                JolCraftBlocks.ASGARNIAN_CROP_TOP.get(),
                HopsCropTopBlock.TOP_AGE, // <-- use your top crop's static property
                0, 1, 2, 3, 4 // <-- as many stages as you defined models/textures for
        );

        blockModels.createCropBlock(JolCraftBlocks.ASGARNIAN_CROP_BOTTOM.get(), HopsCropBottomBlock.AGE, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        createTopCropBlock(
                blockModels,
                JolCraftBlocks.DUSKHOLD_CROP_TOP.get(),
                HopsCropTopBlock.TOP_AGE, // <-- use your top crop's static property
                0, 1, 2, 3, 4 // <-- as many stages as you defined models/textures for
        );

        blockModels.createCropBlock(JolCraftBlocks.DUSKHOLD_CROP_BOTTOM.get(), HopsCropBottomBlock.AGE, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        createTopCropBlock(
                blockModels,
                JolCraftBlocks.KRANDONIAN_CROP_TOP.get(),
                HopsCropTopBlock.TOP_AGE, // <-- use your top crop's static property
                0, 1, 2, 3, 4 // <-- as many stages as you defined models/textures for
        );

        blockModels.createCropBlock(JolCraftBlocks.KRANDONIAN_CROP_BOTTOM.get(), HopsCropBottomBlock.AGE, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        createTopCropBlock(
                blockModels,
                JolCraftBlocks.YANILLIAN_CROP_TOP.get(),
                HopsCropTopBlock.TOP_AGE, // <-- use your top crop's static property
                0, 1, 2, 3, 4 // <-- as many stages as you defined models/textures for
        );

        blockModels.createCropBlock(JolCraftBlocks.YANILLIAN_CROP_BOTTOM.get(), HopsCropBottomBlock.AGE, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);


        // For Fermenting Cauldron: custom blockstate with level property
        blockModels.blockStateOutput.accept(new BlockStateGenerator() {
            @Override
            public JsonObject get() {
                JsonObject root = new JsonObject();
                JsonObject variants = new JsonObject();
                variants.add("level=1", modelObj(JolCraft.MOD_ID, "block/fermenting_cauldron_level1"));
                variants.add("level=2", modelObj(JolCraft.MOD_ID, "block/fermenting_cauldron_level2"));
                variants.add("level=3", modelObj(JolCraft.MOD_ID, "block/fermenting_cauldron_full"));
                root.add("variants", variants);
                return root;
            }
            @Override
            public Block getBlock() {
                return JolCraftBlocks.FERMENTING_CAULDRON.get();
            }
        });


    }

    //Hops top helper
    private void createTopCropBlock(BlockModelGenerators blockModels, Block block, IntegerProperty ageProperty, int... ageToVisualStageMapping) {
        if (ageProperty.getPossibleValues().size() != ageToVisualStageMapping.length) {
            throw new IllegalArgumentException("Mismatch between age property values and visual stage mapping!");
        }

        Int2ObjectMap<ResourceLocation> visualStageModels = new Int2ObjectOpenHashMap<>();

        PropertyDispatch dispatch = PropertyDispatch.property(ageProperty).generate(ageValue -> {
            int visualStage = ageToVisualStageMapping[ageValue];
            ResourceLocation modelId = visualStageModels.computeIfAbsent(
                    visualStage,
                    i -> blockModels.createSuffixedVariant(block, "_stage" + i, ModelTemplates.CROP, TextureMapping::crop)
            );
            return Variant.variant().with(VariantProperties.MODEL, modelId);
        });

        blockModels.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(dispatch));
    }

    // --- RAW MODEL JSON SAVER ---
    private void saveModelJson(PackOutput output, ResourceLocation modelLoc, JsonObject json) {
        Path path = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve("assets")
                .resolve(modelLoc.getNamespace())
                .resolve("models")
                .resolve(modelLoc.getPath() + ".json");
        try {
            DataProvider.saveStable(null, json, path);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save model " + modelLoc, e);
        }
    }




    // Helper for the model property
    private static JsonObject modelObj(String modid, String path) {
        JsonObject obj = new JsonObject();
        obj.addProperty("model", modid + ":" + path);
        return obj;
    }


}
