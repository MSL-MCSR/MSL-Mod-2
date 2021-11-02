package com.mcsrleague.mslmod.mixin.game;

import com.mcsrleague.mslmod.MSLMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = "openScreen", at = @At("HEAD"))
    private void openScreenMixin(Screen screen, CallbackInfo ci) {
        if (MSLMod.ooml()) {
            if (screen == null) {
                MSLMod.eo().unpause();
            } else if (screen instanceof GameMenuScreen) {
                MSLMod.eo().pause();
            }
        }
    }
}
