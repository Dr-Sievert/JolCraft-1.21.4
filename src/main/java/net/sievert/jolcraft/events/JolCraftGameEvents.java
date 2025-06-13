package net.sievert.jolcraft.events;

import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.data.JolCraftTags;
import net.sievert.jolcraft.entity.custom.DwarfGuardEntity;
import net.sievert.jolcraft.villager.JolCraftVillagers;

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
        if (event.getType() == VillagerProfession.CARTOGRAPHER){
            new VillagerTrades.TreasureMapForEmeralds(
                    13,
                    JolCraftTags.Structures.ON_FORGE_EXPLORER_MAPS,
                    "filled_map.jolcraft.forge",
                    MapDecorationTypes.TARGET_X,
                    8,
                    10
            );
        }

    }

}
