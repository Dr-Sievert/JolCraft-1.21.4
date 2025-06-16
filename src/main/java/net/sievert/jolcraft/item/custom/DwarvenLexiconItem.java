package net.sievert.jolcraft.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.sievert.jolcraft.capability.DwarvenLanguage;
import net.sievert.jolcraft.capability.JolCraftCapabilities;

public class DwarvenLexiconItem extends Item {

    public DwarvenLexiconItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            DwarvenLanguage lang = serverPlayer.getCapability(JolCraftCapabilities.DWARVEN_LANGUAGE);
            if (lang != null) {
                if (!lang.knowsLanguage()) {
                    lang.setKnowsLanguage(true);
                    level.playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 2.0f, 0.7f);
                    serverPlayer.displayClientMessage(Component.literal("You have learned to understand the dwarven language"), true);
                } else {
                    serverPlayer.displayClientMessage(Component.literal("You already understand the dwarven language"), true);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    private boolean hasDwarvenLanguage(ServerPlayer player) {
        DwarvenLanguage cap = player.getCapability(JolCraftCapabilities.DWARVEN_LANGUAGE);
        return cap != null && cap.knowsLanguage();
    }

    private void grantDwarvenLanguage(ServerPlayer player) {
        DwarvenLanguage cap = player.getCapability(JolCraftCapabilities.DWARVEN_LANGUAGE);
        if (cap != null) {
            cap.setKnowsLanguage(true);
        }
    }


}