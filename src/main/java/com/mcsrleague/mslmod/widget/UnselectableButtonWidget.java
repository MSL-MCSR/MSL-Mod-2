package com.mcsrleague.mslmod.widget;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class UnselectableButtonWidget extends ButtonWidget {
    public UnselectableButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress);
    }

    @Override
    public boolean changeFocus(boolean x){
        return false;
    }
}
