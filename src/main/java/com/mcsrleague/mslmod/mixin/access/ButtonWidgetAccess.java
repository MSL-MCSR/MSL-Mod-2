package com.mcsrleague.mslmod.mixin.access;

import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ButtonWidget.class)
public interface ButtonWidgetAccess {
    @Accessor
    void setOnPress(ButtonWidget.PressAction onPress);
}
