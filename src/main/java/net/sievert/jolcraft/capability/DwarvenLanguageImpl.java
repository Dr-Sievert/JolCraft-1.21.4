package net.sievert.jolcraft.capability;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class DwarvenLanguageImpl implements DwarvenLanguage {

    private boolean knowsLanguage = false;

    @Override
    public boolean knowsLanguage() {
        return this.knowsLanguage;
    }

    @Override
    public void setKnowsLanguage(boolean knows) {
        this.knowsLanguage = knows;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("KnowsDwarvish", this.knowsLanguage());
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.setKnowsLanguage(tag.getBoolean("KnowsDwarvish"));
    }
}