package net.guavy.sf.gui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.guavy.sf.ScreenshotForum;
import net.guavy.sf.data.PacketHandler;
import net.guavy.sf.data.PacketNewPost;
import net.guavy.sf.data.Post;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class GuiNewPost extends GuiScreen {
    public String screenshotPath;
    public String author;

    public GuiTextField postNameField;
    public GuiTextField postDescriptionField;

    public GuiNewPost(String screenshotPath, String author) {
        this.screenshotPath = screenshotPath;
        this.author = author;
    }

    @Override
    public void initGui() {
        this.postNameField = new GuiTextField(9, this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
        this.postNameField.setFocused(true);
        this.postNameField.setText("");
        this.postNameField.setMaxStringLength(40);

        this.postDescriptionField = new GuiTextField(10, this.fontRenderer, this.width / 2 - 100, 100, 200, 20);
        this.postDescriptionField.setText("");
        this.postDescriptionField.setMaxStringLength(80);

        this.buttonList.add(new GuiButton(0, this.width / 2 - 120, this.height - 40, 120, 20, "Create Post"));
        this.buttonList.add(new GuiButton(1, this.width / 2, this.height - 40, 120, 20, "Exit"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                BufferedImage bufferedImage = scale(ImageIO.read(new File(this.screenshotPath)), 800, 450);
                ImageIO.write(bufferedImage, "png", os);

                CloseableHttpClient client = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost("https://api.imgur.com/3/image");

                httpPost.addHeader("Authorization", "Client-ID " + ScreenshotForum.IMGUR_TOKEN);

                List<NameValuePair> params = new ArrayList<>(2);
                params.add(new BasicNameValuePair("image", Base64.getEncoder().encodeToString(os.toByteArray())));

                httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

                HttpResponse response = client.execute(httpPost);
                HttpEntity entity = response.getEntity();

                String result = "";

                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }

                client.close();

                JsonObject object = new Gson().fromJson(result, JsonObject.class).get("data").getAsJsonObject();
                Post post = new Post(this.postNameField.getText(), this.postDescriptionField.getText(), author, object.get("link").getAsString(), System.currentTimeMillis());

                PacketHandler.INSTANCE.sendToServer(new PacketNewPost(post));

                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();

                break;
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
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "New Post", this.width / 2, 20, 16777215);

        this.drawCenteredString(this.fontRenderer, "Post Name", this.width / 2 - 75, 48, 16777215);
        this.postNameField.drawTextBox();
        this.drawCenteredString(this.fontRenderer, "Post Description", this.width / 2 - 60, 88, 16777215);
        this.postDescriptionField.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        this.postNameField.mouseClicked(mouseX, mouseY, mouseButton);
        this.postDescriptionField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.postNameField.isFocused())
        {
            this.postNameField.textboxKeyTyped(typedChar, keyCode);
        }
        else if (this.postDescriptionField.isFocused())
        {
            this.postDescriptionField.textboxKeyTyped(typedChar, keyCode);
        }
    }

    public static BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight) {
        BufferedImage scaledImage = null;
        if (imageToScale != null) {
            scaledImage = new BufferedImage(dWidth, dHeight, imageToScale.getType());
            Graphics2D graphics2D = scaledImage.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.drawImage(imageToScale, 0, 0, dWidth, dHeight, null);
            graphics2D.dispose();
        }
        return scaledImage;
    }
}
