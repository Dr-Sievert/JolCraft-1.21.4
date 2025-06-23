package net.sievert.jolcraft.block.custom;

import net.minecraft.util.StringRepresentable;

public enum FermentingStage implements StringRepresentable {
    YEAST_FERMENTING,
    YEAST_READY,
    MALTED,
    BREW_FERMENTING,
    BREW_READY;

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
