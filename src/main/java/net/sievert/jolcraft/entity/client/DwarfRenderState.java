package net.sievert.jolcraft.entity.client;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.AnimationState;
import net.sievert.jolcraft.entity.custom.AbstractDwarfEntity;
import net.sievert.jolcraft.entity.custom.variation.DwarfBeardColor;
import net.sievert.jolcraft.entity.custom.variation.DwarfEyeColor;
import net.sievert.jolcraft.entity.custom.variation.DwarfVariant;

public class DwarfRenderState extends HumanoidRenderState {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState blockAnimationState = new AnimationState();
    public final AnimationState drinkAnimationState = new AnimationState();
    public AbstractDwarfEntity dwarf;
    public DwarfVariant variant;
    public DwarfBeardColor beard;
    public DwarfEyeColor eye;
}