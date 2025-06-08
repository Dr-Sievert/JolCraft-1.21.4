package net.sievert.jolcraft.entity.client;


import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;
import net.sievert.jolcraft.entity.custom.DwarfVariant;

public class DwarfRenderState extends LivingEntityRenderState {
    public final AnimationState idleAnimationState = new AnimationState();
    public DwarfVariant variant;
}