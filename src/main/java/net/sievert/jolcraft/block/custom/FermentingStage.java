package net.sievert.jolcraft.block.custom;

import net.minecraft.util.StringRepresentable;

public enum FermentingStage implements StringRepresentable {
    YEAST_FERMENTING,
    YEAST_READY,
    MALTED,
    HOPS,             // New: Hops are added here
    BREW_FERMENTING,
    BREW_READY;

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
