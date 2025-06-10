package net.sievert.jolcraft.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.Goal;
import net.sievert.jolcraft.entity.custom.DwarfGuardEntity;

import java.util.EnumSet;

public class DwarfBlockGoal extends Goal {

    private final DwarfGuardEntity dwarf;
    private int cooldown = 0;
    private int blockTicks = 0;

    public DwarfBlockGoal(DwarfGuardEntity dwarf) {
        this.dwarf = dwarf;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        return dwarf.getTarget() != null && !dwarf.isBlocking();
    }

    @Override
    public boolean canContinueToUse() {
        return dwarf.isBlocking() && dwarf.getTarget() != null && blockTicks-- > 0;
    }

    @Override
    public void start() {
        dwarf.setBlocking(true);
        blockTicks = 15; // block for 15 ticks
    }

    @Override
    public void stop() {
        dwarf.setBlocking(false);
        cooldown = 60; // 3 second cooldown
    }

    @Override
    public void tick() {
        if (dwarf.getTarget() != null) {
            dwarf.getLookControl().setLookAt(dwarf.getTarget(), 10.0F, dwarf.getMaxHeadXRot());
            dwarf.getNavigation().stop(); // remain still while blocking
        }
    }
}
