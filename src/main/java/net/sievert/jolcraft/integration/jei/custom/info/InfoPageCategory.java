package net.sievert.jolcraft.integration.jei.custom.info;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.inputs.IJeiInputHandler;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.item.JolCraftItems;

import java.util.List;

public class InfoPageCategory implements IRecipeCategory<InfoPageRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "info_page");
    public static final IRecipeType<InfoPageRecipe> RECIPE_TYPE =
            IRecipeType.create(JolCraft.MOD_ID, "info_page", InfoPageRecipe.class);

    // This is not ideal if you want to scroll each recipe independently, but works for one at a time.
    private int scrollOffset = 0;

    private final IDrawable background;
    private final IDrawable icon;

    public InfoPageCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 100); // 100 tall for more space
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(JolCraftItems.DWARVEN_TOME.get()));
    }

    @Override
    public IRecipeType<InfoPageRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.jolcraft.info_page");
    }

    @Override
    public int getWidth() {
        return 150;
    }

    @Override
    public int getHeight() {
        return 100;
    }

    @Override
    public void draw(InfoPageRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        background.draw(graphics, 0, 0);

        // Scrollable text area
        int textStartY = 32;
        int textHeight = getHeight() - textStartY - 8;
        int lineHeight = 10;
        int maxLines = Math.max(1, textHeight / lineHeight);

        List<FormattedCharSequence> lines = Minecraft.getInstance().font.split(recipe.getContent(), getWidth() - 16);
        int totalLines = lines.size();
        int maxScroll = Math.max(0, totalLines - maxLines);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));

        for (int i = 0; i < Math.min(maxLines, totalLines - scrollOffset); ++i) {
            graphics.drawString(
                    Minecraft.getInstance().font,
                    lines.get(i + scrollOffset),
                    8,
                    textStartY + i * lineHeight,
                    0x444444,
                    false
            );
        }

        // Draw scroll bar if needed
        if (totalLines > maxLines) {
            int barX = getWidth() - 8;
            int barY = textStartY;
            int barWidth = 4;
            int barHeight = textHeight;

            graphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0x66BBBBBB);
            float ratio = maxLines / (float) totalLines;
            int thumbHeight = Math.max(12, Math.round(barHeight * ratio));
            int maxThumbMove = barHeight - thumbHeight;
            int thumbY = barY + (maxScroll == 0 ? 0 : Math.round(maxThumbMove * (scrollOffset / (float)maxScroll)));

            graphics.fill(barX, thumbY, barX + barWidth, thumbY + thumbHeight, 0xFF888888);
            graphics.fill(barX + 1, thumbY + 1, barX + barWidth - 1, thumbY + thumbHeight - 1, 0xFF666666);
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, InfoPageRecipe recipe, IFocusGroup focuses) {
        int textStartY = 32;
        int textHeight = getHeight() - textStartY - 8;
        int areaX = 8;
        int areaY = textStartY;
        int areaW = getWidth() - 16;
        int areaH = textHeight;

        builder.addInputHandler(new IJeiInputHandler() {
            @Override
            public net.minecraft.client.gui.navigation.ScreenRectangle getArea() {
                return new net.minecraft.client.gui.navigation.ScreenRectangle(areaX, areaY, areaW, areaH);
            }

            @Override
            public boolean handleMouseScrolled(double mouseX, double mouseY, double scrollDeltaX, double scrollDeltaY) {
                int sign = (int) Math.signum(scrollDeltaY);
                int maxLines = areaH / 10;
                int lines = Minecraft.getInstance().font.split(recipe.getContent(), getWidth() - 16).size();
                int maxScroll = Math.max(0, lines - maxLines);
                scrollOffset = Math.max(0, Math.min(scrollOffset - sign, maxScroll));
                return true;
            }
        });
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, InfoPageRecipe recipe, IFocusGroup focuses) {
        int slotX = (getWidth() - 18) / 2;
        int slotY = 8;
        // Add both input and output at the same position
        builder.addSlot(mezz.jei.api.recipe.RecipeIngredientRole.INPUT, slotX, slotY)
                .add(recipe.getFocusStack());
        builder.addSlot(mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT, slotX, slotY)
                .add(recipe.getFocusStack());
    }


    @Override
    public IDrawable getIcon() {
        return icon;
    }
}
