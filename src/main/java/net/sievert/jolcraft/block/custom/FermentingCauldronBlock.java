package net.sievert.jolcraft.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.sievert.jolcraft.block.entity.FermentingCauldronBlockEntity;
import net.sievert.jolcraft.block.entity.JolCraftBlockEntities;
import net.sievert.jolcraft.item.JolCraftItems;
import org.jetbrains.annotations.Nullable;

public class FermentingCauldronBlock extends AbstractCauldronBlock implements EntityBlock {

    private static final int TICK_DELAY = 40;

    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 1, 3);
    public static final EnumProperty<FermentingStage> STAGE = EnumProperty.create("stage", FermentingStage.class);
    public static final BooleanProperty MALTED = BooleanProperty.create("malted");
    public static final IntegerProperty FERMENTATION_PROGRESS = IntegerProperty.create("fermentation_progress", 0, 100);

    public static final MapCodec<FermentingCauldronBlock> CODEC = simpleCodec(FermentingCauldronBlock::new);

    public FermentingCauldronBlock(Properties properties) {
        super(properties, CauldronInteraction.EMPTY);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(LEVEL, 3)
                        .setValue(STAGE, FermentingStage.YEAST_FERMENTING) // default stage
                        .setValue(FERMENTATION_PROGRESS, 0)
                        .setValue(MALTED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LEVEL, STAGE, FERMENTATION_PROGRESS, MALTED);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            // Yeast bottle extraction logic (for yeast ready stage)
            if (stack.is(Items.GLASS_BOTTLE) && state.getValue(FermentingCauldronBlock.STAGE) == FermentingStage.YEAST_READY) {
                int currentLevel = state.getValue(FermentingCauldronBlock.LEVEL);

                ItemStack yeastBottle = new ItemStack(JolCraftItems.YEAST.get());

                if (!player.isCreative()) {
                    stack.shrink(1);
                }

                if (!player.getInventory().add(yeastBottle)) {
                    player.drop(yeastBottle, false);
                }

                int newLevel = currentLevel - 1;
                BlockState newState = newLevel > 0 ? state.setValue(FermentingCauldronBlock.LEVEL, newLevel)
                        : Blocks.CAULDRON.defaultBlockState();

                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 0.8F, 1.0F);

                return InteractionResult.SUCCESS;
            }

            // Dwarven Brew extraction logic (for brew ready stage)
            if (stack.is(JolCraftItems.GLASS_MUG.get()) && state.getValue(FermentingCauldronBlock.STAGE) == FermentingStage.BREW_READY) {
                int currentLevel = state.getValue(FermentingCauldronBlock.LEVEL);

                ItemStack dwarvenBrew = new ItemStack(JolCraftItems.DWARVEN_BREW.get());

                if (!player.isCreative()) {
                    stack.shrink(1);
                }

                if (!player.getInventory().add(dwarvenBrew)) {
                    player.drop(dwarvenBrew, false);
                }

                int newLevel = currentLevel - 1;
                BlockState newState = newLevel > 0 ? state.setValue(FermentingCauldronBlock.LEVEL, newLevel)
                        : Blocks.CAULDRON.defaultBlockState();

                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 0.8F, 0.5F);

                return InteractionResult.SUCCESS;
            }

            // TODO: Add new interactions here for malted stage if needed.
        }
        return InteractionResult.PASS;
    }


    @Nullable
    @Override
    public FermentingCauldronBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FermentingCauldronBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (type == JolCraftBlockEntities.FERMENTING_CAULDRON.get()) {
            return (lvl, pos, st, blockEntity) -> {
                if (blockEntity instanceof FermentingCauldronBlockEntity fermenting) {
                    fermenting.tick();
                }
            };
        }
        return null;
    }

    @Override
    public boolean isFull(BlockState state) {
        return state.getValue(LEVEL) == 3;
    }

    @Override
    protected MapCodec<? extends AbstractCauldronBlock> codec() {
        return CODEC;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isClientSide()) return;

        FermentingStage stage = state.getValue(STAGE);
        // Tick only if fermenting yeast or brew
        if (stage == FermentingStage.YEAST_FERMENTING || stage == FermentingStage.BREW_FERMENTING) {
            level.scheduleTick(pos, this, TICK_DELAY);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        FermentingStage stage = state.getValue(STAGE);
        if (!level.isClientSide() && (stage == FermentingStage.YEAST_FERMENTING || stage == FermentingStage.BREW_FERMENTING)) {
            level.scheduleTick(pos, this, 20);
        }
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        // Return the vanilla cauldron item stack when middle-clicking
        return new ItemStack(Items.CAULDRON); // Return vanilla cauldron instead of fermenting cauldron
    }

}
