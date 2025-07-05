package net.sievert.jolcraft.attachment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface Hearth extends INBTSerializable<CompoundTag> {
    boolean hasLitThisDay();
    void setLitThisDay(boolean value);

    static Hearth get(Player player) {
        return player.getData(JolCraftAttachments.HEARTH.get());
    }
}
