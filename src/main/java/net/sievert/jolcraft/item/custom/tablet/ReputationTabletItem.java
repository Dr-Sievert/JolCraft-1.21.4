package net.sievert.jolcraft.item.custom.tablet;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sievert.jolcraft.data.JolCraftDataComponents;
import net.sievert.jolcraft.network.JolCraftNetworking;
import net.sievert.jolcraft.network.packet.ClientboundEndorsementsPacket;
import net.sievert.jolcraft.network.packet.ClientboundReputationPacket;
import net.sievert.jolcraft.util.attachment.DwarvenLanguageHelper;
import net.sievert.jolcraft.util.attachment.DwarvenReputationHelper;

import java.util.List;

public class ReputationTabletItem extends Item {
    public ReputationTabletItem(Properties properties) {
        super(properties);
    }

    private static final int[] ENDORSEMENT_THRESHOLDS = {2, 5, 9, 14};

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (!DwarvenLanguageHelper.knowsDwarvishServer(serverPlayer)) {
                serverPlayer.displayClientMessage(
                        Component.translatable("tooltip.jolcraft.tablet.locked").withStyle(ChatFormatting.GRAY), true
                );
                return InteractionResult.SUCCESS;
            }

            int currentTier = DwarvenReputationHelper.getTierServer(serverPlayer);
            int endorsements = DwarvenReputationHelper.getEndorsementCountServer(serverPlayer);

            if (currentTier >= ENDORSEMENT_THRESHOLDS.length) {
                serverPlayer.displayClientMessage(
                        Component.translatable("tooltip.jolcraft.reputation.max_tier").withStyle(ChatFormatting.GRAY), true
                );
            } else {
                int needed = ENDORSEMENT_THRESHOLDS[currentTier];
                serverPlayer.displayClientMessage(
                        Component.literal("Endorsements for reputation advancement: " + endorsements + "/" + needed)
                                .withStyle(ChatFormatting.GRAY), true
                );
            }

            level.playSound(null, player.blockPosition(), SoundEvents.CHISELED_BOOKSHELF_INSERT, SoundSource.PLAYERS, 1.0f, 0.5f);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            int tier = DwarvenReputationHelper.getTierServer(serverPlayer);
            int endorsements = DwarvenReputationHelper.getEndorsementCountServer(serverPlayer);

            stack.set(JolCraftDataComponents.REP_OWNER.get(), serverPlayer.getName().getString());
            stack.set(JolCraftDataComponents.REP_TIER.get(), tier);
            stack.set(JolCraftDataComponents.REP_ENDORSEMENTS.get(), endorsements);

            JolCraftNetworking.sendToClient(serverPlayer,
                    new ClientboundEndorsementsPacket(DwarvenReputationHelper.getAllEndorsementsServer(serverPlayer))
            );
            JolCraftNetworking.sendToClient(serverPlayer,
                    new ClientboundReputationPacket(tier)
            );
        }
        super.onCraftedBy(stack, level, player);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (DwarvenLanguageHelper.knowsDwarvishClient()) {
            String ownerName = stack.getOrDefault(JolCraftDataComponents.REP_OWNER.get(), "Unknown");
            int tier = stack.getOrDefault(JolCraftDataComponents.REP_TIER.get(), 0);
            int endorsements = stack.getOrDefault(JolCraftDataComponents.REP_ENDORSEMENTS.get(), 0);

            tooltip.add(Component.translatable("tooltip.jolcraft.rep_owner", ownerName)
                    .withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltip.jolcraft.reputation_tier")
                    .append(Component.translatable("jolcraft.reputation_tier." + tier))
                    .withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltip.jolcraft.endorsement_count", endorsements)
                    .withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("tooltip.jolcraft.tablet.locked")
                    .withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
