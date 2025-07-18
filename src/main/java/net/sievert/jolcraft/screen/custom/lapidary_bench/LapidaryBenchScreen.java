package net.sievert.jolcraft.screen.custom.lapidary_bench;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.sievert.jolcraft.JolCraft;

public class LapidaryBenchScreen extends AbstractContainerScreen<LapidaryBenchMenu> {

    // Texture resources
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/gui/container/lapidary_bench.png");
    private static final ResourceLocation HIGHLIGHT = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/gui/container/sprites/button_highlighted.png");
    private static final ResourceLocation HAMMER = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/gui/container/sprites/lapidary_bench/deepslate_artisan_hammer.png");
    private static final ResourceLocation CHISEL = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/gui/container/sprites/lapidary_bench/deepslate_chisel.png");

    // Button positions
    private static final int HAMMER_X = 80;
    private static final int CHISEL_X = 128;
    private static final int BUTTON_Y = 32;
    private static final int BUTTON_SIZE = 16;
    private static final int HIGHLIGHT_SIZE = 17;

    public LapidaryBenchScreen(LapidaryBenchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 150;
        this.titleLabelY = 6;
        this.inventoryLabelY = 56;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(RenderType.GUI_TEXTURED, TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, 176, 150);

        // Always render hammer if gem/geode in slot
        if (menu.hasGem() || menu.hasGeode()) {
            boolean hammerActive = menu.hasHammer();
            renderToolButton(guiGraphics, x, y, mouseX, mouseY, HAMMER_X, BUTTON_Y, HAMMER, 0, hammerActive);
        }
        // Always render chisel if gem in slot
        if (menu.hasGem()) {
            boolean chiselActive = menu.hasChisel();
            renderToolButton(guiGraphics, x, y, mouseX, mouseY, CHISEL_X, BUTTON_Y, CHISEL, 1, chiselActive);
        }
    }


    /**
     * Renders one tool button (hammer or chisel) with highlight on hover.
     */

    private void renderToolButton(
            GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY,
            int btnRelX, int btnRelY, ResourceLocation icon, int btnIndex, boolean active
    ) {
        int bx = x + btnRelX;
        int by = y + btnRelY;
        boolean hovered = active && mouseX >= bx && mouseY >= by && mouseX < bx + BUTTON_SIZE && mouseY < by + BUTTON_SIZE;

        float alpha = active ? 1.0f : 0.4f;

        // 1. Flush any batched draws so far before changing alpha
        guiGraphics.flush();

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);

        // Highlight (only for active+hover)
        if (hovered) {
            guiGraphics.blit(RenderType.GUI_TEXTURED, HIGHLIGHT, bx, by, 0, 0, HIGHLIGHT_SIZE, HIGHLIGHT_SIZE, HIGHLIGHT_SIZE, HIGHLIGHT_SIZE);
        }
        // Button icon
        guiGraphics.blit(RenderType.GUI_TEXTURED, icon, bx, by, 0, 0, BUTTON_SIZE, BUTTON_SIZE, BUTTON_SIZE, BUTTON_SIZE);

        // 2. Flush *again* to finish the alpha-drawn batch
        guiGraphics.flush();

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f); // reset alpha for later draws
        RenderSystem.disableBlend();
    }





    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xDDDDDD, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xDDDDDD, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if (button == 0) {
            // Hammer logic
            if ((menu.hasGem() || menu.hasGeode()) && menu.hasHammer() && isOverButton(mouseX, mouseY, x + HAMMER_X, y + BUTTON_Y)) {
                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0); // Hammer
                return true;
            }
            // Chisel logic
            if (menu.hasGem() && menu.hasChisel() && isOverButton(mouseX, mouseY, x + CHISEL_X, y + BUTTON_Y)) {
                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 1); // Chisel
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }


    private boolean isOverButton(double mouseX, double mouseY, int bx, int by) {
        return mouseX >= bx && mouseY >= by && mouseX < bx + BUTTON_SIZE && mouseY < by + BUTTON_SIZE;
    }

    private void playClickSound() {
        if (minecraft != null && minecraft.getSoundManager() != null) {
            minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
    }
}
