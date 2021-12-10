package com.mcsrleague.mslmod.mixin.game;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Locale;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract void debugWarn(String string, Object... objects);

    @Shadow
    public abstract void setClipboard(String string);

    @Inject(method = "processF3", at = @At("HEAD"), cancellable = true)
    private void replaceF3CMixin(int key, CallbackInfoReturnable<Boolean> info) {
        if (key == 67) {
            assert this.client.player != null;
            if (this.client.player.getReducedDebugInfo()) {
                info.setReturnValue(false);
            } else {
                ClientPlayNetworkHandler clientPlayNetworkHandler = this.client.player.networkHandler;
                if (clientPlayNetworkHandler == null) {
                    info.setReturnValue(false);
                }

                this.debugWarn("debug.copy_location.message");
                this.setClipboard(String.format(Locale.ROOT, "%d %d %d | %.2f", (int) this.client.player.getX(), (int) this.client.player.getY(), (int) this.client.player.getZ(), this.client.player.yaw));
                info.setReturnValue(true);
            }
        }
    }
}
