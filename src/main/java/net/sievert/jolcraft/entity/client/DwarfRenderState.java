package net.sievert.jolcraft.entity.client;


import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.item.ItemStack;
import net.sievert.jolcraft.entity.custom.DwarfGuardEntity;
import net.sievert.jolcraft.entity.custom.DwarfVariant;

public class DwarfRenderState extends HumanoidRenderState {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState blockAnimationState = new AnimationState();
    public DwarfVariant variant;
    public DwarfGuardEntity dwarf;
}