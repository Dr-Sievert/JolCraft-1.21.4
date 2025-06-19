package net.sievert.jolcraft.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.capability.DwarvenReputationImpl;
import net.sievert.jolcraft.capability.JolCraftAttachments;
import net.sievert.jolcraft.component.JolCraftDataComponents;
import net.sievert.jolcraft.item.JolCraftItems;

import java.util.List;

public class ReputationTabletItem extends Item {
    public ReputationTabletItem(Properties properties) {
        super(properties);
    }

    private static final int[] ENDORSEMENT_THRESHOLDS = {2, 5, 9, 14};

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            DwarvenReputationImpl rep = serverPlayer.getData(JolCraftAttachments.DWARVEN_REP.get());
            int currentTier = rep.getTier();
            int endorsements = rep.getEndorsementCount();
            System.out.println("Player rep tier: " + currentTier + ", endorsement count: " + endorsements);

            if (currentTier >= ENDORSEMENT_THRESHOLDS.length) {
                serverPlayer.displayClientMessage(
                        Component.literal("You have reached the highest reputation tier!"), true
                );
            } else {
                int needed = ENDORSEMENT_THRESHOLDS[currentTier];
                serverPlayer.displayClientMessage(
                        Component.literal("Endorsements for reputation advancement: " + endorsements + "/" + needed).withStyle(ChatFormatting.GRAY), true
                );
            }
            level.playSound(null, player.blockPosition(), SoundEvents.CHISELED_BOOKSHELF_INSERT, SoundSource.PLAYERS, 1.0f, 0.5f);
        }

        return InteractionResult.SUCCESS;
    }


    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (stack.is(JolCraftItems.REPUTATION_TABLET_0.get())) {
                stack.set(JolCraftDataComponents.REP_OWNER.get(), serverPlayer.getName().getString());
                stack.set(JolCraftDataComponents.REP_TIER.get(), 0);
                stack.set(JolCraftDataComponents.REP_ENDORSEMENTS.get(), 1);

                DwarvenReputationImpl rep = serverPlayer.getData(JolCraftAttachments.DWARVEN_REP.get());
                System.out.println("Attempting to add endorsement to player rep");

                ResourceLocation HISTORIAN_ID = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "historian");
                if (!rep.hasEndorsement(HISTORIAN_ID)) {
                    rep.addEndorsement(HISTORIAN_ID);
                    System.out.println("✅ Added endorsement: historian");
                } else {
                    System.out.println("ℹ️ Historian endorsement already present");
                }

                System.out.println("🎯 Player endorsement count: " + rep.getEndorsementCount());
            }
        }

        super.onCraftedBy(stack, level, player);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        boolean knows = net.sievert.jolcraft.client.data.MyClientLanguageData.knowsLanguage();

        if (knows) {
            String ownerName = stack.getOrDefault(JolCraftDataComponents.REP_OWNER.get(), "Unknown");
            int tier = stack.getOrDefault(JolCraftDataComponents.REP_TIER.get(), 0);
            int endorsements = stack.getOrDefault(JolCraftDataComponents.REP_ENDORSEMENTS.get(), 0);

            tooltip.add(Component.translatable("tooltip.jolcraft.rep_owner", ownerName)
                    .withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltip.jolcraft.reputation_tier")
                    .append(Component.translatable("jolcraft.reputation_tier." + tier))
                    .withStyle(ChatFormatting.GOLD));
            tooltip.add(Component.translatable("tooltip.jolcraft.endorsement_count", endorsements)
                    .withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("tooltip.jolcraft.tablet.locked")
                    .withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, context, tooltip, flag);
    }


}
