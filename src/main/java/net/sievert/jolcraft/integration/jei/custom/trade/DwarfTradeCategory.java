package net.sievert.jolcraft.integration.jei.custom.trade;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.sievert.jolcraft.JolCraft;
import net.sievert.jolcraft.entity.JolCraftEntities;
import net.sievert.jolcraft.entity.custom.dwarf.*;
import net.sievert.jolcraft.item.JolCraftItems;

public class DwarfTradeCategory implements IRecipeCategory<DwarfTradeRecipe> {
    public static final IRecipeType<DwarfTradeRecipe> RECIPE_TYPE = IRecipeType.create(JolCraft.MOD_ID, "dwarf_trades", DwarfTradeRecipe.class);
    private static final java.util.Map<String, LivingEntity> DWARF_RENDER_CACHE = new java.util.HashMap<>();
    private final IDrawable background;
    private final IDrawable icon;
    private static final ResourceLocation ARROW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/gui/jei/sprites/arrow_right.png");
    private static final ResourceLocation PLUS_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/gui/jei/sprites/plus.png");


    public DwarfTradeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 60);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(JolCraftItems.GOLD_COIN.get()));
    }

    @Override
    public IRecipeType<DwarfTradeRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.jolcraft.dwarf_trades");
    }

    @Override
    public int getWidth() {
        return 150;
    }

    @Override
    public int getHeight() {
        return 60;
    }

    @Override
    public void draw(DwarfTradeRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        background.draw(graphics, 0, 0);

        // === Profession and level merged, e.g. "Master Dwarf" ===
        int textY = 12;        // vertical position (tweak here)
        int offsetX = -19;       // horizontal offset (tweak here)

        // Get level string (e.g., "Master")
        int level = recipe.getLevel();
        String levelKey = "merchant.level." + level;
        String levelStr = Component.translatable(levelKey).getString();

        // Merge with profession, e.g. "Master Dwarf"
        String profText = recipe.getProfession();
        String displayStr = levelStr + " " + profText;

        // Center the full string
        int x = ((getWidth() - Minecraft.getInstance().font.width(displayStr)) / 2) + offsetX;

        // Draw merged string
        graphics.drawString(Minecraft.getInstance().font, displayStr, x, textY, 0x888888, false);

        // Input slots: always A, maybe B
        boolean hasB = recipe.getInputB() != null && !recipe.getInputB().isEmpty();
        int slotAX = 2, slotBX = 24, slotAY = 36, slotBY = 36;
        int plusX = 16, plusY = 38;
        int arrowX = hasB ? 45 : 21;
        int arrowY = 35;
        int outputX = hasB ? 66 : 44;
        int outputY = 36;

        // Draw plus if two inputs
        if (hasB) {
            graphics.blit(
                    RenderType.GUI_TEXTURED,
                    PLUS_TEXTURE,
                    plusX, plusY,
                    0, 0,
                    12, 12,
                    12, 12
            );
        }

        // Draw arrow
        graphics.blit(
                RenderType.GUI_TEXTURED,
                ARROW_TEXTURE,
                arrowX, arrowY,
                0, 0,
                22, 18,
                22, 18
        );

        LivingEntity dwarf = getOrCreateDwarf(recipe);
        if (dwarf != null) {
            int offsetY = -16;
            int minX = 100, minY = 10 + offsetY, maxX = 160, maxY = 90 + offsetY;
            int scale = 32;
            float yOffset = 0.0F;

            InventoryScreen.renderEntityInInventoryFollowsMouse(
                    graphics,
                    minX, minY, maxX, maxY,
                    scale,
                    yOffset,
                    (float) mouseX, (float) mouseY,
                    dwarf
            );
        }

    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DwarfTradeRecipe recipe, IFocusGroup focuses) {
        // Profession egg slot (always first)
        builder.addSlot(RecipeIngredientRole.INPUT, 95, 42)
                .add(recipe.getSpawnEgg());
        if(recipe.getInputA().is(JolCraftItems.GOLD_COIN.get())){
            // Input A (coin pouch or gold coin)
            builder.addSlot(RecipeIngredientRole.INPUT, 2, 36).add(new ItemStack(JolCraftItems.GOLD_COIN.get())).add(new ItemStack(JolCraftItems.COIN_POUCH.get()));
        }else{
            builder.addSlot(RecipeIngredientRole.INPUT, 2, 36).add(recipe.getInputA());
        }

        // Input B if present (for recipes requiring two inputs)
        if (recipe.getInputB() != null && !recipe.getInputB().isEmpty()) {
            if(recipe.getInputB().is(JolCraftItems.GOLD_COIN.get())){
                // Input B (coin pouch or gold coin)
                builder.addSlot(RecipeIngredientRole.INPUT, 27, 36).add(new ItemStack(JolCraftItems.GOLD_COIN.get())).add(new ItemStack(JolCraftItems.COIN_POUCH.get()));
            }else{
                builder.addSlot(RecipeIngredientRole.INPUT, 27, 36).add(recipe.getInputB());
            }
            // Output farther right for double input
            builder.addSlot(RecipeIngredientRole.OUTPUT, 68, 36).add(recipe.getOutput());
        } else {
            // Output closer for single input
            builder.addSlot(RecipeIngredientRole.OUTPUT, 45, 36).add(recipe.getOutput());
        }
    }


    public static LivingEntity getOrCreateDwarf(DwarfTradeRecipe recipe) {
        String profession = recipe.getProfession();
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || profession == null) return null;

        String key = profession.toLowerCase();
        LivingEntity cached = DWARF_RENDER_CACHE.get(key);
        if (cached != null) return cached;

        LivingEntity entity = null;
        switch (key) {
            case "dwarf" -> entity = new DwarfEntity(JolCraftEntities.DWARF.get(), mc.level);
            case "guildmaster" -> entity = new DwarfGuildmasterEntity(JolCraftEntities.DWARF_GUILDMASTER.get(), mc.level);
            case "historian" -> entity = new DwarfHistorianEntity(JolCraftEntities.DWARF_HISTORIAN.get(), mc.level);
            case "merchant" -> entity = new DwarfMerchantEntity(JolCraftEntities.DWARF_MERCHANT.get(), mc.level);
            case "scrapper" -> entity = new DwarfScrapperEntity(JolCraftEntities.DWARF_SCRAPPER.get(), mc.level);
            case "brewmaster" -> entity = new DwarfBrewmasterEntity(JolCraftEntities.DWARF_BREWMASTER.get(), mc.level);
            case "guard" -> entity = new DwarfGuardEntity(JolCraftEntities.DWARF_GUARD.get(), mc.level);
            case "keeper" -> entity = new DwarfKeeperEntity(JolCraftEntities.DWARF_KEEPER.get(), mc.level);
            case "artisan" -> entity = new DwarfArtisanEntity(JolCraftEntities.DWARF_ARTISAN.get(), mc.level);
            case "explorer" -> entity = new DwarfExplorerEntity(JolCraftEntities.DWARF_EXPLORER.get(), mc.level);
            case "miner" -> entity = new DwarfMinerEntity(JolCraftEntities.DWARF_MINER.get(), mc.level);
            default -> entity = new DwarfEntity(JolCraftEntities.DWARF.get(), mc.level); // fallback
        }

        if (entity != null) {
            DWARF_RENDER_CACHE.put(key, entity);
        }
        return entity;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }
}
