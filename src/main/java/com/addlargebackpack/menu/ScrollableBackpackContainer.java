package com.addlargebackpack.menu;

import com.addlargebackpack.AddLargeBackpackMod;
import com.addlargebackpack.inventory.BackpackInventory;
import com.addlargebackpack.items.BackpackItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ScrollableBackpackContainer extends AbstractContainerMenu {
    private final BackpackInventory backpackInventory;
    private final int backpackSlots;
    private int scrollOffset = 0;
    private static final int VISIBLE_ROWS = 6;
    private static final int COLS = 12;

    public ScrollableBackpackContainer(int id, Inventory playerInventory, ItemStack backpackStack) {
        super(AddLargeBackpackMod.BACKPACK_MENU_TYPE.get(), id);
        
        this.backpackSlots = BackpackItem.getBackpackSize();
        this.backpackInventory = new BackpackInventory(backpackStack, backpackSlots);

        setupSlots(playerInventory);
    }

    private void setupSlots(Inventory playerInventory) {
        this.slots.clear();
        
        // Add visible backpack slots based on scroll offset
        for (int row = 0; row < VISIBLE_ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int slotIndex = (row + scrollOffset) * COLS + col;
                if (slotIndex < backpackSlots) {
                    this.addSlot(new ScrollableSlot(backpackInventory, slotIndex, 8 + col * 18, 18 + row * 18));
                } else {
                    // Add empty placeholder slot
                    this.addSlot(new EmptySlot(8 + col * 18, 18 + row * 18));
                }
            }
        }

        // Add player inventory slots
        int playerInventoryStartY = 18 + VISIBLE_ROWS * 18 + 14;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, playerInventoryStartY + row * 18));
            }
        }

        // Add player hotbar slots
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, playerInventoryStartY + 58));
        }
    }

    public void scroll(int direction) {
        int maxRows = (int) Math.ceil((double) backpackSlots / COLS);
        int maxScrollOffset = Math.max(0, maxRows - VISIBLE_ROWS);
        
        int newOffset = scrollOffset + direction;
        if (newOffset >= 0 && newOffset <= maxScrollOffset) {
            scrollOffset = newOffset;
            // We would need to refresh the container here, but that's complex
            // For now, this is a simplified implementation
        }
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public int getMaxScrollOffset() {
        int maxRows = (int) Math.ceil((double) backpackSlots / COLS);
        return Math.max(0, maxRows - VISIBLE_ROWS);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            
            int visibleBackpackSlots = VISIBLE_ROWS * COLS;
            
            if (index < visibleBackpackSlots) {
                // Moving from backpack to player inventory
                if (!this.moveItemStackTo(itemstack1, visibleBackpackSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Moving from player inventory to backpack
                if (!this.moveItemStackTo(itemstack1, 0, visibleBackpackSlots, false)) {
                    return ItemStack.EMPTY;
                }
            }
            
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public BackpackInventory getBackpackInventory() {
        return backpackInventory;
    }

    public int getBackpackSlots() {
        return backpackSlots;
    }

    // Custom slot for scrollable backpack
    private static class ScrollableSlot extends Slot {
        public ScrollableSlot(BackpackInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return !(stack.getItem() instanceof com.addlargebackpack.items.BackpackItem);
        }
    }

    // Empty slot that can't hold items (for padding)
    private static class EmptySlot extends Slot {
        public EmptySlot(int x, int y) {
            super(null, 0, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack getItem() {
            return ItemStack.EMPTY;
        }

        @Override
        public void set(ItemStack stack) {
            // Do nothing
        }

        @Override
        public boolean hasItem() {
            return false;
        }
    }
}