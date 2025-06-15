package net.sievert.jolcraft.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.capability.DwarvenLanguage;
import net.sievert.jolcraft.capability.JolCraftCapabilities;
import net.sievert.jolcraft.entity.custom.DwarfGuardEntity;
import net.sievert.jolcraft.item.JolCraftItems;

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
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        DwarvenLanguage oldCap = event.getOriginal().getCapability(JolCraftCapabilities.DWARVEN_LANGUAGE);
        DwarvenLanguage newCap = event.getEntity().getCapability(JolCraftCapabilities.DWARVEN_LANGUAGE);

        if (oldCap != null && newCap != null) {
            var tag = oldCap.serializeNBT(event.getOriginal().level().registryAccess());
            newCap.deserializeNBT(event.getEntity().level().registryAccess(), tag);
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



}
