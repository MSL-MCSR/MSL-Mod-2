package com.mcsrleague.mslmod.mixin.game;

import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugHud.class)
public abstract class DebugHudMixin {
    @Inject(method="getLeftText",at=@At("RETURN"),cancellable = true)
    private void addDebugLineMixin(CallbackInfoReturnable<List<String>> info){
        info.getReturnValue().add("MSL Mod Loaded");
    }
}
