package net.sievert.jolcraft.events;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.custom.DwarfGuardEntity;

@EventBusSubscriber(modid = JolCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class JolCraftEvents {

    //Hurting living entities

    @SubscribeEvent
    public static void onLivingHurt(LivingDamageEvent.Pre event) {
        if(event.getEntity() instanceof DwarfGuardEntity dwarf && dwarf.isBlocking()) {
            // Cancel the damage
            event.setNewDamage(0F);
            // Suppress knockback and hurt effect
            dwarf.hurtMarked = false;
            // Optional: feedback (sound/particles)
            dwarf.level().playSound(null, dwarf.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.HOSTILE, 1.0F, 1.0F);
            dwarf.level().broadcastEntityEvent(dwarf, (byte) 29);
        }

    }
}
