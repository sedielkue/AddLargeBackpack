package com.addlargebackpack.client.gui;

import com.addlargebackpack.menu.BackpackContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.util.Mth;

public class BackpackScreen extends AbstractContainerScreen<BackpackContainer> {
    private static final ResourceLocation CHEST_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/container/generic_54.png");
    
    private static final int VISIBLE_ROWS = 6; // Number of visible backpack rows
    private static final int COLS = 9; // Number of columns
    private static final int SLOT_SIZE = 18; // Size of each slot
    private static final int SCROLLBAR_WIDTH = 4; // Thinner scrollbar
    private static final int SCROLLBAR_HEIGHT = 15;
    
    private int scrollOffset = 0; // Current scroll position (in rows)
    private int maxScrollOffset = 0; // Maximum scroll position
    private boolean isScrolling = false; // Whether the player is dragging the scrollbar
    private float scrollOfFloat = 0.0F; // Scroll position as a float (0.0 to 1.0)

    public BackpackScreen(BackpackContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        
        // Calculate scroll parameters - ensure we can see all content
        int totalRows = (int) Math.ceil((double) container.getBackpackSlots() / COLS);
        this.maxScrollOffset = Math.max(0, totalRows - VISIBLE_ROWS);
        
        // Always start at the top
        this.scrollOffset = 0;
        this.scrollOfFloat = 0.0F;
        
        // Set GUI dimensions exactly like a large chest
        this.imageWidth = 176; // Standard chest width
        this.imageHeight = 222; // Standard height for 6-row chest
        this.inventoryLabelY = this.imageHeight - 94; // Position player inventory label correctly
    }

    @Override
    protected void init() {
        super.init();
        // Always start at the top when opening backpack
        this.scrollOfFloat = 0.0F;
        this.scrollOffset = 0;
        // Reset container scroll position as well
        this.menu.setScrollOffset(0);
        this.scrollTo(0.0F);
        
        // Force multiple synchronization cycles to ensure proper display
        for (int i = 0; i < 3; i++) {
            this.menu.broadcastChanges();
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        
        // Draw the complete chest background
        guiGraphics.blit(CHEST_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
        
        // Draw scrollbar if needed
        if (this.maxScrollOffset > 0) {
            this.renderScrollbar(guiGraphics, x, y, mouseX, mouseY);
        }
    }

    private void renderScrollbar(GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY) {
        int scrollbarX = x + this.imageWidth - 7; // Move closer to the right edge to avoid slot overlap
        int scrollbarY = y + 18;
        int scrollbarTrackHeight = VISIBLE_ROWS * SLOT_SIZE - 2;
        
        // Draw scrollbar track (background)
        guiGraphics.fill(scrollbarX, scrollbarY, scrollbarX + SCROLLBAR_WIDTH, scrollbarY + scrollbarTrackHeight, 0xFF555555);
        
        // Calculate scrollbar handle position and size
        int handleHeight = Math.max(SCROLLBAR_HEIGHT, scrollbarTrackHeight * VISIBLE_ROWS / (VISIBLE_ROWS + maxScrollOffset));
        int handleY = scrollbarY + (int)((scrollbarTrackHeight - handleHeight) * this.scrollOfFloat);
        
        // Draw scrollbar handle
        int handleColor = this.isScrolling ? 0xFFDDDDDD : (this.isMouseOverScrollbar(mouseX, mouseY) ? 0xFFBBBBBB : 0xFFAAAAAA);
        guiGraphics.fill(scrollbarX + 1, handleY, scrollbarX + SCROLLBAR_WIDTH - 1, handleY + handleHeight, handleColor);
        
        // Draw scrollbar border
        guiGraphics.fill(scrollbarX, scrollbarY, scrollbarX + 1, scrollbarY + scrollbarTrackHeight, 0xFF333333); // left
        guiGraphics.fill(scrollbarX + SCROLLBAR_WIDTH - 1, scrollbarY, scrollbarX + SCROLLBAR_WIDTH, scrollbarY + scrollbarTrackHeight, 0xFF333333); // right
        guiGraphics.fill(scrollbarX, scrollbarY, scrollbarX + SCROLLBAR_WIDTH, scrollbarY + 1, 0xFF333333); // top
        guiGraphics.fill(scrollbarX, scrollbarY + scrollbarTrackHeight - 1, scrollbarX + SCROLLBAR_WIDTH, scrollbarY + scrollbarTrackHeight, 0xFF333333); // bottom
    }

    private boolean isMouseOverScrollbar(int mouseX, int mouseY) {
        if (this.maxScrollOffset <= 0) return false;
        
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        int scrollbarX = x + this.imageWidth - 7; // Match the new scrollbar position
        int scrollbarY = y + 18;
        int scrollbarTrackHeight = VISIBLE_ROWS * SLOT_SIZE - 2;
        
        return mouseX >= scrollbarX && mouseX < scrollbarX + SCROLLBAR_WIDTH &&
               mouseY >= scrollbarY && mouseY < scrollbarY + scrollbarTrackHeight;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && this.isMouseOverScrollbar((int)mouseX, (int)mouseY)) {
            this.isScrolling = true;
            return true;
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.isScrolling = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.isScrolling && this.maxScrollOffset > 0) {
            int y = (this.height - this.imageHeight) / 2;
            int scrollbarY = y + 18;
            int scrollbarTrackHeight = VISIBLE_ROWS * SLOT_SIZE - 2;
            
            float newScrollOf = ((float)mouseY - scrollbarY) / scrollbarTrackHeight;
            this.scrollTo(Mth.clamp(newScrollOf, 0.0F, 1.0F));
            return true;
        }
        
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.maxScrollOffset > 0) {
            float scrollStep = 1.0F / this.maxScrollOffset;
            float newScrollOf = this.scrollOfFloat - (float)delta * scrollStep;
            this.scrollTo(newScrollOf);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    private void scrollTo(float scrollOf) {
        this.scrollOfFloat = Mth.clamp(scrollOf, 0.0F, 1.0F);
        this.scrollOffset = Math.round(this.scrollOfFloat * this.maxScrollOffset);
        
        // Update the container's scroll offset (client-side)
        this.menu.setScrollOffset(this.scrollOffset);
        
        // Send scroll offset to server
        com.addlargebackpack.network.PacketHandler.INSTANCE.sendToServer(
            new com.addlargebackpack.network.ScrollPacket(this.scrollOffset)
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        
        // Force container synchronization to ensure slots are always up to date
        this.menu.broadcastChanges();
        
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Draw the title
        guiGraphics.drawString(this.font, this.title, 8, 6, 4210752, false);
        // Draw the player inventory label at the correct position
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.inventoryLabelY, 4210752, false);
    }
}