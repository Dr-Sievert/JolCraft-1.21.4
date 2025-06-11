package net.sievert.jolcraft.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.sievert.jolcraft.entity.custom.DwarfEntity;

import java.util.EnumSet;


public class DwarfRevengeGoal extends Goal
{
    private final DwarfEntity entity;

    public DwarfRevengeGoal(DwarfEntity entity)
    {
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse()
    {
        return this.entity.getLastHurtByMob() != null && this.entity.getLastHurtByMob().isAlive() && this.entity.distanceTo(this.entity.getLastHurtByMob()) <= 10.0F && (!(this.entity.getLastHurtByMob() instanceof Player) || !((Player)this.entity.getLastHurtByMob()).isCreative());
    }

    @Override
    public void tick()
    {
        LivingEntity revengeTarget = this.entity.getLastHurtByMob();
        if(revengeTarget != null && this.entity.getTradingPlayer() == null && revengeTarget instanceof Player)
        {
            this.entity.getLookControl().setLookAt(revengeTarget, 10.0F, (float) this.entity.getHeadRotSpeed());
            if(this.entity.distanceTo(revengeTarget) >= 1.5D)
            {
                this.entity.getNavigation().moveTo(revengeTarget, 1.3F);
            }
            else
            {
                revengeTarget.hurt(this.entity.damageSources().mobAttack(this.entity), (float) this.entity.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE).getValue());
                this.entity.setAttacking(true);
                this.entity.setLastHurtByMob(null);
            }
        }
    }

    @Override
    public boolean canContinueToUse()
    {
        return this.entity.getLastHurtByMob() != null && this.entity.getLastHurtByMob().isAlive() && this.entity.distanceTo(this.entity.getLastHurtByMob()) <= 10.0F && this.entity.getTradingPlayer() == null;
    }

    @Override
    public void stop()
    {
        this.entity.setAttacking(false);
        this.entity.setLastHurtByMob(null);
    }
}