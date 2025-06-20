package net.sievert.jolcraft.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.capability.DwarvenLanguageImpl;
import net.sievert.jolcraft.capability.JolCraftAttachments;
import net.sievert.jolcraft.data.JolCraftTags;
import net.sievert.jolcraft.entity.custom.dwarf.DwarfGuardEntity;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.item.custom.SpannerItem;
import net.sievert.jolcraft.util.SalvageLootHelper;
import net.sievert.jolcraft.network.JolCraftNetworking;
import net.sievert.jolcraft.network.packet.ClientboundSyncLanguagePacket;

import java.util.List;

@EventBusSubscriber(modid = JolCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class JolCraftGameEvents {

    //Hurting living entities
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof DwarfGuardEntity dwarf && dwarf.isBlockCooldownReady() && event.getSource().getEntity() instanceof Monster) {
            dwarf.markForBlocking();
            event.setNewDamage(0.2F);
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
        var cap = player.getData(JolCraftAttachments.DWARVEN_LANGUAGE);

        if (cap != null) {
            // 🧠 Grant temp access if joining in creative
            if (player.isCreative() && !cap.knowsLanguage()) {
                if (cap instanceof DwarvenLanguageImpl impl) {
                    impl.grantTemporaryCreativeLanguage();
                }
            }

            // ✅ Sync to client
            if (player instanceof ServerPlayer serverPlayer) {
                JolCraftNetworking.sendToClient(serverPlayer, new ClientboundSyncLanguagePacket(cap.knowsLanguage()));
                System.out.println("[SERVER] Sync packet on join: " + cap.knowsLanguage());
            }
        }
    }

    @SubscribeEvent
    public static void onGameModeChange(PlayerEvent.PlayerChangeGameModeEvent event) {
        Player player = event.getEntity();
        var cap = player.getData(JolCraftAttachments.DWARVEN_LANGUAGE);

        if (cap instanceof DwarvenLanguageImpl impl) {
            if (event.getNewGameMode() == GameType.CREATIVE && event.getCurrentGameMode() != GameType.CREATIVE) {
                // 🟢 Entering creative — grant temp language if needed
                if (!impl.knowsLanguage()) {
                    impl.grantTemporaryCreativeLanguage();
                }
            } else if (event.getCurrentGameMode() == GameType.CREATIVE && event.getNewGameMode() != GameType.CREATIVE) {
                // 🔴 Leaving creative — revoke temp language if it was granted this way
                impl.revokeCreativeLanguage();
            }

            // ✅ Sync to client
            if (player instanceof ServerPlayer serverPlayer) {
                JolCraftNetworking.sendToClient(serverPlayer, new ClientboundSyncLanguagePacket(impl.knowsLanguage()));
            }
        }
    }


}
