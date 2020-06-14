package net.guavy.sf.event;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ScreenshotEventHandler {
    @SubscribeEvent
    public void onScreenshot(ScreenshotEvent event) {
        ITextComponent screenshotText = new TextComponentString(event.getScreenshotFile().getName() + " was saved. ");

        screenshotText.getStyle().setColor(TextFormatting.GOLD);
        screenshotText.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, event.getScreenshotFile().getAbsolutePath()));

        ITextComponent addonText = new TextComponentString("Click here to post about it!");
        addonText.getStyle().setColor(TextFormatting.GREEN);
        addonText.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/newpost " + event.getScreenshotFile().getAbsolutePath()));

        screenshotText.appendSibling(addonText);

        event.setResultMessage(screenshotText);
    }
}
