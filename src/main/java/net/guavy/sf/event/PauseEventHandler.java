package net.guavy.sf.event;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class PauseEventHandler {
    @SubscribeEvent
    public void openPauseMenu(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() == null) return;

        if (event.getGui() instanceof GuiIngameMenu) {
            GuiIngameMenu gui = (GuiIngameMenu) event.getGui();
            List<GuiButton> buttons = event.getButtonList();

            buttons.add(new GuiButton(18, gui.width / 2 - 100, gui.height / 4 + 120 + 6, "Social"));
            event.setButtonList(buttons);
        }
    }
}
