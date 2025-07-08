package net.sievert.jolcraft.screen.custom;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.sievert.jolcraft.JolCraft;

public class LockScreen extends AbstractContainerScreen<LockMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/gui/container/strongbox_lock.png");
    private static final ResourceLocation PROGRESS_TEXTURE = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/gui/container/sprites/lockpick/lockpick_progress.png");

    public LockScreen(LockMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;  // Set the width of the background image
        this.imageHeight = 150; // Set the height of the background image
        this.titleLabelY = 6;   // Position of the title
        this.inventoryLabelY = 56; // Position of the inventory label
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Set up the background texture
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(RenderType.GUI_TEXTURED, TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, 176, 150);

        // Render the lockpick progress bar
        renderLockpickProgress(guiGraphics, x, y);
    }

    private void renderLockpickProgress(GuiGraphics guiGraphics, int x, int y) {

        // Get the lockpick progress from the BlockEntity (menu's block entity)
        float progress = this.menu.getBlockEntity().getLockpickProgress();

        // Debugging to check the lockpick progress
        System.out.println("Lockpick progress (screen): " + progress);

        // Define the size of the progress bar
        int progressWidth = 108;  // Width of the progress bar
        int progressHeight = 16;  // Height of the progress bar
        int progressX = x + 34;   // X position for the progress bar
        int progressY = y + 16;   // Y position for the progress bar (adjust as needed)

        // Calculate the width of the progress bar based on progress (scale the width based on progress)
        int progressBarWidth = (int) (progress * progressWidth / 200);  // Scaling progress from 0 to 200 max value

        // Draw the progress using your custom texture
        guiGraphics.blit(RenderType.GUI_TEXTURED, PROGRESS_TEXTURE, progressX, progressY, 0, 0, progressBarWidth, progressHeight, progressWidth, progressHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Render the screen title and player inventory title
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xDDDDDD, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xDDDDDD, false);
    }
}
