package net.sievert.jolcraft.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sievert.jolcraft.block.entity.JolCraftBlockEntities;
import net.sievert.jolcraft.block.entity.custom.StrongboxBlockEntity;
import net.sievert.jolcraft.item.JolCraftItems;

import javax.annotation.Nullable;

public class StrongboxBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final VoxelShape STRONGBOX_SHAPE = Block.box(1, 0, 3, 15, 10, 13); // [minX, minY, minZ, maxX, maxY, maxZ]
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty NATURAL = BooleanProperty.create("natural");

    // --- Constructor ---
    public StrongboxBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(WATERLOGGED, Boolean.valueOf(false))
                        .setValue(NATURAL, Boolean.valueOf(false))
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return rotateShape(STRONGBOX_SHAPE, facing);
    }

    // Utility to rotate VoxelShape (XZ axes only)
    private static VoxelShape rotateShape(VoxelShape shape, Direction facing) {
        switch (facing) {
            case SOUTH:
                return shape; // Default
            case WEST:
                return Block.box(
                        3, 0, 1, 13, 10, 15
                ); // Rotate manually or use a library to rotate shape
            case NORTH:
                return Block.box(
                        1, 0, 3, 15, 10, 13
                );
            case EAST:
                return Block.box(
                        3, 0, 1, 13, 10, 15
                );
            default:
                return shape;
        }
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, WATERLOGGED, NATURAL);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        boolean waterlogged = fluidState.getType() == Fluids.WATER;

        return this.defaultBlockState()
                .setValue(FACING, direction)
                .setValue(WATERLOGGED, waterlogged);
    }


    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            LevelReader level,
            ScheduledTickAccess scheduledTick,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            RandomSource random
    ) {
        if (state.getValue(WATERLOGGED)) {
            scheduledTick.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, scheduledTick, pos, direction, neighborPos, neighborState, random);
    }


    public static final MapCodec<StrongboxBlock> CODEC = simpleCodec(StrongboxBlock::new);

    @Override
    protected MapCodec<? extends StrongboxBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StrongboxBlockEntity(pos, state); // You’ll define StrongboxBlockEntity separately
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof StrongboxBlockEntity strongbox) {
                Containers.dropContents(level, pos, strongbox);
                // If you want the Strongbox to *destroy* its contents on break, skip the dropContents line.
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide()) {
            if (state.getValue(NATURAL)) {
                level.setBlock(pos, state.setValue(NATURAL, false), 3);
            }
            MenuProvider menuProvider = this.getMenuProvider(state, level, pos);
            if (menuProvider != null) {
                player.openMenu(menuProvider, pos); // Passes the position to the client automatically
            }
        }
        return InteractionResult.SUCCESS;
    }


    @Override
    @Nullable
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        return (be instanceof MenuProvider menuProvider) ? menuProvider : null;
    }
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return createTickerHelper(type, JolCraftBlockEntities.STRONGBOX.get(), StrongboxBlockEntity::lidAnimateTick);
        } else {
            return createTickerHelper(type, JolCraftBlockEntities.STRONGBOX.get(), (serverLevel, pos, blockState, be) -> {
                if (be instanceof StrongboxBlockEntity strongbox) {
                    strongbox.recheckOpen();
                }
            });
        }
    }



    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof StrongboxBlockEntity strongbox) {
            return AbstractContainerMenu.getRedstoneSignalFromContainer(strongbox);
        }
        return 0;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(JolCraftItems.STRONGBOX_ITEM.get());
    }

    public static boolean isNatural(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof StrongboxBlock && state.getValue(NATURAL);
    }


}
