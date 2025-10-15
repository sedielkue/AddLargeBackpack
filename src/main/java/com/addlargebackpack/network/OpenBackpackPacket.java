package com.addlargebackpack.network;

import com.addlargebackpack.items.BackpackItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Network packet for opening a backpack from a specific inventory slot.
 * Sent from client to server when the player presses the open backpack key.
 */
public class OpenBackpackPacket {
    private final int slotIndex;

    public OpenBackpackPacket(int slotIndex) {
        this.slotIndex = slotIndex;
    }

    public OpenBackpackPacket(FriendlyByteBuf buf) {
        this.slotIndex = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.slotIndex);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && slotIndex >= 0 && slotIndex < player.getInventory().getContainerSize()) {
                ItemStack stack = player.getInventory().getItem(slotIndex);
                if (stack.getItem() instanceof BackpackItem) {
                    BackpackItem backpackItem = (BackpackItem) stack.getItem();
                    backpackItem.openBackpack(player, stack, slotIndex);
                }
            }
        });
        return true;
    }
}
