package net.sievert.jolcraft.entity.ai.goal;


import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;

public class DwarfUseItemGoal<T extends Mob> extends Goal {
    private final T mob;
    private final ItemStack item;
    private final Predicate<? super T> canUseSelector;
    @Nullable
    private final SoundEvent finishUsingSound;

    private final int cooldownTicks;
    private int cooldownTimer = 0;

    public DwarfUseItemGoal(T mob, ItemStack item, @Nullable SoundEvent finishUsingSound, Predicate<? super T> canUseSelector, int cooldownTicks) {
        this.mob = mob;
        this.item = item;
        this.finishUsingSound = finishUsingSound;
        this.canUseSelector = canUseSelector;
        this.cooldownTicks = cooldownTicks;
    }

    @Override
    public boolean canUse() {
        if (cooldownTimer > 0) {
            cooldownTimer--; // decrement cooldown while idle
            return false;
        }
        return this.canUseSelector.test(this.mob);
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.isUsingItem();
    }

    @Override
    public void start() {
        this.mob.setItemSlot(EquipmentSlot.MAINHAND, this.item.copy());
        this.mob.startUsingItem(InteractionHand.MAIN_HAND);
    }

    @Override
    public void stop() {
        this.mob.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        if (this.finishUsingSound != null) {
            this.mob.playSound(this.finishUsingSound, 1.0F, this.mob.getRandom().nextFloat() * 0.2F + 0.9F);
        }
        this.cooldownTimer = cooldownTicks; // start cooldown after use
    }

}

