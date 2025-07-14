package net.sievert.jolcraft.item.armor;

import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.sound.JolCraftSounds;
import net.sievert.jolcraft.util.JolCraftTags;

import java.util.EnumMap;

public class JolCraftArmorMaterials {

    public static final ResourceKey<EquipmentAsset> DEEPSLATE_KEY =
            ResourceKey.create(EquipmentAssets.ROOT_ID, ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "deepslate"));

    public static final ArmorMaterial DEEPSLATE_ARMOR_MATERIAL = new ArmorMaterial(
            24,
            Util.make(new EnumMap<>(ArmorType.class), attribute -> {
                attribute.put(ArmorType.BOOTS, 2);
                attribute.put(ArmorType.LEGGINGS, 5);
                attribute.put(ArmorType.CHESTPLATE, 6);
                attribute.put(ArmorType.HELMET, 2);
                attribute.put(ArmorType.BODY, 5);
            }),
            10,
            JolCraftSounds.ARMOR_EQUIP_DEEPSLATE,
            1.0f,
            0.1f,
            JolCraftTags.Items.REPAIRS_DEEPSLATE,
            DEEPSLATE_KEY
    );




}
