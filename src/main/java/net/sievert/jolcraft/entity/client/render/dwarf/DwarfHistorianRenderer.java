package net.sievert.jolcraft.entity.client.render.dwarf;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.client.dwarf.DwarfBeardLayer;
import net.sievert.jolcraft.entity.client.dwarf.DwarfEyeLayer;
import net.sievert.jolcraft.entity.client.model.dwarf.DwarfHistorianModel;
import net.sievert.jolcraft.entity.client.dwarf.DwarfRenderState;
import net.sievert.jolcraft.entity.custom.dwarf.DwarfHistorianEntity;


public class DwarfHistorianRenderer extends DwarfRenderer<DwarfHistorianEntity> {

    public DwarfHistorianRenderer(EntityRendererProvider.Context context) {
        super(context, new DwarfHistorianModel(context.bakeLayer(DwarfHistorianModel.LAYER_LOCATION)));
        this.addLayer(new DwarfBeardLayer(this));
        this.addLayer(new DwarfEyeLayer(this));

    }

    @Override
    public ResourceLocation getTextureLocation(DwarfRenderState entity) {
        return ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/entity/dwarf/dwarf_historian.png");
    }

    @Override
    public void render(DwarfRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(renderState, poseStack, bufferSource, packedLight);

    }

    @Override
    public DwarfRenderState createRenderState() {
        return new DwarfRenderState();
    }

    @Override
    public void extractRenderState(DwarfHistorianEntity entity, DwarfRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
    }

}

