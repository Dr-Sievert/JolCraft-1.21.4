package net.sievert.jolcraft.block.custom.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.sievert.jolcraft.block.JolCraftBlocks;

import javax.annotation.Nullable;

public class VerdantSoilBlock extends Block {
    public VerdantSoilBlock(Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility ability, boolean simulate) {
        // Only respond to hoe tilling action
        if (ability == ItemAbilities.HOE_TILL) {
            // Optionally, check for air above
            if (context.getLevel().getBlockState(context.getClickedPos().above()).isAir()) {
                return JolCraftBlocks.VERDANT_FARMLAND.get().defaultBlockState();
            }
        }
        return null; // No modification for other tools or cases
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (random.nextInt(100) == 0) {
            level.addParticle(
                    ParticleTypes.HAPPY_VILLAGER,
                    (double)pos.getX() + random.nextDouble(),
                    (double)pos.getY() + 1.1,
                    (double)pos.getZ() + random.nextDouble(),
                    0.0,
                    0.0,
                    0.0
            );
        }
    }


}
