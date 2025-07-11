package net.sievert.jolcraft.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class AncientUnidentifiedItem extends AncientItemBase {
    public AncientUnidentifiedItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (canIdentify(serverPlayer)) {
                ItemStack stack = player.getItemInHand(hand);
                ItemStack identified = getRandomIdentifiedItem(serverPlayer, stack);

                if (!identified.isEmpty()) {
                    if (player.getAbilities().instabuild) {
                        boolean added = false;
                        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                            if (player.getInventory().getItem(i).isEmpty()) {
                                added = player.addItem(identified.copy());
                                break;
                            }
                        }
                        if (!added) {
                            player.drop(identified.copy(), false);
                        }
                    } else {
                        if (stack.getCount() == 1) {
                            player.setItemInHand(hand, identified);
                        } else {
                            stack.shrink(1);
                            boolean added = player.addItem(identified);
                            if (!added) {
                                player.drop(identified, false);
                            }
                        }
                    }
                    playIdentifySuccessSound(level, player);
                    serverPlayer.displayClientMessage(getIdentifySuccessMessage(serverPlayer, identified), true);
                }
            } else {
                playIdentifyFailSound(level, player);
                // Language takes precedence
                if (!hasRequiredLanguage(serverPlayer)) {
                    serverPlayer.displayClientMessage(getFailMessageMissingLanguage(serverPlayer), true);
                } else {
                    serverPlayer.displayClientMessage(getFailMessageMissingEffect(serverPlayer), true);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    /** Determines whether this player can identify the item. */
    protected abstract boolean canIdentify(ServerPlayer player);

    /** Determines if player meets the language requirement (used for fail precedence). */
    protected abstract boolean hasRequiredLanguage(ServerPlayer player);

    /** Returns the identified item (or ItemStack.EMPTY if failed/invalid). */
    protected abstract ItemStack getRandomIdentifiedItem(ServerPlayer player, ItemStack original);

    /** Tooltip (content only, not gating/knowledge logic). */
    @Override
    protected abstract Component getFullyReadableTooltip(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flag);

    @Override
    protected abstract Component getLockedTooltip(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flag);

    @Override
    protected abstract Component getPartialUnderstandingTooltip(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flag);

    @Override
    protected abstract Component getUnreadableTooltipSGA(ItemStack stack, Player player, List<Component> tooltip, TooltipFlag flag);

    /** Message when identification succeeds. */
    protected abstract Component getIdentifySuccessMessage(ServerPlayer player, ItemStack identified);

    /** Message when identification fails, language missing. */
    protected abstract Component getFailMessageMissingLanguage(ServerPlayer player);

    /** Message when identification fails, effect missing (but language present). */
    protected abstract Component getFailMessageMissingEffect(ServerPlayer player);

    /** Sound on successful identification. */
    protected abstract void playIdentifySuccessSound(Level level, Player player);

    /** Sound on failed identification. */
    protected abstract void playIdentifyFailSound(Level level, Player player);
}
