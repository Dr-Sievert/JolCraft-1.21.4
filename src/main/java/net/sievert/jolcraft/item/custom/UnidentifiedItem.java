package net.sievert.jolcraft.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class UnidentifiedItem extends Item {
    public UnidentifiedItem(Properties properties) {
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
                serverPlayer.displayClientMessage(getIdentifyFailMessage(serverPlayer), true);
            }
        }
        return InteractionResult.SUCCESS;
    }

    /** Must define in subclasses: requirement to identify. */
    protected abstract boolean canIdentify(ServerPlayer player);

    /** Must define in subclasses: which item to return upon identification. */
    protected abstract ItemStack getRandomIdentifiedItem(ServerPlayer player, ItemStack original);

    /** Must define in subclasses: tooltip for unidentified state. */
    protected abstract Component getUnidentifiedTooltip(Player player, ItemStack stack);

    /** Must define in subclasses: message on successful identification. */
    protected abstract Component getIdentifySuccessMessage(ServerPlayer player, ItemStack identified);

    /** Must define in subclasses: message on failed identification. */
    protected abstract Component getIdentifyFailMessage(ServerPlayer player);

    /** Must define in subclasses: sound to play on success. */
    protected abstract void playIdentifySuccessSound(Level level, Player player);

    /** Must define in subclasses: sound to play on fail. */
    protected abstract void playIdentifyFailSound(Level level, Player player);

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (context.level() != null && context.level().isClientSide()) {
            Player player = net.minecraft.client.Minecraft.getInstance().player;
            tooltip.add(getUnidentifiedTooltip(player, stack));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
