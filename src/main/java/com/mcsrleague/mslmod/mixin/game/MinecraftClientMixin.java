package com.mcsrleague.mslmod.mixin.game;

import com.mcsrleague.mslmod.MSLMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    @Final
    public TextRenderer textRenderer;
    @Shadow
    private Profiler profiler;
    @Shadow
    @Final
    private Window window;

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

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/ToastManager;draw(Lnet/minecraft/client/util/math/MatrixStack;)V", shift = At.Shift.AFTER))
    private void renderOnTopMixin(boolean tick, CallbackInfo info) {
        this.profiler.swap("timer");
        MSLMod.getTimer().render(new MatrixStack(), textRenderer, window.getScaledWidth(), window.getScaledHeight());
    }
}
