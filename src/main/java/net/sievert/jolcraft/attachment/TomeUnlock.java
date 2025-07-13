package net.sievert.jolcraft.attachment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.Set;

public interface TomeUnlock extends INBTSerializable<CompoundTag> {
    Set<String> getUnlocks();
    void addUnlock(String id);
    boolean hasUnlock(String id);

    static TomeUnlock get(Player player) {
        return player.getData(JolCraftAttachments.TOME_UNLOCK.get());
    }
}
