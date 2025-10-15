package com.addlargebackpack.menu;

import com.addlargebackpack.AddLargeBackpackMod;
import com.addlargebackpack.config.BackpackConfig;
import com.addlargebackpack.inventory.BackpackInventory;
import com.addlargebackpack.items.BackpackItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BackpackContainer extends AbstractContainerMenu {
    private final BackpackInventory backpackInventory;
    private final int backpackSlots;
    private final ItemStack backpackStack;
    private final int backpackSlotIndex;
    private static final int VISIBLE_ROWS = 6;
    private int scrollOffset = 0;

    public BackpackContainer(int id, Inventory playerInventory, ItemStack backpackStack) {
        super(AddLargeBackpackMod.BACKPACK_MENU_TYPE.get(), id);
        
        this.backpackSlots = BackpackItem.getBackpackSize();
        this.backpackInventory = new BackpackInventory(backpackStack, backpackSlots);
        this.backpackStack = backpackStack;
        
        // Always start with scroll position at top
        this.scrollOffset = 0;
        
        // Find the backpack slot index to prevent moving it
        this.backpackSlotIndex = findBackpackSlotIndex(playerInventory);

        // Add backpack slots - only visible ones (initially scroll offset = 0)
        for (int row = 0; row < VISIBLE_ROWS; row++) {
            for (int col = 0; col < 9; col++) {
                int visibleIndex = row * 9 + col;
                this.addSlot(new ScrollableBackpackSlot(backpackInventory, visibleIndex, 8 + col * 18, 18 + row * 18, this));
            }
        }

        // Add player inventory slots with restriction
        int playerInventoryStartY = 18 + VISIBLE_ROWS * 18 + 14; // Start player inventory below visible backpack area
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int slotIndex = col + row * 9 + 9;
                this.addSlot(new RestrictedSlot(playerInventory, slotIndex, 8 + col * 18, playerInventoryStartY + row * 18));
            }
        }

        // Add player hotbar slots with restriction
        for (int col = 0; col < 9; col++) {
            this.addSlot(new RestrictedSlot(playerInventory, col, 8 + col * 18, playerInventoryStartY + 58));
        }
        
        // Ensure all slots are properly initialized and synced
        this.broadcastChanges();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            
            int visibleBackpackSlots = VISIBLE_ROWS * 9; // Always 54 visible slots
            
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
        return true; // Always valid since the backpack is in the player's inventory
    }

    public BackpackInventory getBackpackInventory() {
        return backpackInventory;
    }

    public int getBackpackSlots() {
        return backpackSlots;
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setScrollOffset(int offset) {
        if (this.scrollOffset != offset) {
            this.scrollOffset = offset;
            // Force update all visible slots when scroll position changes
            this.broadcastChanges();
        }
    }
    
    public int getMaxScrollOffset() {
        int totalRows = BackpackConfig.BACKPACK_ROWS.get();
        return Math.max(0, totalRows - VISIBLE_ROWS);
    }

    private int findBackpackSlotIndex(Inventory playerInventory) {
        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
            ItemStack stack = playerInventory.getItem(i);
            if (stack == backpackStack) {
                return i;
            }
        }
        return -1; // Not found
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        // Prevent taking the backpack that's currently open
        if (slot.container instanceof Inventory && 
            slot.getSlotIndex() == backpackSlotIndex) {
            return false;
        }
        return super.canTakeItemForPickAll(stack, slot);
    }
    
    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        // Standard synchronization is sufficient with dynamic slot calculation
    }

    /**
     * Custom slot implementation for scrollable backpack slots.
     * Maps visible screen positions (visibleIndex) to actual inventory positions (actualIndex)
     * based on the current scroll offset.
     */
    private static class ScrollableBackpackSlot extends Slot {
        private final BackpackContainer container;
        private final int visibleIndex; // Fixed screen position (0-53 for 6 rows)

        public ScrollableBackpackSlot(BackpackInventory inventory, int visibleIndex, int x, int y, BackpackContainer container) {
            super(inventory, 0, x, y); // Use 0 as placeholder - we override getSlotIndex()
            this.container = container;
            this.visibleIndex = visibleIndex;
        }

        /**
         * Calculates the actual inventory index based on visible position and scroll offset.
         * Formula: actualIndex = (visibleRow + scrollOffset) * 9 + column
         */
        private int getActualIndex() {
            int row = visibleIndex / 9;
            int col = visibleIndex % 9;
            return (row + container.getScrollOffset()) * 9 + col;
        }

        /**
         * Override to return the dynamically calculated actual index instead of the cached value.
         * This is critical for proper slot interaction after scrolling.
         */
        @Override
        public int getSlotIndex() {
            return getActualIndex();
        }

        @Override
        public ItemStack getItem() {
            int actualIndex = getActualIndex();
            if (actualIndex >= 0 && actualIndex < container.getBackpackSlots()) {
                return container.backpackInventory.getItem(actualIndex);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public void set(ItemStack stack) {
            int actualIndex = getActualIndex();
            if (actualIndex >= 0 && actualIndex < container.getBackpackSlots()) {
                container.backpackInventory.setItem(actualIndex, stack);
                this.setChanged();
            }
        }

        @Override
        public boolean hasItem() {
            return !this.getItem().isEmpty();
        }

        @Override
        public ItemStack remove(int amount) {
            int actualIndex = getActualIndex();
            if (actualIndex >= 0 && actualIndex < container.getBackpackSlots()) {
                return container.backpackInventory.removeItem(actualIndex, amount);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            int actualIndex = getActualIndex();
            if (actualIndex >= 0 && actualIndex < container.getBackpackSlots()) {
                return !(stack.getItem() instanceof com.addlargebackpack.items.BackpackItem);
            }
            return false;
        }

        @Override
        public boolean isActive() {
            int actualIndex = getActualIndex();
            return actualIndex >= 0 && actualIndex < container.getBackpackSlots();
        }
    }

    // Custom slot class for player inventory that prevents moving the open backpack
    private class RestrictedSlot extends Slot {
        private final int slotIndex;

        public RestrictedSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
            this.slotIndex = index;
        }

        @Override
        public boolean mayPickup(net.minecraft.world.entity.player.Player player) {
            // Prevent picking up the backpack that's currently open
            if (this.slotIndex == backpackSlotIndex) {
                return false;
            }
            return super.mayPickup(player);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            // Prevent placing items in the slot where the open backpack is
            if (this.slotIndex == backpackSlotIndex && !stack.isEmpty()) {
                return false;
            }
            return super.mayPlace(stack);
        }
    }
}