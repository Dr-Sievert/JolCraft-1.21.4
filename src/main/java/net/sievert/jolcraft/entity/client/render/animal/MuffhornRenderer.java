package net.sievert.jolcraft.entity.client.render.animal;

import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.client.model.animal.MuffhornModel;
import net.sievert.jolcraft.entity.custom.animal.MuffhornEntity;

@OnlyIn(Dist.CLIENT)
public class MuffhornRenderer extends AgeableMobRenderer<MuffhornEntity, LivingEntityRenderState, MuffhornModel> {

    public MuffhornRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new MuffhornModel(context.bakeLayer(MuffhornModel.LAYER_LOCATION)),         // adult model
                new MuffhornModel(context.bakeLayer(MuffhornModel.BABY_LAYER_LOCATION)),    // baby model
                0.6f // scale factor (adjust as needed)
        );
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/entity/animal/muffhorn.png");
    }

    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    public void extractRenderState(MuffhornEntity p_364800_, LivingEntityRenderState p_363914_, float p_360614_) {
        super.extractRenderState(p_364800_, p_363914_, p_360614_);
    }
}
