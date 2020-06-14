package net.guavy.sf.gui;

import net.guavy.sf.ScreenshotForum;
import net.guavy.sf.data.Post;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiScrollingList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SocialScrollPanel extends GuiScrollingList {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat();
    private static final ResourceLocation ICON_MISSING = new ResourceLocation("minecraft","textures/misc/unknown_server.png");

    private GuiSocialMenu parent;
    private ArrayList<Post> posts;
    private ArrayList<ResourceLocation> postImages;

    public SocialScrollPanel(GuiSocialMenu parent, ArrayList<Post> posts, int width, int height, int top, int bottom, int left, int entryHeight) {
        super(parent.mc, width, height, top, bottom, left, entryHeight, width, height);

        this.parent = parent;
        this.posts = posts;

        this.postImages = new ArrayList<>();

        for (Post post : posts) {
            if(post.icon == null) {
                this.postImages.add(ICON_MISSING);
                break;
            };
            try {
                CloseableHttpClient client = HttpClients.createDefault();

                HttpGet request = new HttpGet(post.icon);

                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    this.postImages.add(this.parent.mc.getRenderManager().renderEngine.getDynamicTextureLocation(post.icon, new DynamicTexture(TextureUtil.readBufferedImage(entity.getContent()))));
                } else {
                    this.postImages.add(ICON_MISSING);
                }

                client.close();
            } catch (IOException e) {
                this.postImages.add(ICON_MISSING);
                e.printStackTrace();
            }
        }
    }


    @Override
    protected int getSize() {
        return posts.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {

    }

    @Override
    protected boolean isSelected(int index) {
        return false;
    }

    @Override
    protected void drawBackground() {
        this.parent.drawDefaultBackground();
    }

    @Override
    protected int getContentHeight()
    {
        return (this.getSize()) * 38 + 1;
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int top, int slotBuffer, Tessellator tess) {
        Post post = posts.get(slotIdx);

        if(post.name == null) return;

        FontRenderer font = this.parent.mc.fontRenderer;

        Date date = new Date(post.dateCreated);

        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        String dateString = DATE_FORMAT.format(date);

        if(date.after(c.getTime())) {
            dateString = dateString.substring(8);
        } else {
            dateString = dateString.substring(0, 7);
        }

        String authorString = post.author + " (" + dateString + ")";

        if(postImages.get(slotIdx) != null) {
            this.parent.mc.getTextureManager().bindTexture(postImages.get(slotIdx));
        } else {
            this.parent.mc.getTextureManager().bindTexture(ICON_MISSING);
        }

        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.pushMatrix();
        Gui.drawModalRectWithCustomSizedTexture(this.left + 2, top + 2, 0, 0, 58, slotHeight - 2, 58, slotHeight - 2);
        GlStateManager.popMatrix();

        font.drawString(font.trimStringToWidth(post.name,this.right - 64), this.left + 64, top + 2, 0x00FF00);

        if(post.description.length() > 34) {
            String descriptionFirstLine = post.description.substring(0, 34);
            String descriptionSecondLine = post.description.substring(34);

            font.drawString(font.trimStringToWidth(descriptionFirstLine,listWidth - 64), this.left + 64, top + 14, 0xFFFFFF);
            font.drawString(font.trimStringToWidth(descriptionSecondLine,listWidth - 64), this.left + 64, top + 22, 0xFFFFFF);
            font.drawString(authorString, this.left + 64, top + 31, 0xffa500);
        } else {
            font.drawString(font.trimStringToWidth(post.description,listWidth - 64), this.left + 64, top + 14, 0xFFFFFF);
            font.drawString(authorString, this.left + 64, top + 23, 0xffa500);
        }
    }
}

