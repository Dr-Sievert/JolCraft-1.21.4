package net.sievert.jolcraft.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sievert.jolcraft.block.entity.JolCraftBlockEntities;
import net.sievert.jolcraft.block.entity.custom.StrongboxBlockEntity;
import net.sievert.jolcraft.component.JolCraftDataComponents;
import net.sievert.jolcraft.item.JolCraftItems;
import net.sievert.jolcraft.screen.custom.LockMenu;

import javax.annotation.Nullable;
import java.util.List;

public class StrongboxBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final VoxelShape STRONGBOX_SHAPE = Block.box(1, 0, 3, 15, 10, 13); // [minX, minY, minZ, maxX, maxY, maxZ]
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

    // --- Constructor ---
    public StrongboxBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(WATERLOGGED, Boolean.valueOf(false))
                        .setValue(LOCKED, Boolean.valueOf(false))
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
        builder.add(FACING, WATERLOGGED, LOCKED);
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
            if (be instanceof StrongboxBlockEntity strongbox && level instanceof ServerLevel serverLevel) {
                Player breakingPlayer = serverLevel.getNearestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 8, false);
                ItemStack tool = breakingPlayer != null ? breakingPlayer.getMainHandItem() : ItemStack.EMPTY;

                serverLevel.registryAccess().lookup(Registries.ENCHANTMENT).ifPresent(lookup -> {
                    var silkTouchHolder = lookup.get(Enchantments.SILK_TOUCH);
                    boolean hasSilkTouch = silkTouchHolder
                            .map(enchantment -> EnchantmentHelper.getItemEnchantmentLevel(enchantment, tool) > 0)
                            .orElse(false);

                    if (hasSilkTouch) {
                        strongbox.setSilkTouched(true); // Mark it as silk-touched

                        // Create a drop ItemStack for the Strongbox item
                        ItemStack drop = new ItemStack(JolCraftItems.STRONGBOX_ITEM.get());

                        // Store the contents of the strongbox in the ItemStack's container data component
                        drop.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(strongbox.getItems()));

                        // Store loot table and seed using the custom DataComponentType
                        ResourceKey<LootTable> lootTable = strongbox.getLootTable();  // Get the loot table as a ResourceKey
                        Long lootTableSeed = strongbox.getLootTableSeed();  // Get the seed (may still be 0L)

                        // Extract just the path from the ResourceKey (e.g., "jolcraft:strongbox/strongbox")
                        String lootTablePath = lootTable != null ? lootTable.location().toString() : "";  // Get path part

                        // Store the loot table if it's not null or empty
                        if (!lootTablePath.isEmpty()) {
                            drop.set(JolCraftDataComponents.LOOT_TABLE, lootTablePath);  // Store loot table path as a String
                        }

                        // Only set the lootTableSeed if it's not the default (0L)
                        if (lootTableSeed != 0L) {
                            drop.set(JolCraftDataComponents.LOOT_SEED, String.valueOf(lootTableSeed));  // Store loot table seed as a String
                        }

                        if (state.getValue(StrongboxBlock.LOCKED)) {
                            drop.set(JolCraftDataComponents.LOCKED, true);  // Add LOCKED component to the dropped item
                        }

                        // Drop the Strongbox item with loot table information attached
                        if(!breakingPlayer.isCreative()){
                            Block.popResource(level, pos, drop);
                        }

                        }
                         else {
                        // Default behavior when Silk Touch is not used
                        if (strongbox.getLootTable() != null && state.getValue(LOCKED)){
                            if(!breakingPlayer.isCreative()){
                                Block.popResource(level, pos, new ItemStack(JolCraftItems.STRONGBOX_ITEM.get())); // Drop the Strongbox item (no loot)
                            }
                        } else {
                            if(!breakingPlayer.isCreative()){
                                // If no loot table, drop the Strongbox item and contents
                                Containers.dropContents(level, pos, strongbox);  // Drop the contents of the Strongbox
                                Block.popResource(level, pos, new ItemStack(JolCraftItems.STRONGBOX_ITEM.get())); // Drop the Strongbox item
                            }

                        }
                    }
                });
            }

            // Ensure the default removal behavior is executed
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (!level.isClientSide && level.getBlockEntity(pos) instanceof StrongboxBlockEntity be) {

            // Restore the contents from the ItemStack if present
            if (stack.has(DataComponents.CONTAINER)) {
                ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
                NonNullList<ItemStack> items = NonNullList.withSize(be.getContainerSize(), ItemStack.EMPTY);
                contents.copyInto(items);
                be.setItems(items);
            }

            // Handle LOCKED state separately
            if (stack.has(JolCraftDataComponents.LOCKED)) {
                boolean isLocked = stack.get(JolCraftDataComponents.LOCKED);
                level.setBlock(pos, state.setValue(StrongboxBlock.LOCKED, isLocked), 3);  // Set the LOCKED state of the block
            }

            // Ensure LootTable is set, but not nullified when locked
            if (stack.has(JolCraftDataComponents.LOOT_TABLE)) {
                String lootTableString = stack.get(JolCraftDataComponents.LOOT_TABLE);

                // Create the ResourceKey<LootTable> from the lootTableString
                ResourceKey<LootTable> lootTableKey = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.tryParse(lootTableString));

                // Set the loot table in the block entity
                be.setLootTable(lootTableKey, be.getLootTableSeed());  // Use the current lootTableSeed

            }

            // Handle Loot Table Seed separately
            if (stack.has(JolCraftDataComponents.LOOT_SEED)) {
                String lootTableSeedString = stack.get(JolCraftDataComponents.LOOT_SEED);

                // Convert the loot table seed string into a long
                long lootTableSeed = Long.parseLong(lootTableSeedString);

                // Set the loot table seed in the block entity
                be.setLootTableSeed(lootTableSeed);
            }

            // Force a block update to ensure the loot table is applied correctly on the client
            level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);

            // Mark the block entity as changed to sync it with the client
            be.setChanged();  // This ensures the block entity is synced with the client
        }
    }


    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.CONSUME; // Only handle this on the server side

        // Check if the player is holding coal or charcoal first
        boolean isCoal = stack.is(Items.COAL) || stack.is(Items.CHARCOAL);

        //FOR TESTING FOR NOW!
        // If the player is holding coal/charcoal and the Strongbox is not locked, lock the Strongbox and show a message
        if (isCoal && !state.getValue(LOCKED)) {
            // Lock the Strongbox
            level.setBlock(pos, state.setValue(LOCKED, true), 3);
            // Display a message to the player
            player.displayClientMessage(
                    Component.translatable("tooltip.jolcraft.strongbox.locked").withStyle(ChatFormatting.GRAY), true
            );
            return InteractionResult.SUCCESS; // Consume the interaction to prevent further actions
        }

        // If the stack is empty (no item), just open the menu and unlock the Strongbox if it's locked
        if (!isCoal) {
            // Open the menu
            MenuProvider menuProvider = this.getMenuProvider(state, level, pos);
            if (menuProvider != null) {
                player.openMenu(menuProvider, pos); // Passes the position to the client automatically
            }

            // Unlock the Strongbox if it is locked
//            if (state.getValue(LOCKED)) {
//                level.setBlock(pos, state.setValue(LOCKED, false), 3); // Set LOCKED to false (unlock it)
//            }

            return InteractionResult.SUCCESS; // Return SUCCESS to indicate the interaction was handled
        }

        // Return PASS if it's neither coal/charcoal nor an empty hand
        return InteractionResult.PASS;
    }


    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        // Return an empty list if the drops are already being handled by `onRemove`
        return List.of();  // Prevent default drops if handled in onRemove
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
            // Keep the lid animation logic for the client side
            return createTickerHelper(type, JolCraftBlockEntities.STRONGBOX.get(), StrongboxBlockEntity::lidAnimateTick);
        } else {
            // Keep the recheckOpen logic for the server side, and also call the tick method to update progress
            return createTickerHelper(type, JolCraftBlockEntities.STRONGBOX.get(), (serverLevel, pos, blockState, be) -> {
                if (be instanceof StrongboxBlockEntity strongbox) {
                    // Recheck if the strongbox is open
                    strongbox.recheckOpen();
                    // Call the tick method to update lockpick progress
                    strongbox.serverTick((ServerLevel) level, pos, state, strongbox);  // Update the lockpick progress on the server side
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
        ItemStack stack = new ItemStack(JolCraftItems.STRONGBOX_ITEM.get());

        if (includeData) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof StrongboxBlockEntity strongbox) {
                stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(strongbox.getItems()));
            }
        }

        return stack;
    }



}
