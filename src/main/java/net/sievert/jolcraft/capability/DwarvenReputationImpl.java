package net.sievert.jolcraft.capability;

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

    private int tier = 0; // 0 = Stranger, 1 = Known Face, etc.
    private final Set<ResourceLocation> endorsements = new HashSet<>();

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public void setTier(int tier) {
        this.tier = tier;
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

    public static final Codec<DwarvenReputationImpl> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("tier").forGetter(DwarvenReputationImpl::getTier),
            ResourceLocation.CODEC.listOf().fieldOf("endorsements")
                    .xmap(HashSet::new, ArrayList::new)
                    .forGetter(rep -> new HashSet<>(rep.getEndorsements()))
    ).apply(instance, (tier, endorsementSet) -> {
        DwarvenReputationImpl impl = new DwarvenReputationImpl();
        impl.setTier(tier);
        impl.getEndorsements().addAll(endorsementSet);
        return impl;
    }));

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("tier", tier);

        ListTag endorsementList = new ListTag();
        for (ResourceLocation id : endorsements) {
            endorsementList.add(StringTag.valueOf(id.toString()));
        }
        tag.put("endorsements", endorsementList);

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
    }
}
