package net.sievert.jolcraft.event;

import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.PlayLevelSoundEvent;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.network.client.data.MyClientDeliriumData;

@EventBusSubscriber(modid = JolCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class JolCraftSoundEvents {

    @SubscribeEvent
    public static void onPlaySound(PlayLevelSoundEvent event) {
        if (MyClientDeliriumData.getMuffleTicks() > 0) {
            var soundHolder = event.getSound();
            if (soundHolder == null) return;

            var soundKeyOpt = soundHolder.unwrapKey();
            if (soundKeyOpt.isEmpty()) return;
            var soundLocation = soundKeyOpt.get().location();

            var caveKeyOpt = SoundEvents.AMBIENT_CAVE.unwrapKey();
            if (caveKeyOpt.isEmpty()) return;
            var caveLocation = caveKeyOpt.get().location();

            if (soundLocation.equals(caveLocation)) return;

            event.setNewVolume(event.getOriginalVolume() * 0.3F);
        }
    }

}