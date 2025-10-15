package com.addlargebackpack.network;

import com.addlargebackpack.menu.BackpackContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Network packet for synchronizing backpack scroll position from client to server.
 * Ensures that both sides calculate the same actual slot indices based on scroll offset.
 */
public class ScrollPacket {
    private final int scrollOffset;

    public ScrollPacket(int scrollOffset) {
        this.scrollOffset = scrollOffset;
    }

    public ScrollPacket(FriendlyByteBuf buf) {
        this.scrollOffset = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.scrollOffset);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.containerMenu instanceof BackpackContainer) {
                BackpackContainer container = (BackpackContainer) player.containerMenu;
                container.setScrollOffset(this.scrollOffset);
            }
        });
        return true;
    }
}
