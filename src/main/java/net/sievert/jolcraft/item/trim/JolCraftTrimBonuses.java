package net.sievert.jolcraft.item.trim;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.entity.EquipmentSlotGroup;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JolCraftTrimBonuses {

    private static final Map<String, List<TrimAttributeBonus>> TRIM_BONUSES = new HashMap<>();

    static {

        //Possible operations are ADD_VALUE (simple add +1) , ADD_MULTIPLIED_BASE (add multiplied with base for example 0.1 of base hp 20 is +2),
        // ADD_MULTIPLIED_TOTAL same as previous but it multiplies not with base but with current total (Multiplicatively)

        TRIM_BONUSES.put("aegiscore", List.of(
                bonus(Attributes.ARMOR_TOUGHNESS, 0.5, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.HEAD),
                bonus(Attributes.ARMOR_TOUGHNESS, 0.5, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.CHEST),
                bonus(Attributes.ARMOR_TOUGHNESS, 0.5, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.LEGS),
                bonus(Attributes.ARMOR_TOUGHNESS, 0.5, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.FEET)
        ));

        TRIM_BONUSES.put("ashfang", List.of(
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.HEAD),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.CHEST),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.LEGS),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.FEET)
        ));

        TRIM_BONUSES.put("deepmarrow", List.of(
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.HEAD),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.CHEST),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.LEGS),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.FEET)
        ));

        TRIM_BONUSES.put("earthblood", List.of(
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.HEAD),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.CHEST),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.LEGS),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.FEET)
        ));

        TRIM_BONUSES.put("emberglass", List.of(
                bonus(Attributes.MAX_HEALTH, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.HEAD),
                bonus(Attributes.MAX_HEALTH, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.CHEST),
                bonus(Attributes.MAX_HEALTH, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.LEGS),
                bonus(Attributes.MAX_HEALTH, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.FEET)
        ));

        TRIM_BONUSES.put("frostvein", List.of(
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.HEAD),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.CHEST),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.LEGS),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.FEET)
        ));

        TRIM_BONUSES.put("grimstone", List.of(
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.HEAD),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.CHEST),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.LEGS),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.FEET)
        ));

        TRIM_BONUSES.put("ironheart", List.of(
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.HEAD),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.CHEST),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.LEGS),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.FEET)
        ));

        TRIM_BONUSES.put("lumiere", List.of(
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.HEAD),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.CHEST),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.LEGS),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.FEET)
        ));

        TRIM_BONUSES.put("moonshard", List.of(
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.HEAD),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.CHEST),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.LEGS),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.FEET)
        ));

        TRIM_BONUSES.put("rustagate", List.of(
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.HEAD),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.CHEST),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.LEGS),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.FEET)
        ));

        TRIM_BONUSES.put("skyburrow", List.of(
                bonus(Attributes.MOVEMENT_SPEED, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, EquipmentSlot.HEAD),
                bonus(Attributes.MOVEMENT_SPEED, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, EquipmentSlot.CHEST),
                bonus(Attributes.MOVEMENT_SPEED, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, EquipmentSlot.LEGS),
                bonus(Attributes.MOVEMENT_SPEED, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, EquipmentSlot.FEET)
        ));

        TRIM_BONUSES.put("sungleam", List.of(
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.HEAD),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.CHEST),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.LEGS),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.FEET)
        ));

        TRIM_BONUSES.put("verdanite", List.of(
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.HEAD),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.CHEST),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.LEGS),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.FEET)
        ));

        TRIM_BONUSES.put("woecrystal", List.of(
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.HEAD),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.CHEST),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.LEGS),
                bonus(Attributes.ARMOR, 1.0, AttributeModifier.Operation.ADD_VALUE, EquipmentSlot.FEET)
        ));
    }


    public static void applyBonus(ItemStack stack, ArmorTrim trim) {
        String material = trim.material().unwrapKey().map(k -> k.location().getPath()).orElse("");
        List<TrimAttributeBonus> bonuses = TRIM_BONUSES.get(material);
        if (bonuses == null) return;

        EquipmentSlot thisSlot = getSlotForArmor(stack);
        if (thisSlot == null) return;

        ItemAttributeModifiers oldModifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

        // Copy all existing modifiers EXCEPT any with a jolcraft prefix (old trim bonuses)
        for (ItemAttributeModifiers.Entry entry : oldModifiers.modifiers()) {
            ResourceLocation id = entry.modifier().id();
            // Remove only JolCraft trim-related ones (prefix, or more robust, check contains 'jolcraft')
            if (id == null || !id.getNamespace().equals("jolcraft")) {
                builder.add(entry.attribute(), entry.modifier(), entry.slot());
            }
        }

        // Only apply bonuses matching this armor's slot
        for (TrimAttributeBonus bonus : bonuses) {
            if (bonus.slot == thisSlot) {
                builder.add(
                        bonus.attribute,
                        new AttributeModifier(
                                ResourceLocation.fromNamespaceAndPath("jolcraft", material + "_" + bonus.attribute.value().getDescriptionId() + "_" + bonus.slot.getName()),
                                bonus.amount,
                                bonus.operation
                        ),
                        EquipmentSlotGroup.bySlot(bonus.slot)
                );
            }
        }

        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, builder.build());
    }



    public static EquipmentSlot getSlotForArmor(ItemStack stack) {
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable != null) {
            return equippable.slot();
        }
        return null;
    }




    private static TrimAttributeBonus bonus(Holder<Attribute> attr, double amount, AttributeModifier.Operation op, EquipmentSlot slot) {
        return new TrimAttributeBonus(attr, amount, op, slot);
    }

    public record TrimAttributeBonus(
            Holder<Attribute> attribute,
            double amount,
            AttributeModifier.Operation operation,
            EquipmentSlot slot
    ) {}
}
