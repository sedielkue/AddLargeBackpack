package com.addlargebackpack.network;

import com.addlargebackpack.AddLargeBackpackMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * Handles network communication between client and server for the backpack mod.
 * Currently manages scroll position synchronization packets.
 */
public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        ResourceLocation.fromNamespaceAndPath(AddLargeBackpackMod.MODID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;
    
    private static int id() {
        return packetId++;
    }

    /**
     * Registers all network packets for the mod.
     * Must be called during mod initialization.
     */
    public static void register() {
        INSTANCE.messageBuilder(ScrollPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(ScrollPacket::new)
            .encoder(ScrollPacket::toBytes)
            .consumerMainThread(ScrollPacket::handle)
            .add();
        
        INSTANCE.messageBuilder(OpenBackpackPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(OpenBackpackPacket::new)
            .encoder(OpenBackpackPacket::toBytes)
            .consumerMainThread(OpenBackpackPacket::handle)
            .add();
    }
}
