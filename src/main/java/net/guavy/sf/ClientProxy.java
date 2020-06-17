package net.guavy.sf;

import net.guavy.sf.data.Post;
import net.guavy.sf.gui.GuiSocialMenu;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class ClientProxy {
    public static void openPostGui(ArrayList<Post> posts) {
        System.out.println("dasdawdawdawdd");
        Minecraft mc = Minecraft.getMinecraft();

        mc.addScheduledTask(() -> mc.displayGuiScreen(new GuiSocialMenu(posts)));
    }
}
