package net.sievert.jolcraft.events;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.custom.DwarfGuardEntity;

@EventBusSubscriber(modid = JolCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class JolCraftEvents {

    //Hurting living entities
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof DwarfGuardEntity dwarf && dwarf.isBlockCooldownReady() && event.getSource().getEntity() instanceof Monster) {
            dwarf.markForBlocking();
            event.setNewDamage(0.2F);
        }
    }

}
