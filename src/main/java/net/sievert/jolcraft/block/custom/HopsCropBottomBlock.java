package net.sievert.jolcraft.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.util.TriState;
import net.sievert.jolcraft.data.JolCraftTags;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class HopsCropBottomBlock extends CropBlock {
    public static final int MAX_AGE = 9;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 9);
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(0, 0, 0, 16, 4, 16),
            Block.box(0, 0, 0, 16, 6, 16),
            Block.box(0, 0, 0, 16, 8, 16),
            Block.box(0, 0, 0, 16, 10, 16),
            Block.box(0, 0, 0, 16, 12, 16),
            Block.box(0, 0, 0, 16, 14, 16),
            Block.box(0, 0, 0, 16, 16, 16),
            Block.box(0, 0, 0, 16, 16, 16),
            Block.box(0, 0, 0, 16, 16, 16)
    };

    private final Supplier<? extends ItemLike> seedItem;
    @Nullable
    private final Supplier<? extends Block> topBlock;

    public HopsCropBottomBlock(Properties props, Supplier<? extends ItemLike> seedItem, Supplier<? extends Block> topBlock) {
        super(props);
        this.seedItem = seedItem;
        this.topBlock = topBlock;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPE_BY_AGE[state.getValue(AGE)];
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return seedItem.get();
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return !this.isMaxAge(state);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);

        //If it cant survive destroy it
        if (!this.canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true); // with drops
            return;
        }

        // Darkness check
        if (!hasSufficientDarkness(level, pos)) return;

        // Only allow growth if above is air or already a valid hops top block
        if (!(aboveState.isAir() || aboveState.is(JolCraftTags.Blocks.HOPS_TOP))) {
            return; // Blocked above, do not grow
        }

        if (age < MAX_AGE) {
            float growthSpeed = getGrowthSpeed(state, level, pos);
            if (random.nextInt((int) (25.0F / growthSpeed) + 1) == 0) {
                int newAge = age + 1;
                BlockState newState = this.getStateForAge(newAge);
                level.setBlock(pos, newState, 2);

                // TOP BLOCK HANDLING
                if (newAge >= 5) {
                    int topAge = Math.min(newAge - 5, HopsCropTopBlock.MAX_AGE);

                    if (aboveState.isAir() || !aboveState.is(JolCraftTags.Blocks.HOPS_TOP)) {
                        BlockState topState = topBlock.get().defaultBlockState().setValue(HopsCropTopBlock.TOP_AGE, topAge);
                        level.setBlock(above, topState, 2);
                    } else if (aboveState.getValue(HopsCropTopBlock.TOP_AGE) != topAge) {
                        // Always update the top's age to match
                        level.setBlock(above, aboveState.setValue(HopsCropTopBlock.TOP_AGE, topAge), 2);
                    }
                } else {
                    // If we're below stage 5, make sure the top block is gone
                    if (aboveState.is(JolCraftTags.Blocks.HOPS_TOP)) {
                        level.destroyBlock(above, true);
                    }
                }
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState soil = level.getBlockState(pos.below());
        boolean soilOk = false;

        // NeoForge/vanilla's TriState for plant support
        TriState soilDecision = soil.canSustainPlant(level, pos.below(), Direction.UP, state);
        if (!soilDecision.isDefault()) {
            soilOk = soilDecision.isTrue();
        } else if (soil.getBlock() instanceof FarmBlock) {
            soilOk = true;
        }

        boolean darkOk = hasSufficientDarkness(level, pos);

        // Registry name for debug (avoids NPE, but should always be present)
        ResourceLocation soilName = BuiltInRegistries.BLOCK.getKey(soil.getBlock());

        return soilOk && darkOk;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock() instanceof net.minecraft.world.level.block.FarmBlock;
    }

    public static boolean hasSufficientDarkness(LevelReader level, BlockPos pos) {
        return level.getRawBrightness(pos, 0) <= 8;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level instanceof ServerLevel && entity instanceof Ravager) {
            level.destroyBlock(pos, true, entity);
        }
        super.entityInside(state, level, pos, entity);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        // Only run this if the block is really being destroyed/replaced (not just aging)
        if (newState.getBlock() != this) {
            BlockPos abovePos = pos.above();
            BlockState topState = level.getBlockState(abovePos);
            if (topState.is(JolCraftTags.Blocks.HOPS_TOP)) {
                level.destroyBlock(abovePos, true);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
