package net.sievert.jolcraft.entity.client.dwarf;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.AnimationState;
import net.sievert.jolcraft.entity.custom.dwarf.AbstractDwarfEntity;
import net.sievert.jolcraft.entity.custom.dwarf.variation.DwarfBeardColor;
import net.sievert.jolcraft.entity.custom.dwarf.variation.DwarfEyeColor;
import net.sievert.jolcraft.entity.custom.dwarf.variation.DwarfVariant;

public class DwarfRenderState extends HumanoidRenderState {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState inspectingAnimationState = new AnimationState();
    public final AnimationState blockingAnimationState = new AnimationState();
    public final AnimationState drinkAnimationState = new AnimationState();
    public AbstractDwarfEntity dwarf;
    public DwarfVariant variant;
    public DwarfBeardColor beard;
    public DwarfEyeColor eye;
}