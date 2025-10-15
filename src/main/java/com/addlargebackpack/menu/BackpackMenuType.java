package com.addlargebackpack.menu;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class BackpackMenuType {
    public static MenuType<BackpackContainer> create() {
        return IForgeMenuType.create((windowId, inv, data) -> {
            // The data buffer should contain the backpack itemstack information
            // For simplicity, we'll find the backpack in the player's inventory
            for (int i = 0; i < inv.getContainerSize(); i++) {
                if (inv.getItem(i).getItem() instanceof com.addlargebackpack.items.BackpackItem) {
                    return new BackpackContainer(windowId, inv, inv.getItem(i));
                }
            }
            // Fallback: create with main hand item
            return new BackpackContainer(windowId, inv, inv.player.getMainHandItem());
        });
    }
}