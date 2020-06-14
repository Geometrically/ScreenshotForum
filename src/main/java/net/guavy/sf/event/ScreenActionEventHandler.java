package net.guavy.sf.event;

import net.guavy.sf.data.PacketHandler;
import net.guavy.sf.data.PacketRequestPosts;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ScreenActionEventHandler {
    @SubscribeEvent
    public void onScreenClick(GuiScreenEvent.ActionPerformedEvent event) {
        if (event.getGui() == null) return;

        if (event.getGui() instanceof GuiIngameMenu) {
            if(event.getButton().id == 18) {
                PacketHandler.INSTANCE.sendToServer(new PacketRequestPosts());
            }
        }
    }
}
