package net.guavy.sf.data;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    private static int packetId = 0;

    public static SimpleNetworkWrapper INSTANCE = null;

    public PacketHandler() {
    }

    public static int nextID() {
        return packetId++;
    }

    public static void registerMessages(String channelName) {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
        registerMessages();
    }

    public static void registerMessages() {
        INSTANCE.registerMessage(PacketRequestPosts.Handler.class, PacketRequestPosts.class, nextID(), Side.SERVER);
        INSTANCE.registerMessage(PacketSendPosts.Handler.class, PacketSendPosts.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(PacketNewPost.Handler.class, PacketNewPost.class, nextID(), Side.SERVER);
    }
}
