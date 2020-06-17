package net.guavy.sf.data;

import io.netty.buffer.ByteBuf;
import net.guavy.sf.ClientProxy;
import net.guavy.sf.ScreenshotForum;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;

public class PacketSendPosts implements IMessage {

    private ArrayList<Post> posts;

    @Override
    public void fromBytes(ByteBuf buf) {
        byte[] bytes = new byte[buf.readInt()];
        buf.readBytes(bytes);

        posts = ScreenshotForum.getPostsFromBytes(bytes);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        byte[] bytes = ScreenshotForum.getPostsAsBytes();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    public static class Handler implements IMessageHandler<PacketSendPosts, IMessage> {
        @Override
        public IMessage onMessage(PacketSendPosts message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketSendPosts message, MessageContext ctx) {
            ClientProxy.openPostGui(message.posts);
        }
    }
}
