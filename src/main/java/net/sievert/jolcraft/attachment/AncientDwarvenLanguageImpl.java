package net.sievert.jolcraft.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class AncientDwarvenLanguageImpl implements AncientDwarvenLanguage {
    private boolean knowsLanguage = false;

    @Override
    public boolean knowsLanguage() {
        return knowsLanguage;
    }

    @Override
    public void setKnowsLanguage(boolean value) {
        this.knowsLanguage = value;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("KnowsLanguage", knowsLanguage);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.knowsLanguage = tag.getBoolean("KnowsLanguage");
    }
}

