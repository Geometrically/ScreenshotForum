package net.guavy.sf.data;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import net.guavy.sf.ScreenshotForum;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;

public class PacketNewPost implements IMessage {
    private Post post;

    public PacketNewPost() {
    }

    public PacketNewPost(Post post) {
        this.post = post;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        byte[] bytes = new byte[buf.readInt()];
        buf.readBytes(bytes);

        Gson gson = new Gson();

        post = gson.fromJson(new String(bytes), Post.class);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        Gson gson = new Gson();

        byte[] bytes = gson.toJson(post).getBytes();

        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    public static class Handler implements IMessageHandler<PacketNewPost, IMessage> {
        @Override
        public IMessage onMessage(PacketNewPost message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketNewPost message, MessageContext ctx) {
            ArrayList<Post> posts = ScreenshotForum.getPosts();

            posts.add(message.post);

            ScreenshotForum.writePosts(posts);
        }
    }
}
