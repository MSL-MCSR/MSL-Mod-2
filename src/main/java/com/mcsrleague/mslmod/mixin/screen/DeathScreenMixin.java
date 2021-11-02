package com.mcsrleague.mslmod.mixin.screen;

import com.mcsrleague.mslmod.MSLMod;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {
    protected DeathScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void initMixin(CallbackInfo ci) {
        if (MSLMod.ooml()) {
            AbstractButtonWidget titleButton = null;
            for (AbstractButtonWidget button : buttons) {
                if (button.getMessage() instanceof TranslatableText && ((TranslatableText) button.getMessage()).getKey().equals("deathScreen.titleScreen")) {
                    titleButton = button;
                    break;
                }
            }
            if (titleButton != null) {
                buttons.remove(titleButton);
                children.remove(titleButton);
            }
        }
    }
}
