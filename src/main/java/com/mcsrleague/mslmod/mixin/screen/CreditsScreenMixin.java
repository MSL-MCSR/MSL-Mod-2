package com.mcsrleague.mslmod.mixin.screen;

import com.mcsrleague.mslmod.MSLMod;
import com.mcsrleague.mslmod.screen.CompletionScreen;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreditsScreen.class)
public abstract class CreditsScreenMixin extends Screen {
    protected CreditsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void initMixin(CallbackInfo info) {
        assert client != null;
        if (MSLMod.isCompleted()) {
            String[] tokenAndTime = MSLMod.takeTokenAndTime();
            client.openScreen(new CompletionScreen(tokenAndTime[0], tokenAndTime[1], this));
        }
    }
}
