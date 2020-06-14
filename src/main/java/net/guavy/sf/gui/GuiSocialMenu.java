package net.guavy.sf.gui;

import net.guavy.sf.data.Post;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@SideOnly(Side.CLIENT)
public class GuiSocialMenu extends GuiScreen {

    private SocialScrollPanel socialScrollPanel;
    private final ArrayList<Post> posts;

    public GuiSocialMenu(ArrayList<Post> posts) {
        Collections.reverse(posts);

        this.posts = posts;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        this.buttonList.add(new GuiButton(1, this.width / 4, this.height - 40, this.width / 2, 20, "Exit"));
        this.socialScrollPanel = new SocialScrollPanel(this, posts, this.width / 2, this.height, 30, this.height - 50, this.width/4, 38);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();

                break;
            default:
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.socialScrollPanel.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, "Social", this.width / 2, 20, 16777215);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
