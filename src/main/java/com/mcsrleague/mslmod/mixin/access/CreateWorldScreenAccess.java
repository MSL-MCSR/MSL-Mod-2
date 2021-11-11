package com.mcsrleague.mslmod.mixin.access;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CreateWorldScreen.class)
public interface CreateWorldScreenAccess {
    @Accessor
    TextFieldWidget getLevelNameField();

    @Invoker("createLevel")
    void invokeCreateLevel();

    @Accessor
    void setField_24289(Difficulty field_24289);

    @Accessor
    void setField_24290(Difficulty field_24290);
}
