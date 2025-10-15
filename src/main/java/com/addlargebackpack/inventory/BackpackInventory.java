package com.addlargebackpack.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class BackpackInventory extends SimpleContainer {
    private final ItemStack backpackStack;
    private static final String ITEMS_TAG = "Items";

    public BackpackInventory(ItemStack backpackStack, int size) {
        super(size);
        this.backpackStack = backpackStack;
        loadItems();
    }

    private void loadItems() {
        CompoundTag tag = backpackStack.getOrCreateTag();
        if (tag.contains(ITEMS_TAG)) {
            NonNullList<ItemStack> items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(tag, items);
            for (int i = 0; i < items.size() && i < getContainerSize(); i++) {
                setItem(i, items.get(i));
            }
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        saveItems();
    }

    private void saveItems() {
        CompoundTag tag = backpackStack.getOrCreateTag();
        NonNullList<ItemStack> items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < getContainerSize(); i++) {
            items.set(i, getItem(i));
        }
        ContainerHelper.saveAllItems(tag, items);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        // Prevent backpacks from being placed inside themselves
        return !(stack.getItem() instanceof com.addlargebackpack.items.BackpackItem);
    }
    
    @Override
    public ItemStack getItem(int index) {
        return super.getItem(index);
    }
}