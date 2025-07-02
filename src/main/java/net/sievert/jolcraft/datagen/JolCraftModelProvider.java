package net.sievert.jolcraft.datagen;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.color.item.Dye;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.select.TrimMaterialProperty;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.block.JolCraftBlocks;
import net.sievert.jolcraft.block.custom.BarleyCropBlock;
import net.sievert.jolcraft.block.custom.HopsCropBottomBlock;
import net.sievert.jolcraft.block.custom.HopsCropTopBlock;
import net.sievert.jolcraft.item.JolCraftEquipmentAssets;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.item.JolCraftTrimMaterials;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JolCraftModelProvider extends ModelProvider {

    private final PackOutput packOutput;
    public JolCraftModelProvider(PackOutput output) {
        super(output, JolCraft.MOD_ID);
        this.packOutput = output;

    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        //Core
        itemModels.generateFlatItem(JolCraftItems.GOLD_COIN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DWARVEN_LEXICON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.PARCHMENT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DEEPSLATE_PLATE.get(), ModelTemplates.FLAT_ITEM);

        //Weapons and Tools
        itemModels.generateFlatItem(JolCraftItems.DEEPSLATE_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DEEPSLATE_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DEEPSLATE_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DEEPSLATE_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(JolCraftItems.DEEPSLATE_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        //Custom Armor and Trim Materials/Templates
        List<ItemModelGenerators.TrimMaterialData> allTrimMaterials = new ArrayList<>();
        for (Map.Entry<String, ResourceKey<TrimMaterial>> entry : VANILLA_TRIMS.entrySet()) {
            allTrimMaterials.add(new ItemModelGenerators.TrimMaterialData(entry.getKey(), entry.getValue(), Map.of()));
        }
        allTrimMaterials.addAll(JOLCRAFT_TRIMS); // <--- just this line for ALL your custom trims

        generateTrimmableArmorSetWithCustom(itemModels, "deepslate", JolCraftEquipmentAssets.DEEPSLATE_KEY, false);

        generateArmorWithTrim(itemModels, "leather", EquipmentAssets.LEATHER, true);
        generateArmorWithTrim(itemModels, "chainmail", EquipmentAssets.CHAINMAIL, false);
        generateArmorWithTrim(itemModels, "iron", EquipmentAssets.IRON, false);
        generateArmorWithTrim(itemModels, "golden", EquipmentAssets.GOLD, false);
        generateArmorWithTrim(itemModels, "diamond", EquipmentAssets.DIAMOND,false);
        generateArmorWithTrim(itemModels, "netherite", EquipmentAssets.NETHERITE, false);

        itemModels.generateFlatItem(JolCraftItems.FORGE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);

        //Alchemy
        itemModels.generateFlatItem(JolCraftItems.INVERIX.get(), ModelTemplates.FLAT_ITEM);

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

        //Other Crops
        itemModels.generateFlatItem(JolCraftItems.DEEPSLATE_BULBS.get(), ModelTemplates.FLAT_ITEM);

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

        blockModels.blockStateOutput.accept(new BlockStateGenerator() {
            @Override
            public JsonObject get() {
                JsonObject root = new JsonObject();
                JsonObject variants = new JsonObject();
                for (int age = 0; age <= 9; age++) {
                    variants.add("age=" + age, modelObj(JolCraft.MOD_ID, "block/deepslate_bulbs_crop_stage" + age));
                }
                root.add("variants", variants);
                return root;
            }

            @Override
            public Block getBlock() {
                return JolCraftBlocks.DEEPSLATE_BULBS_CROP.get();
            }
        });


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

    //Custom Helpers!!

    private void generateTrimmableItemWithCustomList(
            ItemModelGenerators itemModels,
            String baseName,
            ResourceKey<EquipmentAsset> key,
            boolean dyeable,
            List<ItemModelGenerators.TrimMaterialData> trimMaterialList) {

        for (String type : ARMOR_TYPES) {
            String fileName = baseName + "_" + type;

            ResourceLocation baseModelLocation = ResourceLocation.fromNamespaceAndPath(key.location().getNamespace(), "item/" + fileName);
            ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath(key.location().getNamespace(), "item/" + fileName);
            ResourceLocation overlayTexture = ResourceLocation.fromNamespaceAndPath(key.location().getNamespace(), "item/" + fileName + "_overlay");

            List<SelectItemModel.SwitchCase<ResourceKey<TrimMaterial>>> list = new ArrayList<>(trimMaterialList.size());

            for (ItemModelGenerators.TrimMaterialData data : trimMaterialList) {
                boolean isCustom = data.materialKey().location().getNamespace().equals("jolcraft");
                ResourceLocation trimModelLoc = baseModelLocation.withSuffix("_" + data.name() + "_trim");

                // Only change this line:
                String trimTextureName = data.name();
                if (baseName.equals(data.name())) {
                    trimTextureName += "_darker";
                }
                ResourceLocation trimTextureLocation = ResourceLocation.fromNamespaceAndPath("minecraft", "trims/items/" + type + "_trim_" + trimTextureName);

                ItemModel.Unbaked bakedModel;
                if (dyeable) {
                    itemModels.generateLayeredItem(
                            trimModelLoc,
                            textureLocation,
                            overlayTexture,
                            trimTextureLocation  // layer1 = correct namespace!
                    );
                    bakedModel = ItemModelUtils.tintedModel(trimModelLoc, new Dye(-6265536));
                } else {
                    itemModels.generateLayeredItem(
                            trimModelLoc,
                            textureLocation,
                            trimTextureLocation  // layer1 = correct namespace!
                    );
                    bakedModel = ItemModelUtils.plainModel(trimModelLoc);
                }
                list.add(ItemModelUtils.when(data.materialKey(), bakedModel));
            }


            // Default (fallback) model
            ItemModel.Unbaked defaultModel;
            if (dyeable) {
                ModelTemplates.TWO_LAYERED_ITEM.create(baseModelLocation, TextureMapping.layered(textureLocation, overlayTexture), itemModels.modelOutput);
                defaultModel = ItemModelUtils.tintedModel(baseModelLocation, new Dye(-6265536)); // Example color
            } else {
                ModelTemplates.FLAT_ITEM.create(baseModelLocation, TextureMapping.layer0(textureLocation), itemModels.modelOutput);
                defaultModel = ItemModelUtils.plainModel(baseModelLocation);
            }

            // Register select model for this armor piece
            Item armorItem = getItemFromBaseName(baseName, type);
            itemModels.itemModelOutput.accept(
                    armorItem,
                    ItemModelUtils.select(new TrimMaterialProperty(), defaultModel, list)
            );
        }
    }

    public void generateTrimmableArmorSetWithCustom(
            ItemModelGenerators itemModels,
            String baseName,
            ResourceKey<EquipmentAsset> key,
            boolean dyeable
    ) {
        List<ItemModelGenerators.TrimMaterialData> allTrims = new ArrayList<>(ItemModelGenerators.TRIM_MATERIAL_MODELS);
        allTrims.addAll(JOLCRAFT_TRIMS);
        generateTrimmableItemWithCustomList(itemModels, baseName, key, dyeable, allTrims);
    }


    private void generateArmorWithTrim(
            ItemModelGenerators itemModels,
            String baseName,
            ResourceKey<EquipmentAsset> key,
            boolean dyeable) {

        // Gather all possible trims (vanilla + all custom)
        List<ItemModelGenerators.TrimMaterialData> allTrimMaterials = new ArrayList<>();

        // Add all vanilla trims
        for (Map.Entry<String, ResourceKey<TrimMaterial>> entry : VANILLA_TRIMS.entrySet()) {
            allTrimMaterials.add(new ItemModelGenerators.TrimMaterialData(entry.getKey(), entry.getValue(), Map.of()));
        }
        // Add ALL custom trims from static list
        allTrimMaterials.addAll(JOLCRAFT_TRIMS);

        for (String type : ARMOR_TYPES) {
            String fileName = baseName + "_" + type;

            List<SelectItemModel.SwitchCase<ResourceKey<TrimMaterial>>> selectCases = new ArrayList<>();

            for (ItemModelGenerators.TrimMaterialData trim : allTrimMaterials) {
                boolean isCustom = trim.materialKey().location().getNamespace().equals("jolcraft");
                boolean isVanillaArmor = baseName.equals("diamond") || baseName.equals("netherite") || baseName.equals("leather")
                        || baseName.equals("iron") || baseName.equals("golden") || baseName.equals("chainmail");
                String trimName = trim.name();

                ResourceLocation caseModelLoc;

                // Handle non-vanilla armor separately to avoid double suffixing
                if (!isVanillaArmor && isCustom) {
                    // Only add one _trim suffix for custom, non-vanilla armor
                    caseModelLoc = ResourceLocation.fromNamespaceAndPath("jolcraft", "item/" + fileName + "_" + trimName + "_trim");

                    ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("jolcraft", "item/" + fileName);
                    ResourceLocation overlay = ResourceLocation.fromNamespaceAndPath("jolcraft", "item/" + fileName + "_overlay");
                    ResourceLocation trimTexture = ResourceLocation.fromNamespaceAndPath("jolcraft", "trims/items/" + type + "_trim_" + trimName);

                    addTrimModelToList(
                            itemModels,
                            caseModelLoc,
                            texture,
                            overlay,
                            trim,
                            trimTexture,
                            selectCases,
                            dyeable
                    );
                } else if (isCustom) {
                    // Vanilla armor: Jolcraft custom trims still get one _trim
                    caseModelLoc = ResourceLocation.fromNamespaceAndPath("jolcraft", "item/" + fileName);

                    ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("minecraft", "item/" + fileName);
                    ResourceLocation overlay = ResourceLocation.fromNamespaceAndPath("minecraft", "item/" + fileName + "_overlay");
                    ResourceLocation trimTexture = ResourceLocation.fromNamespaceAndPath("minecraft", "trims/items/" + type + "_trim_" + trimName);

                    addTrimModelToList(
                            itemModels,
                            caseModelLoc,
                            texture,
                            overlay,
                            trim,
                            trimTexture,
                            selectCases,
                            dyeable
                    );
                } else {
                    // Vanilla trims (any armor): always reference vanilla
                    caseModelLoc = ResourceLocation.fromNamespaceAndPath("minecraft", "item/" + fileName + "_" + trimName + "_trim");
                    ItemModel.Unbaked dummyModel = ItemModelUtils.plainModel(caseModelLoc);
                    selectCases.add(ItemModelUtils.when(trim.materialKey(), dummyModel));
                }
            }

            // Fallback logic
            ResourceLocation fallbackModelLoc = (baseName.equals("diamond") || baseName.equals("netherite") || baseName.equals("leather")
                    || baseName.equals("iron") || baseName.equals("golden") || baseName.equals("chainmail"))
                    ? ResourceLocation.fromNamespaceAndPath("minecraft", "item/" + fileName)
                    : ResourceLocation.fromNamespaceAndPath("jolcraft", "item/" + fileName);

            ItemModel.Unbaked fallbackModel = dyeable
                    ? ItemModelUtils.tintedModel(fallbackModelLoc, new Dye(-6265536))
                    : ItemModelUtils.plainModel(fallbackModelLoc);

            Item armorItem = getItemFromBaseName(baseName, type);
            itemModels.itemModelOutput.accept(
                    armorItem,
                    ItemModelUtils.select(new TrimMaterialProperty(), fallbackModel, selectCases)
            );
        }
    }


    // Helper method to add the trim model to the list (common for custom trims only)
    private void addTrimModelToList(
            ItemModelGenerators itemModels,
            ResourceLocation baseModelLocation,
            ResourceLocation textureLocation,
            ResourceLocation overlayTexture,
            ItemModelGenerators.TrimMaterialData trim,
            ResourceLocation trimTextureLocation,
            List<SelectItemModel.SwitchCase<ResourceKey<TrimMaterial>>> list,
            boolean dyeable) {

        ItemModel.Unbaked bakedModel;
        if (dyeable) {
            // Generate the item model with overlay and trim texture for dyeable items
            itemModels.generateLayeredItem(baseModelLocation.withSuffix("_" + trim.name() + "_trim"), textureLocation, overlayTexture, trimTextureLocation);
            bakedModel = ItemModelUtils.tintedModel(baseModelLocation.withSuffix("_" + trim.name() + "_trim"), new Dye(-6265536)); // Example color
        } else {
            // Generate the model with the trim texture for non-dyeable items
            itemModels.generateLayeredItem(baseModelLocation.withSuffix("_" + trim.name() + "_trim"), textureLocation, trimTextureLocation);
            bakedModel = ItemModelUtils.plainModel(baseModelLocation.withSuffix("_" + trim.name() + "_trim"));
        }

        list.add(ItemModelUtils.when(trim.materialKey(), bakedModel));
    }

    // Helper method with custom trim list
    private static final String[] ARMOR_TYPES = {"helmet", "chestplate", "leggings", "boots"};

    private static final List<ItemModelGenerators.TrimMaterialData> JOLCRAFT_TRIMS = List.of(
            new ItemModelGenerators.TrimMaterialData(
                    "deepslate",
                    JolCraftTrimMaterials.DEEPSLATE,
                    Map.of(JolCraftEquipmentAssets.DEEPSLATE_KEY, "deepslate_darker")
            )
            // Add more trims as you implement them!
    );

    // At class level, or as a static final, define:
    private static final Map<String, ResourceKey<TrimMaterial>> VANILLA_TRIMS = Map.of(
            "quartz",   TrimMaterials.QUARTZ,
            "iron",     TrimMaterials.IRON,
            "netherite",TrimMaterials.NETHERITE,
            "redstone", TrimMaterials.REDSTONE,
            "copper",   TrimMaterials.COPPER,
            "gold",     TrimMaterials.GOLD,
            "emerald",  TrimMaterials.EMERALD,
            "diamond",  TrimMaterials.DIAMOND,
            "lapis",    TrimMaterials.LAPIS,
            "amethyst", TrimMaterials.AMETHYST
    );

    private Item getItemFromBaseName(String baseName, String type) {
        // Dynamically construct the correct item name based on baseName and type
        String itemName = baseName + "_" + type;

        // Try to fetch from jolcraft first
        ResourceLocation jolcraftLocation = ResourceLocation.fromNamespaceAndPath("jolcraft", itemName);
        Optional<Item> itemOptional = BuiltInRegistries.ITEM.getOptional(jolcraftLocation);

        // If the item wasn't found in jolcraft, fall back to the minecraft namespace
        if (!itemOptional.isPresent()) {
            ResourceLocation minecraftLocation = ResourceLocation.fromNamespaceAndPath("minecraft", itemName);
            itemOptional = BuiltInRegistries.ITEM.getOptional(minecraftLocation);
        }

        // If the item was not found in either namespace, throw an exception
        return itemOptional.orElseThrow(() -> new IllegalStateException("Item not found: " + itemName));
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

    // Helper for the model property
    private static JsonObject modelObj(String modid, String path) {
        JsonObject obj = new JsonObject();
        obj.addProperty("model", modid + ":" + path);
        return obj;
    }


}
