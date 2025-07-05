package net.sievert.jolcraft.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DuskcapBlock extends JolCraftMushroomBlock{

    public DuskcapBlock(Properties properties) {
        super(properties);
    }

    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 12.0, 11.0);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public static final MapCodec<DuskcapBlock> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder.group(
                    BlockBehaviour.Properties.CODEC.fieldOf("properties").forGetter(block -> block.properties)
            ).apply(builder, DuskcapBlock::new)
    );

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }


    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = level.getBlockState(blockpos);
        net.neoforged.neoforge.common.util.TriState soilDecision = blockstate.canSustainPlant(level, blockpos, net.minecraft.core.Direction.UP, state);
        return blockstate.is(BlockTags.MUSHROOM_GROW_BLOCK)
                ? true
                : soilDecision.isDefault() ? (level.getRawBrightness(pos, 0) < 8 && this.mayPlaceOn(blockstate, level, blockpos)) : soilDecision.isTrue();
    }

}
