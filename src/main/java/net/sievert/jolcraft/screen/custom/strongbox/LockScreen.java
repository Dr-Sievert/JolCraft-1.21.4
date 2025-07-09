package net.sievert.jolcraft.screen.custom.strongbox;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.sievert.jolcraft.JolCraft;

import java.util.List;
import java.util.Random;

public class LockScreen extends AbstractContainerScreen<LockMenu> {
    private final Random guiRandom = new Random();
    private int lastSeenPulse = 0;

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/gui/container/strongbox_lock.png");
    private static final ResourceLocation PROGRESS_TEXTURE = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/gui/container/sprites/lockpick/lockpick_progress.png");
    private static final ResourceLocation HIGHLIGHT =
            ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/gui/container/sprites/lockpick/button_highlighted.png");
    private static final ResourceLocation LOCKPICK_TEXTURE = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/item/lockpick.png");
    private static final ResourceLocation BROKEN_LOCKPICK_TEXTURE1 = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/gui/container/sprites/lockpick/lockpick_broken1.png");
    private static final ResourceLocation BROKEN_LOCKPICK_TEXTURE2 = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/gui/container/sprites/lockpick/lockpick_broken2.png");
    private static final ResourceLocation BROKEN_LOCKPICK_TEXTURE3 = ResourceLocation.fromNamespaceAndPath(JolCraft.MOD_ID, "textures/gui/container/sprites/lockpick/lockpick_broken3.png");


    // List of broken textures
    private static final List<ResourceLocation> BROKEN_BUTTON_TEXTURES = List.of(
            BROKEN_LOCKPICK_TEXTURE1, BROKEN_LOCKPICK_TEXTURE2, BROKEN_LOCKPICK_TEXTURE3
    );

    private ResourceLocation lockpick_broken = BROKEN_LOCKPICK_TEXTURE1;  // Initial texture


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
        int pulse = this.menu.getButtonLayerUpdatePulse();

        guiGraphics.blit(RenderType.GUI_TEXTURED, TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, 176, 150);

        // Only update broken lockpick sprite if a new cycle has started
        if (pulse != lastSeenPulse) {
            lastSeenPulse = pulse;
            guiGraphics.blit(RenderType.GUI_TEXTURED, TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, 176, 150);
            updateBrokenLockpickTexture();
            renderLockpickProgress(guiGraphics, x, y);
        }


        if (this.menu.isActive()) {
            renderLockpickProgress(guiGraphics, x, y);
            renderButtonTextures(guiGraphics, x, y, mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Render the screen title and player inventory title
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xDDDDDD, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xDDDDDD, false);
    }

    private void renderLockpickProgress(GuiGraphics guiGraphics, int x, int y) {

        float progress = this.menu.getLockpickProgress();

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

    // Only update texture once per cycle
    private void updateBrokenLockpickTexture() {
        int idx = guiRandom.nextInt(BROKEN_BUTTON_TEXTURES.size());
        this.lockpick_broken = BROKEN_BUTTON_TEXTURES.get(idx);
    }


    private void renderButtonTextures(GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY) {
        int correctButtonId = this.menu.getCorrectButtonId(); // 0–2
        int[] buttonXs = { x + 48, x + 80, x + 112 };
        int buttonY = y + 31;

        for (int idx = 0; idx < 3; idx++) {
            int bx = buttonXs[idx], by = buttonY, bw = 16, bh = 16, hw = 17, hh = 17;

            boolean hovered = mouseX >= bx && mouseY >= by && mouseX < bx + bw && mouseY < by + bh;
            if (hovered) {
                // Center the 17x17 highlight on the 16x16 button
                guiGraphics.blit(RenderType.GUI_TEXTURED, HIGHLIGHT, bx, by, 0, 0, hw, hh, hw, hh);
            }
            ResourceLocation texture = (idx == correctButtonId) ? LOCKPICK_TEXTURE : lockpick_broken;
            guiGraphics.blit(RenderType.GUI_TEXTURED, texture, bx, by, 0, 0, bw, bh, bw, bh);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        if (button == 0) {
            for (int idx = 0; idx < 3; idx++) {
                int btnX = x + 48 + idx * 32;
                int btnY = y + 31;
                int btnW = 16, btnH = 16;

                if (mouseX >= btnX && mouseY >= btnY && mouseX < btnX + btnW && mouseY < btnY + btnH && this.menu.isActive()) {
                    // Play instant client-only click sound for feedback
                    if (minecraft != null && minecraft.getSoundManager() != null) {
                        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    }
                    // Notify the server (let server do real action/check)
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, idx);
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }





}
