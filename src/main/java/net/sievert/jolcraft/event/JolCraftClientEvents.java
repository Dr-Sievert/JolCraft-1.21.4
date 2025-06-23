package net.sievert.jolcraft.event;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.block.JolCraftBlocks;
import net.sievert.jolcraft.block.custom.FermentingCauldronBlock;
import net.sievert.jolcraft.block.custom.FermentingStage;
import net.sievert.jolcraft.block.entity.FermentingCauldronBlockEntity;

@EventBusSubscriber(modid = JolCraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class JolCraftClientEvents {

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, world, pos, tintIndex) -> {
            if (tintIndex != 0) {
                return 0xFFFFFFFF; // default white for other tint indexes
            }

            FermentingStage stage = state.getValue(FermentingCauldronBlock.STAGE);

            // Handle finished states with direct color
            if (stage == FermentingStage.YEAST_READY) {
                return 0xFF40B14A; // dark green for yeast ready
            }
            if (stage == FermentingStage.BREW_READY) {
                return 0xFFA500; // orange for brew ready (adjusted)
            }

            // Determine progress ratio from blockstate or blockentity
            float ratio = 0f;
            if (state.hasProperty(FermentingCauldronBlock.FERMENTATION_PROGRESS)) {
                int progress = state.getValue(FermentingCauldronBlock.FERMENTATION_PROGRESS);
                ratio = clamp01(progress / 100f);
            } else if (world != null && pos != null) {
                BlockEntity be = world.getBlockEntity(pos);
                if (be instanceof FermentingCauldronBlockEntity fermentingBE) {
                    ratio = clamp01(fermentingBE.getFermentationProgressRatio());
                }
            }

            // Define start and end colors per fermenting type
            int startColor;
            int endColor;

            if (stage == FermentingStage.YEAST_FERMENTING) {
                startColor = 0xFF8EE8AA; // watery light green start for yeast
                endColor = 0xFF40B14A;   // dark green yeast finish
            } else if (stage == FermentingStage.BREW_FERMENTING) {
                startColor = 0xFF40B14A; // yeast green for fermenting brew start
                endColor = 0xFFA500;     // golden orange for brew finish
            } else if (stage == FermentingStage.MALTED) {
                // Optional: fixed color or no gradient for malted state
                return 0xFFB16A1D; // brown/orange color for malted
            } else {
                // fallback to watery green
                startColor = 0xFF8EE8AA;
                endColor = 0xFF40B14A;
            }

            return blendColors(startColor, endColor, ratio);
        }, JolCraftBlocks.FERMENTING_CAULDRON.get());
    }

    private static float clamp01(float value) {
        return Math.min(1.0f, Math.max(0.0f, value));
    }

    private static int blendColors(int color1, int color2, float ratio) {
        ratio = clamp01(ratio);
        int a1 = (color1 >> 24) & 0xFF;
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int a2 = (color2 >> 24) & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int a = (int)(a1 + (a2 - a1) * ratio);
        int r = (int)(r1 + (r2 - r1) * ratio);
        int g = (int)(g1 + (g2 - g1) * ratio);
        int b = (int)(b1 + (b2 - b1) * ratio);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
