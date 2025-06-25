package net.sievert.jolcraft.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.advancement.JolCraftCriteriaTriggers;
import net.sievert.jolcraft.block.custom.FermentingCauldronBlock;
import net.sievert.jolcraft.block.custom.FermentingStage;
import net.sievert.jolcraft.capability.DwarvenLanguageImpl;
import net.sievert.jolcraft.capability.JolCraftAttachments;
import net.sievert.jolcraft.data.JolCraftTags;
import net.sievert.jolcraft.entity.custom.dwarf.DwarfGuardEntity;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.item.custom.SpannerItem;
import net.sievert.jolcraft.network.packet.ClientboundSyncEndorsementsPacket;
import net.sievert.jolcraft.network.packet.ClientboundSyncReputationPacket;
import net.sievert.jolcraft.util.SalvageLootHelper;
import net.sievert.jolcraft.network.JolCraftNetworking;
import net.sievert.jolcraft.network.packet.ClientboundSyncLanguagePacket;
import net.sievert.jolcraft.block.JolCraftBlocks;

import java.util.List;

@EventBusSubscriber(modid = JolCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class JolCraftGameEvents {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        var player = event.getEntity();
        var level = event.getLevel();
        var pos = event.getPos();
        var state = level.getBlockState(pos);

        if (!level.isClientSide()) {
            // Check that block is full water cauldron
            if (state.is(Blocks.WATER_CAULDRON) && state.getValue(LayeredCauldronBlock.LEVEL) == 3) {
                var mainHandStack = player.getMainHandItem();

                // === SUGAR logic for yeast creation ===
                if (mainHandStack.is(Items.SUGAR)) {
                    BlockState fermentState = JolCraftBlocks.FERMENTING_CAULDRON.get().defaultBlockState()
                            .setValue(FermentingCauldronBlock.LEVEL, 3)
                            .setValue(FermentingCauldronBlock.STAGE, FermentingStage.YEAST_FERMENTING);
                    level.setBlock(pos, fermentState, 3);

                    level.playSound(null, pos, SoundEvents.PLAYER_SPLASH, SoundSource.BLOCKS, 0.3F, 1.7F);

                    if (!player.isCreative()) mainHandStack.shrink(1);

                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                    return;
                }

                // === BARLEY MALT logic for malted stage ===
                if (mainHandStack.is(JolCraftItems.BARLEY_MALT.get())) {
                    BlockState maltedState = JolCraftBlocks.FERMENTING_CAULDRON.get().defaultBlockState()
                            .setValue(FermentingCauldronBlock.LEVEL, 3)
                            .setValue(FermentingCauldronBlock.STAGE, FermentingStage.MALTED);
                    // Optionally set a malted boolean property if you want

                    level.setBlock(pos, maltedState, 3);

                    level.playSound(null, pos, SoundEvents.PLAYER_SPLASH, SoundSource.BLOCKS, 0.3F, 1.7F);

                    if (!player.isCreative()) mainHandStack.shrink(1);

                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                    return;
                }
            }

        }
    }

    @SubscribeEvent
    public static void onInvulnerabilityCheck(EntityInvulnerabilityCheckEvent event) {
        if (event.getEntity() instanceof DwarfGuardEntity dwarf &&
                event.getSource().getEntity() instanceof Monster &&
                dwarf.isBlockCooldownReady()) {
            dwarf.markForBlocking();
            event.setInvulnerable(true);
        }
    }

    @SubscribeEvent
    public static void registerCustomTrades(final VillagerTradesEvent event) {
       if(event.getType() == VillagerProfession.LIBRARIAN) {
           Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

           trades.get(5).add((pTrader, pRandom) -> {
               int baseCost = 32 + pRandom.nextInt(33); // Random between 32 and 64
               return new MerchantOffer(
                       new ItemCost(Items.EMERALD, baseCost),
                       new ItemStack(JolCraftItems.DWARVEN_LEXICON.get(), 1),
                       1, 1, 0.05f
               );
           });
       }
    }


    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        ItemStack main = event.getItemStack();
        ItemStack offhand = player.getOffhandItem();

        if (!(offhand.getItem() instanceof SpannerItem)) return;
        if (!main.is(JolCraftTags.Items.GLOBAL_SCRAP)) return;

        if (!level.isClientSide) {
            List<ItemStack> loot = SalvageLootHelper.generateSalvageLoot(main);
            loot.forEach(stack -> level.addFreshEntity(new net.minecraft.world.entity.item.ItemEntity(
                    level,
                    player.getX(), player.getY() + 0.5, player.getZ(),
                    stack
            )));

            main.shrink(1); // consume the scrap
            offhand.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
            player.swing(InteractionHand.OFF_HAND, true);
            level.playSound(null, player.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.5F);
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        // --- Language ---
        var langCap = player.getData(JolCraftAttachments.DWARVEN_LANGUAGE);
        if (langCap != null) {
            if (player.isCreative() && !langCap.knowsLanguage()) {
                if (langCap instanceof DwarvenLanguageImpl impl) {
                    impl.grantTemporaryCreativeLanguage();
                }
            }
            // ✅ Sync to client
            if (player instanceof ServerPlayer serverPlayer) {
                JolCraftNetworking.sendToClient(serverPlayer, new ClientboundSyncLanguagePacket(langCap.knowsLanguage()));
            }
        }

        // --- Reputation ---
        var repCap = player.getData(JolCraftAttachments.DWARVEN_REP);
        if (repCap != null) {
            if (player.isCreative() && !repCap.wasGrantedByCreative()) {
                repCap.grantTemporaryCreativeReputation();
            }
            // ✅ Sync to client (add this packet!)
            if (player instanceof ServerPlayer serverPlayer) {
                // If you don't have a packet, just leave this out for now
                JolCraftNetworking.sendToClient(serverPlayer,
                        new ClientboundSyncReputationPacket(repCap.getTier())
                );
            }
        }
    }

    @SubscribeEvent
    public static void onGameModeChange(PlayerEvent.PlayerChangeGameModeEvent event) {
        Player player = event.getEntity();

        // --- Language ---
        var langCap = player.getData(JolCraftAttachments.DWARVEN_LANGUAGE);
        if (langCap instanceof DwarvenLanguageImpl impl) {
            if (event.getNewGameMode() == GameType.CREATIVE && event.getCurrentGameMode() != GameType.CREATIVE) {
                if (!impl.knowsLanguage()) {
                    impl.grantTemporaryCreativeLanguage();
                }
            } else if (event.getCurrentGameMode() == GameType.CREATIVE && event.getNewGameMode() != GameType.CREATIVE) {
                impl.revokeCreativeLanguage();
            }
            // ✅ Sync to client
            if (player instanceof ServerPlayer serverPlayer) {
                JolCraftNetworking.sendToClient(serverPlayer, new ClientboundSyncLanguagePacket(impl.knowsLanguage()));
            }
        }

        // --- Reputation ---
        var repCap = player.getData(JolCraftAttachments.DWARVEN_REP);
        if (repCap != null) {
            if (event.getNewGameMode() == GameType.CREATIVE && event.getCurrentGameMode() != GameType.CREATIVE) {
                if (!repCap.wasGrantedByCreative()) {
                    repCap.grantTemporaryCreativeReputation();
                }
            } else if (event.getCurrentGameMode() == GameType.CREATIVE && event.getNewGameMode() != GameType.CREATIVE) {
                repCap.revokeCreativeReputation();
            }
            // ✅ Sync to client (tier AND endorsements)
            if (player instanceof ServerPlayer serverPlayer) {
                JolCraftNetworking.sendToClient(serverPlayer,
                        new ClientboundSyncReputationPacket(repCap.getTier())
                );
                JolCraftNetworking.sendToClient(serverPlayer,
                        new ClientboundSyncEndorsementsPacket(repCap.getEndorsements())
                );
            }
        }
    }

    @SubscribeEvent
    public static void onAdvancementEarned(AdvancementEvent.AdvancementEarnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        JolCraftCriteriaTriggers.HAS_ADVANCEMENT.trigger(player, event.getAdvancement().id());
    }



}
