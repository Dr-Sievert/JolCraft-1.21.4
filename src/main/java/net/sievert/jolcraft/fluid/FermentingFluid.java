package net.sievert.jolcraft.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.sievert.jolcraft.block.JolCraftBlocks;
import net.sievert.jolcraft.block.custom.FermentingCauldronBlock;

import javax.annotation.Nullable;

public abstract class FermentingFluid extends FlowingFluid {

    // Only source fluid; no flowing variant
    public static class Source extends FermentingFluid {
        @Override
        public boolean isSource(FluidState state) {
            return true;
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;  // max amount for source block
        }

        @Override
        public Fluid getFlowing() {
            return this; // No flowing fluid, so return self
        }

        @Override
        public Fluid getSource() {
            return this;
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            // No LEVEL property because no flowing state
        }
    }

    @Override
    protected int getSlopeFindDistance(LevelReader level) {
        return 0; // No flowing, so zero distance
    }

    @Override
    protected int getDropOff(LevelReader level) {
        return 0; // No drop-off without flowing
    }

    @Override
    public boolean canConvertToSource(ServerLevel level) {
        return false; // No conversion since no flowing state
    }

    @Override
    public void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        // No special behavior before destroying block
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F; // Similar to water/lava resistance
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == this; // Only one fluid type here
    }

    @Nullable
    @Override
    public Item getBucket() {
        return null; // No bucket for this fluid
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        return false; // No replacement behavior needed
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return 0; // No ticks needed, static fluid
    }

    @Override
    public BlockState createLegacyBlock(FluidState state) {
        return JolCraftBlocks.FERMENTING_FLUID.get().defaultBlockState();
    }

    @Override
    protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
        super.createFluidStateDefinition(builder);
        builder.add(LEVEL); // <-- ADD THIS!
    }

    @Override
    protected void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
        // Add some bubbles randomly for effect
        if (random.nextInt(10) == 0) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + 1.0;
            double z = pos.getZ() + random.nextDouble();
            level.addParticle(ParticleTypes.BUBBLE, x, y, z, 0, 0, 0);
        }
    }
}
