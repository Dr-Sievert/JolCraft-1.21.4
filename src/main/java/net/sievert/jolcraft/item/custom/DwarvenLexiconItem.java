package net.sievert.jolcraft.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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
import net.sievert.jolcraft.capability.JolCraftAttachments;
import net.sievert.jolcraft.client.data.MyClientLanguageData;
import net.sievert.jolcraft.network.JolCraftNetworking;
import net.sievert.jolcraft.network.packet.ClientboundSyncLanguagePacket;

import java.util.List;

public class DwarvenLexiconItem extends Item {

    public DwarvenLexiconItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            var lang = serverPlayer.getData(JolCraftAttachments.DWARVEN_LANGUAGE.get());
            if (!lang.knowsLanguage()) {
                lang.setKnowsLanguage(true);

                // ✅ SEND PACKET TO CLIENT
                JolCraftNetworking.sendToClient(serverPlayer, new ClientboundSyncLanguagePacket(true));

                JolCraftCriteriaTriggers.HAS_DWARVEN_LANGUAGE.trigger(serverPlayer);
                level.playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 2.0f, 0.7f);
                serverPlayer.displayClientMessage(Component.literal("You have learned to understand the dwarven language"), true);
            } else {
                serverPlayer.displayClientMessage(Component.literal("You already understand the dwarven language"), true);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        boolean knows = MyClientLanguageData.knowsLanguage();
        if (knows) {
            tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_lexicon.unlocked").withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("tooltip.jolcraft.dwarven_lexicon.locked").withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, context, tooltip, flag);
    }




}

