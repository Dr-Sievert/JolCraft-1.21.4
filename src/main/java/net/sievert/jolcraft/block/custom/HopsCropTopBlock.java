package net.sievert.jolcraft.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sievert.jolcraft.data.JolCraftTags;

import java.util.function.Supplier;

public class HopsCropTopBlock extends HopsCropBottomBlock {

    public static final int MAX_AGE = 4;
    public static final IntegerProperty TOP_AGE = IntegerProperty.create("top_age", 0, MAX_AGE);

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(0, 0, 0, 16, 5, 16),
            Block.box(0, 0, 0, 16, 8, 16),
            Block.box(0, 0, 0, 16, 11, 16),
            Block.box(0, 0, 0, 16, 14, 16)
    };

    // Dummy suppliers, never called, but required for constructor

    public HopsCropTopBlock(Properties properties, Supplier<? extends ItemLike> seedItem) {
        super(properties, seedItem, () -> null); // top block doesn't grow further
        this.registerDefaultState(this.stateDefinition.any().setValue(TOP_AGE, 0));
    }


    // --- Shape and state handling ---

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TOP_AGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(TOP_AGE)];
    }

    // --- Crop logic overrides: these blocks do not grow, bonemeal, drop, or act as crops ---

    @Override
    public IntegerProperty getAgeProperty() {
        // Return TOP_AGE for this block
        return TOP_AGE;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        // Only tick to check for survival, not for growth
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState below = level.getBlockState(pos.below());
        // Must be hops bottom block AND bottom block must be mature enough (age ≥ 5)
        if (!below.is(JolCraftTags.Blocks.HOPS_BOTTOM) || below.getValue(HopsCropBottomBlock.AGE) < 5) {
            // The bottom is missing or not mature; destroy self
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(JolCraftTags.Blocks.HOPS_BOTTOM) && below.getValue(HopsCropBottomBlock.AGE) >= 5;
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            LevelReader level,
            ScheduledTickAccess ticker,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            RandomSource random
    ) {
        if (direction == Direction.DOWN &&
                (!neighborState.is(JolCraftTags.Blocks.HOPS_BOTTOM) || neighborState.getValue(HopsCropBottomBlock.AGE) < 5)) {
            // Schedule a tick so randomTick can destroy the block properly (drops, sounds, etc.)
            ticker.scheduleTick(pos, this, 1);
        }
        return super.updateShape(state, level, ticker, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, level, pos, newState, isMoving);
        // Only break bottom if this was a player break (manual, not logic/survival)
        if (newState.isAir() && !isMoving) {
            BlockPos below = pos.below();
            BlockState belowState = level.getBlockState(below);
            if (belowState.is(JolCraftTags.Blocks.HOPS_BOTTOM)) {
                level.destroyBlock(below, true);
            }
        }
    }


}
