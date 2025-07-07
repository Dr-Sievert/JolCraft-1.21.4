package net.sievert.jolcraft.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

class HomesteadEffect extends MobEffect {
    protected HomesteadEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity entity, int amplifier) {
        if (entity.getHealth() < entity.getMaxHealth()) {
            entity.heal(1.0F);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        // 50 * 3 = 150 for level 0, so one heal every 7.5 seconds at amp 0
        int i = (50 * 3) >> amplifier;
        return i > 0 ? duration % i == 0 : true;
    }
}

