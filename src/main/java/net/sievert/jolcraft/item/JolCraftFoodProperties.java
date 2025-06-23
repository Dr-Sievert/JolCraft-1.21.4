package net.sievert.jolcraft.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class JolCraftFoodProperties {

    public static final FoodProperties DWARVEN_BREW = new FoodProperties.Builder().alwaysEdible().nutrition(3).saturationModifier(0.25f).build();

    // Ensure both effects are applied with the given chances
    public static final Consumable DWARVEN_BREW_EFFECT = Consumables.defaultDrink()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 400, 2), 1.0f)) // Always apply health boost
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.CONFUSION, 400, 0), 1.0f)) // Always apply confusion
            .build();
}
