package net.sievert.jolcraft.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class ToolItem extends Item {

    public ToolItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties properties) {
        super(material.applySwordProperties(properties, attackDamage, attackSpeed));
    }

    public ToolItem(Item.Properties properties) {
        super(properties);
    }
}
