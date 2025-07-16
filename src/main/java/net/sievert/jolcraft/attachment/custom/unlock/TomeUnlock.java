package net.sievert.jolcraft.attachment.custom.unlock;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.sievert.jolcraft.attachment.JolCraftAttachments;

import java.util.Set;

public interface TomeUnlock extends INBTSerializable<CompoundTag> {
    Set<String> getUnlocks();
    void addUnlock(String id);
    boolean hasUnlock(String id);

    static TomeUnlock get(Player player) {
        return player.getData(JolCraftAttachments.TOME_UNLOCK.get());
    }
}
