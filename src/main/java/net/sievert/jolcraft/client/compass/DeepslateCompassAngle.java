package net.sievert.jolcraft.client.compass;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class DeepslateCompassAngle implements RangeSelectItemModelProperty {
    public static final MapCodec<DeepslateCompassAngle> MAP_CODEC = DeepslateCompassAngleState.MAP_CODEC.xmap(
            DeepslateCompassAngle::new, p_388035_ -> p_388035_.state
    );
    private final DeepslateCompassAngleState state;

    public DeepslateCompassAngle(boolean wobble, DeepslateCompassAngleState.CompassTarget compassTarget) {
        this(new DeepslateCompassAngleState(wobble, compassTarget));
    }

    private DeepslateCompassAngle(DeepslateCompassAngleState state) {
        this.state = state;
    }

    @Override
    public float get(ItemStack p_387228_, @Nullable ClientLevel p_386952_, @Nullable LivingEntity p_386971_, int p_387210_) {
        return this.state.get(p_387228_, p_386952_, p_386971_, p_387210_);
    }

    @Override
    public MapCodec<DeepslateCompassAngle> type() {
        return MAP_CODEC;
    }
}

