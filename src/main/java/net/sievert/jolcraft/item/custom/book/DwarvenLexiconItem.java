package net.sievert.jolcraft.item.custom.book;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sievert.jolcraft.advancement.JolCraftCriteriaTriggers;
import net.sievert.jolcraft.network.JolCraftNetworking;
import net.sievert.jolcraft.network.packet.ClientboundLanguagePacket;
import net.sievert.jolcraft.sound.JolCraftSounds;
import net.sievert.jolcraft.util.attachment.DwarvenLanguageHelper;

import java.util.List;

public class DwarvenLexiconItem extends Item {

    public DwarvenLexiconItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            // Strict: do NOT grant for creative, only for real unlock
            if (!DwarvenLanguageHelper.knowsDwarvishServerBypassCreative(serverPlayer)) {
                DwarvenLanguageHelper.setKnowsDwarvishServer(serverPlayer, true);

                // ✅ SEND PACKET TO CLIENT
                JolCraftNetworking.sendToClient(serverPlayer, new ClientboundLanguagePacket(true));

                JolCraftCriteriaTriggers.HAS_DWARVEN_LANGUAGE.trigger(serverPlayer);
                level.playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 2.0f, 0.7f);
                level.playSound(null, player.blockPosition(), JolCraftSounds.LEVEL_UP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                serverPlayer.displayClientMessage(Component.translatable("tooltip.jolcraft.dwarven_lexicon.use").withStyle(ChatFormatting.GREEN), true);
            } else {
                serverPlayer.displayClientMessage(Component.translatable("tooltip.jolcraft.dwarven_lexicon.knows").withStyle(ChatFormatting.GRAY), true);
                level.playSound(null, player.blockPosition(), SoundEvents.BOOK_PUT, SoundSource.PLAYERS, 1.2f, 0.8f);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        boolean knows = DwarvenLanguageHelper.knowsDwarvishClient();

        if (Screen.hasShiftDown()) {
            // Detailed (Shift) tooltip
            if (knows) {
                tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_lexicon.shift")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            } else {
                tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_lexicon.locked")
                        .withStyle(ChatFormatting.GRAY));
            }
        } else {
            // Normal summary
            if (knows) {
                tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_lexicon.unlocked")
                        .withStyle(ChatFormatting.GRAY));
            } else {
                tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_lexicon.locked")
                        .withStyle(ChatFormatting.GRAY));
            }
            Component shiftKey = Component.literal("Shift").withStyle(ChatFormatting.BLUE);
            tooltip.add(Component.translatable("tooltip.jolcraft.shift", shiftKey)
                    .withStyle(ChatFormatting.DARK_GRAY));
        }

        super.appendHoverText(stack, context, tooltip, flag);
    }

}
