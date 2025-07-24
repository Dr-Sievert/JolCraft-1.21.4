package net.sievert.jolcraft.event;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.sievert.jolcraft.network.client.data.ClientDeliriumData;
import net.sievert.jolcraft.screen.custom.strongbox.LockMenu;
import net.sievert.jolcraft.screen.custom.strongbox.LockScreen;


@EventBusSubscriber(modid = "jolcraft", bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class JolCraftClientGameEvents {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player != null && mc.screen instanceof LockScreen lockScreen) {
            LockMenu menu = lockScreen.getMenu();
            if (menu != null) {
                menu.tick();
            }
        }

        int prevMuffleTicks = ClientDeliriumData.getAndStorePreviousTicks();
        int currentMuffleTicks = ClientDeliriumData.getMuffleTicks();

        if (prevMuffleTicks == 0 && currentMuffleTicks > 0) {
            if (mc.player != null) {
                mc.player.playSound(SoundEvents.AMBIENT_CAVE.value(), 0.7F + mc.level.random.nextFloat() * 0.4F, 0.8F + mc.level.random.nextFloat() * 0.4F);
            }
        }
        ClientDeliriumData.tick();
    }

}

