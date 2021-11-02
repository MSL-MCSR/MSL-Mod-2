package com.mcsrleague.mslmod.screen;

import net.minecraft.client.gui.screen.TitleScreen;

public class FakeTitleScreen extends TitleScreen {
    @Override
    protected void init() {
        super.init();
        buttons.clear();
        children.clear();
    }
}
