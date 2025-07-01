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
import net.sievert.jolcraft.block.custom.HopsType;
import net.sievert.jolcraft.block.entity.FermentingCauldronBlockEntity;

@EventBusSubscriber(modid = JolCraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class JolCraftClientEvents {

    private static final int YEAST_FERMENTING_START = 0xFF8EE8AA; // Light green
    private static final int YEAST_FERMENTING_END = 0xFF40B14A; // Dark green
    private static final int MALT_COLOR = 0xFFB16A1D; // Brown/orange malt
    private static final int BREW_COLOR = 0xFF9A652B; // Final orange brew
    private static final int BLEND_COLOR = 0xFFA66824; // Malt and brew blend

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, world, pos, tintIndex) -> {
            if (tintIndex != 0) return 0xFFFFFFFF; // Default fallback color

            // Retrieve the current stage of fermentation
            FermentingStage stage = state.getValue(FermentingCauldronBlock.STAGE);

            // Retrieve the current hop type from the block state
            HopsType hopsType = state.getValue(FermentingCauldronBlock.HOPS_TYPE);

            // Color blending logic for fermentation stages
            float ratio = 0f;
            if (state.hasProperty(FermentingCauldronBlock.FERMENTATION_PROGRESS)) {
                int progress = state.getValue(FermentingCauldronBlock.FERMENTATION_PROGRESS);
                ratio = clamp01(progress / 9f);
            }


            // If the cauldron is in the 'MALTED' stage, return the malt color directly without blending
            if (stage == FermentingStage.MALTED) {
                return MALT_COLOR;  // Just the malt color when in MALTED stage
            }


            if (state.getValue(FermentingCauldronBlock.STAGE) != FermentingStage.HOPS){
                switch (stage) {
                    case YEAST_FERMENTING:
                        return blendColors(YEAST_FERMENTING_START, YEAST_FERMENTING_END, ratio); // Blend yeast fermentation
                    case BREW_FERMENTING:
                        return blendColors(BLEND_COLOR, BREW_COLOR, ratio); // Blending between hop and final brew color
                    case YEAST_READY:
                        return YEAST_FERMENTING_END; // Final yeast color (dark green)
                    case BREW_READY:
                        return BREW_COLOR; // Final brew color when brewing is completed
                    default:
                        return YEAST_FERMENTING_START; // Default color for fallback
                }
            }

            // Check for specific hop types and return corresponding color
            switch (hopsType) {
                case ASGARNIAN:
                    return 0xFF6B5352; // Color for Asgarnian hop
                case DUSKHOLD:
                    return 0xFF5F5864; // Color for Duskhold hop
                case KRANDONIAN:
                    return 0xFF526B69; // Color for Krandonian hop
                case YANILLIAN:
                    return 0xFF2B4318; // Color for Yanillian hop
                default:
                    return 0xFFB16A1D; // Default hop color
            }

        }, JolCraftBlocks.FERMENTING_CAULDRON.get());
    }

    // Helper method to clamp a float value between 0 and 1
    private static float clamp01(float value) {
        return Math.min(1.0f, Math.max(0.0f, value));
    }

    // Helper method to blend two colors based on the given ratio
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

        int a = (int) (a1 + (a2 - a1) * ratio);
        int r = (int) (r1 + (r2 - r1) * ratio);
        int g = (int) (g1 + (g2 - g1) * ratio);
        int b = (int) (b1 + (b2 - b1) * ratio);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}