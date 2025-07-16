package net.sievert.jolcraft.attachment.custom.lang;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.sievert.jolcraft.attachment.JolCraftAttachments;

public interface AncientDwarvenLanguage extends INBTSerializable<CompoundTag> {
    boolean knowsLanguage();
    void setKnowsLanguage(boolean value);

    static AncientDwarvenLanguage get(Player player) {
        return player.getData(JolCraftAttachments.ANCIENT_DWARVEN_LANGUAGE.get());
    }
}
