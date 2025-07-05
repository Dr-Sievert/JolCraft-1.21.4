package net.sievert.jolcraft.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class DwarvenLanguageImpl implements DwarvenLanguage {
    private boolean knowsLanguage = false;
    private boolean grantedByCreative = false;

    @Override
    public boolean knowsLanguage() {
        return knowsLanguage;
    }

    @Override
    public void setKnowsLanguage(boolean value) {
        this.knowsLanguage = value;
        if (!value) {
            this.grantedByCreative = false; // Reset creative flag if manually revoked
        }
    }

    public void grantTemporaryCreativeLanguage() {
        this.knowsLanguage = true;
        this.grantedByCreative = true;
    }

    public void revokeCreativeLanguage() {
        if (grantedByCreative) {
            this.knowsLanguage = false;
            this.grantedByCreative = false;
        }
    }

    public boolean wasGrantedByCreative() {
        return this.grantedByCreative;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("KnowsLanguage", knowsLanguage);
        tag.putBoolean("GrantedByCreative", grantedByCreative);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.knowsLanguage = tag.getBoolean("KnowsLanguage");
        this.grantedByCreative = tag.getBoolean("GrantedByCreative");
    }
}