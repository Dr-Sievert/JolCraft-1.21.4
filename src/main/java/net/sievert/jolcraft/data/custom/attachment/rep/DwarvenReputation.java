package net.sievert.jolcraft.data.custom.attachment.rep;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.sievert.jolcraft.data.JolCraftAttachments;

import java.util.Set;

public interface DwarvenReputation extends INBTSerializable<CompoundTag> {
    int getTier();
    void setTier(int tier);

    Set<ResourceLocation> getEndorsements();
    void addEndorsement(ResourceLocation professionId);
    boolean hasEndorsement(ResourceLocation professionId);

    default int getEndorsementCount() {
        return getEndorsements().size();
    }

    default int getMaxTier() {
        return 4;
    }

    static DwarvenReputation get(Player player) {
        return player.getData(JolCraftAttachments.DWARVEN_REP.get());
    }

    static String getTierName(int tier) {
        return switch (tier) {
            case 0 -> "Stranger";
            case 1 -> "Known Face";
            case 2 -> "Trusted";
            case 3 -> "Respected";
            case 4 -> "Blood-Kin";
            default -> "Unknown";
        };
    }
}
