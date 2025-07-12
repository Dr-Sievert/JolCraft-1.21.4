package net.sievert.jolcraft.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class RotatedPillarExperienceBlock extends DropExperienceBlock {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    // Store XP range in our own field for codec access
    private final IntProvider myXpRange;

    public static final MapCodec<RotatedPillarExperienceBlock> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder.group(
                    IntProvider.codec(0, 10).fieldOf("experience").forGetter(b -> b.myXpRange), // Use your own field!
                    propertiesCodec()
            ).apply(builder, RotatedPillarExperienceBlock::new)
    );

    @Override
    public MapCodec<? extends RotatedPillarExperienceBlock> codec() {
        return CODEC;
    }

    public RotatedPillarExperienceBlock(IntProvider xpRange, BlockBehaviour.Properties properties) {
        super(xpRange, properties);
        this.myXpRange = xpRange; // Save for codec
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.Y));
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return rotatePillar(state, rot);
    }

    public static BlockState rotatePillar(BlockState state, Rotation rotation) {
        switch (rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch ((Direction.Axis)state.getValue(AXIS)) {
                    case X: return state.setValue(AXIS, Direction.Axis.Z);
                    case Z: return state.setValue(AXIS, Direction.Axis.X);
                    default: return state;
                }
            default:
                return state;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AXIS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
    }
}
