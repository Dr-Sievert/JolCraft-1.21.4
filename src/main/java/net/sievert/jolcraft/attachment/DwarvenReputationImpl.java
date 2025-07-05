package net.sievert.jolcraft.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.sievert.jolcraft.JolCraft;

import java.util.*;

public class DwarvenReputationImpl implements DwarvenReputation {

    private int tier = 0;
    private final Set<ResourceLocation> endorsements = new HashSet<>();
    private boolean grantedByCreative = false;

    @Override
    public int getTier() {
        // Return max tier if granted by creative, else normal tier
        return grantedByCreative ? getMaxTier() : tier;
    }

    @Override
    public void setTier(int tier) {
        this.tier = tier;
        // If manually setting, clear creative override
        this.grantedByCreative = false;
    }

    @Override
    public Set<ResourceLocation> getEndorsements() {
        return endorsements;
    }

    @Override
    public void addEndorsement(ResourceLocation professionId) {
        endorsements.add(professionId);
    }

    @Override
    public boolean hasEndorsement(ResourceLocation professionId) {
        return endorsements.contains(professionId);
    }

    // --- Creative Rep Logic ---

    @Override
    public void grantTemporaryCreativeReputation() {
        this.grantedByCreative = true;
    }

    @Override
    public void revokeCreativeReputation() {
        this.grantedByCreative = false;
    }

    @Override
    public boolean wasGrantedByCreative() {
        return this.grantedByCreative;
    }

    // --- Serialization ---

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("tier", tier);

        ListTag endorsementList = new ListTag();
        for (ResourceLocation id : endorsements) {
            endorsementList.add(StringTag.valueOf(id.toString()));
        }
        tag.put("endorsements", endorsementList);

        tag.putBoolean("GrantedByCreative", grantedByCreative); // NEW
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.tier = tag.getInt("tier");
        this.endorsements.clear();

        ListTag endorsementList = tag.getList("endorsements", 8); // 8 = StringTag
        for (int i = 0; i < endorsementList.size(); i++) {
            String idString = endorsementList.getString(i);
            ResourceLocation id = ResourceLocation.tryParse(idString);
            if (id != null) {
                endorsements.add(id);
            } else {
                JolCraft.LOGGER.warn("Failed to parse endorsement ID: '{}'", idString);
            }
        }
        this.grantedByCreative = tag.getBoolean("GrantedByCreative"); // NEW
    }

    public static final Codec<DwarvenReputationImpl> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("tier").forGetter(rep -> rep.tier),
            ResourceLocation.CODEC.listOf().fieldOf("endorsements")
                    .xmap(HashSet::new, ArrayList::new)
                    .forGetter(rep -> new HashSet<>(rep.endorsements)),
            Codec.BOOL.optionalFieldOf("GrantedByCreative", false).forGetter(rep -> rep.grantedByCreative)
    ).apply(instance, (tier, endorsementSet, grantedByCreative) -> {
        DwarvenReputationImpl impl = new DwarvenReputationImpl();
        impl.tier = tier;
        impl.endorsements.addAll(endorsementSet);
        impl.grantedByCreative = grantedByCreative;
        return impl;
    }));
}
