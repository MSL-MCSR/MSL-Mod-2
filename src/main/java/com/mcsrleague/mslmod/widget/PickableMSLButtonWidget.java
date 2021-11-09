package com.mcsrleague.mslmod.widget;

import net.minecraft.text.Text;

public class PickableMSLButtonWidget extends MSLButtonWidget implements PickableWidget {
    private boolean picked;

    public PickableMSLButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress);
    }

    @Override
    protected int getTextColor() {
        return isHovered() || picked ? 0 : 16777215;
    }

    @Override
    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    @Override
    protected int getYImage(boolean isHovered) {
        return super.getYImage(isHovered || picked);
    }
}
