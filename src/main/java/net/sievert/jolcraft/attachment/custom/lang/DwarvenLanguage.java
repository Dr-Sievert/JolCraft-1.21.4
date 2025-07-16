package net.sievert.jolcraft.attachment.custom.lang;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.sievert.jolcraft.attachment.JolCraftAttachments;

public interface DwarvenLanguage extends INBTSerializable<CompoundTag> {
    boolean knowsLanguage();
    void setKnowsLanguage(boolean value);

    static DwarvenLanguage get(Player player) {
        return player.getData(JolCraftAttachments.DWARVEN_LANGUAGE.get());
    }
}
