package net.guavy.sf.command;

import net.guavy.sf.gui.GuiNewPost;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class NewPostCommand extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "newpost";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender) {
        return "newpost <image>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            return;
        }

        String s = args[0];

        Minecraft.getMinecraft().displayGuiScreen(new GuiNewPost(s, sender.getName()));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return Collections.emptyList();
    }
}
