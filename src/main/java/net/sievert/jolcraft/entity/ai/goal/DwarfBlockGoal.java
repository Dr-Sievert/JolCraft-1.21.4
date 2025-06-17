package net.sievert.jolcraft.entity.ai.goal;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.sievert.jolcraft.entity.custom.dwarf.DwarfGuardEntity;


import java.util.EnumSet;

public class DwarfBlockGoal extends Goal {

    private final DwarfGuardEntity dwarf;
    private int blockTicks = 0;

    public DwarfBlockGoal(DwarfGuardEntity dwarf) {
        this.dwarf = dwarf;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!dwarf.isBlockCooldownReady()) {
            return false;
        }

        if (dwarf.getTarget() != null && dwarf.consumeBlockFlag()) {
            return true;
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return dwarf.isBlocking() && dwarf.getTarget() != null && blockTicks-- > 0;
    }

    @Override
    public void start() {
        dwarf.setBlocking(true);
        dwarf.level().playSound(null, dwarf.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.HOSTILE, 1.0F, 1.0F);
        blockTicks = 15; // block for 15 ticks
    }

    @Override
    public void stop() {
        dwarf.setBlocking(false);
        dwarf.setBlockCooldown(60); // Cooldown now tracked in entity
    }

    @Override
    public void tick() {
        if (dwarf.getTarget() != null) {
            dwarf.getLookControl().setLookAt(dwarf.getTarget(), 10.0F, dwarf.getMaxHeadXRot());
        }
    }
}
