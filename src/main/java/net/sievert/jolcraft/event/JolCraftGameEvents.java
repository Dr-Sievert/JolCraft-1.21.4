package net.sievert.jolcraft.event;

import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.capability.DwarvenLanguage;
import net.sievert.jolcraft.capability.JolCraftCapabilities;
import net.sievert.jolcraft.entity.custom.DwarfGuardEntity;

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

        }

}
