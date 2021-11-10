package com.mcsrleague.mslmod.widget;

import net.minecraft.text.Text;

public class UnselectableButtonWidget extends SquishButtonWidget {
    public UnselectableButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress);
    }

    @Override
    public boolean changeFocus(boolean x) {
        return false;
    }
}
