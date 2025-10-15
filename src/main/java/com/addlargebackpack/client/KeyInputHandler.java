package com.addlargebackpack.client;

import com.addlargebackpack.items.BackpackItem;
import com.addlargebackpack.network.OpenBackpackPacket;
import com.addlargebackpack.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyInputHandler {
    
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            
            // Check if the key was pressed
            if (KeyBindings.OPEN_BACKPACK.consumeClick()) {
                Player player = mc.player;
                if (player != null) {
                    // Find the leftmost backpack in inventory
                    int backpackSlot = findLeftmostBackpack(player);
                    if (backpackSlot != -1) {
                        // Send packet to server to open backpack
                        PacketHandler.INSTANCE.sendToServer(new OpenBackpackPacket(backpackSlot));
                    }
                }
            }
        }
    }
    
    /**
     * Finds the leftmost backpack in the player's inventory.
     * Priority: hotbar (left to right), then main inventory (left to right, top to bottom)
     * @return slot index, or -1 if no backpack found
     */
    private static int findLeftmostBackpack(Player player) {
        // Check hotbar first (slots 0-8)
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof BackpackItem) {
                return i;
            }
        }
        
        // Check main inventory (slots 9-35)
        for (int i = 9; i < 36; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof BackpackItem) {
                return i;
            }
        }
        
        return -1;
    }
}
