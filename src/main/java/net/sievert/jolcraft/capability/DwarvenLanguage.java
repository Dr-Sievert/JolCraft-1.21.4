package net.sievert.jolcraft.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface DwarvenLanguage extends INBTSerializable<CompoundTag> {
    boolean knowsLanguage();
    void setKnowsLanguage(boolean value);
}